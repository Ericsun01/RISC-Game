package edu.duke.ece651.risc.shared;

import java.util.*;
/**
 *The class generates different actions automatically for AI players
 */
public class ComputerAction {
    private Random random;
    private Player player;

    /**
     * Constructor computerAction
     * @param player the player object that stores status
     */
    public ComputerAction(Player player) {
        this.random = new Random();
        this.player = player;
    }

    /**
     * Constructor computerAction
     * @param player the player object that stores status
     * @param seed the random seed for testing
     */
    public ComputerAction(int seed, Player player) {
        this.random = new Random(seed);
        this.player = player;
    }

    /**
     * Create random placements for computer player
     * @param totalNumOfUnits is the total number of units to place
     * @param map is used to get info
     * @param numOfTerritory is the number of territories that should place
     */
    public List<Action> createPlacementActions(int totalNumOfUnits, Map map, int numOfTerritory) {
        List<Action> computerPlacements = new ArrayList<>();
        Iterator<Territory> iterator = map.getTerritories();

        int numOfUnitsLeft = totalNumOfUnits;
        int numOflastTurn = 0;

        while (iterator.hasNext()) {
            Territory territory = iterator.next();

            if (territory.getOwner().getName().equals(player.getName())) {
                PlacementAction placementAction = null;
                ArrayList<Unit> unitsList = new ArrayList<>();
                numOfUnitsLeft -= numOflastTurn;
                if (computerPlacements.size() < numOfTerritory-1 && numOfUnitsLeft > 0) {
                    // generate random num between [1, numOfUnitsLeft]
                    numOflastTurn = random.nextInt(numOfUnitsLeft)+1;

                    for (int i=0; i<numOflastTurn; i++) {
                        unitsList.add(new Unit(0));
                    }
                    placementAction = new PlacementAction(player,territory,unitsList);
                } else {
                    // add one last placement action
                    if (numOfUnitsLeft <= 0) {
                        break;
                    }

                    for (int i=0; i<numOfUnitsLeft; i++) {
                        unitsList.add(new Unit(0));
                    }
                    placementAction = new PlacementAction(player,territory,unitsList);
                }

                computerPlacements.add(placementAction);
            }
        }

        return computerPlacements;
    }

    /**
     * Check does this territory belongs to this player
     * @param territory is the terriory for checking
     */
    private boolean isFriendlyTerritory(Territory territory) {
        return territory.getOwner().getName().equals(player.getName());
    }

    /**
     * Generate different actions for this round by different preference
     * @param map is the map for getting information
     */
    public List<Action> createRoundActions(Map map) {
        List<Action> roundActions = new ArrayList<>();
        HashMap<String, ArrayList<AttackAction>> commonTargetAttacks = new HashMap<>();

        // Generate MaxTechUpgradeAction
        MaxTechUpgradeAction maxTechUpgradeAction = generateMaxTechUpgradeAction();
        if (maxTechUpgradeAction != null) {
            maxTechUpgradeAction.check();
            roundActions.add(maxTechUpgradeAction);
        }

        // Generate and execute all unitUpgradeActions
        Iterator<Territory> upgradeActionIterator = map.getTerritories();
        while (upgradeActionIterator.hasNext()) {
            Territory territory = upgradeActionIterator.next();
            if (isFriendlyTerritory(territory)) {
                generateAndExecuteUnitUpgradeAction(territory);
            }
        }

        PriorityQueue<Territory> currentTerritoryQueue = getMaxHeapOfTerritories(map);
        Iterator<Territory> moveActionIterator = map.getTerritories();

        // Generate all moveActions
        while (moveActionIterator.hasNext()) {
            Territory territory = moveActionIterator.next();
            MoveAction moveAction = null;
            if (isFriendlyTerritory(territory) && !territory.isBomb()) {
                moveAction = generateMoveAction(territory, currentTerritoryQueue, map);
                if (moveAction != null) {
                    moveAction.check();
                    roundActions.add(moveAction);
                }
            }
        }

        //PriorityQueue<Territory> attackTerritoryQueue = getMaxHeapOfTerritories(map);
        Iterator<Territory> attackActionIterator = map.getTerritories();
        // Generate all attackActions
        while (attackActionIterator.hasNext()) {
            Territory territory = attackActionIterator.next();
            AttackAction attackAction = null;
            if (isFriendlyTerritory(territory) && !territory.isBomb()) {
                attackAction = generateAttackAction(territory);
                if (attackAction != null) {
                    attackAction.check();
                    commonTargetAttacks.putIfAbsent(attackAction.getTo().getName(), new ArrayList<AttackAction>());
                    commonTargetAttacks.get(attackAction.getTo().getName()).add(attackAction);
                }
            }
        }

        // merge attacks to same territory
        for (ArrayList<AttackAction> attackActions: commonTargetAttacks.values()) {
            if (attackActions.size() < 2) {
                roundActions.add(attackActions.get(0));
                continue;
            }
            for (int i = 1; i < attackActions.size(); i++) {
                attackActions.get(0).mergeAction(attackActions.get(i));
            }
            roundActions.add(attackActions.get(0));
        }

        return roundActions;
    }

    /**
     * Generate maxTechUpgradeAction first
     */
    public MaxTechUpgradeAction generateMaxTechUpgradeAction() {
        MaxTechUpgradeCost costTable = new MaxTechUpgradeCost();
        MaxTechUpgradeAction maxTechUpgradeAction = null;
        if (player.getMaxTechLevel() < 6 && player.getTechResources() >= costTable.getCostByLevel(player.getMaxTechLevel()+1)) {
            maxTechUpgradeAction = new MaxTechUpgradeAction(player, player.getMaxTechLevel()+1);
        }
        return maxTechUpgradeAction;
    }

    /**
     * Upgrade as much units to 1 level higher as possible
     * @param territory is the territory that being processed
     */
    public void generateAndExecuteUnitUpgradeAction(Territory territory) {
        HashMap<Integer, Integer> upgradeCost = new HashMap<>();  // the cost of upgrading to current level from previous level
        upgradeCost.put(0, 0);
        upgradeCost.put(1, 3);
        upgradeCost.put(2, 8);
        upgradeCost.put(3, 19);
        upgradeCost.put(4, 25);
        upgradeCost.put(5, 35);
        upgradeCost.put(6, 50);

        ArrayList<Unit> allUnits = territory.getUnits();
        for (Unit unit: allUnits) {
            if (unit.getLevel() < 6 && player.getTechResources() >= upgradeCost.get(unit.getLevel()+1)) {
                UnitUpgradeAction unitUpgradeAction = new UnitUpgradeAction(player, territory, unit.getLevel(), unit.getLevel()+1, 1);
                unitUpgradeAction.check();
                unitUpgradeAction.execute();
            } else {
                continue;
            }
        }
    }

    /**
     * Create MoveAction
     * If a territory is outnumbered by neighboring enemy, move some units from the territory that owns most units to this territory
     * @param territory is the territory that being processed
     * @param currentTerritoryQueue is the maxHeap that stores all friendly territories based on num of units
     * @param map is the map being processed
     */
    public MoveAction generateMoveAction(Territory territory, PriorityQueue<Territory> currentTerritoryQueue, Map map) {

        Iterable<Territory> neighbors = territory.getNeighbors();

        int enemySurroundingUnits = 0;
        for (Territory neighbor: neighbors) { // iterate it's neighbors
            if (!isFriendlyTerritory(neighbor) && !neighbor.isBomb()) {   // enemy territory neighbors
                enemySurroundingUnits += neighbor.getNumUnits();
            }
        }

        if (enemySurroundingUnits > territory.getNumUnits()) {
            Territory mostUnitsTerritory = currentTerritoryQueue.peek();
            if (!isSameTerritory(mostUnitsTerritory, territory)) {
                int bound = mostUnitsTerritory.getNumUnits()/2;
                if (bound <= 0) {
                    return null;
                }
                int numOfMove = random.nextInt(bound);
                ArrayList<Unit> units = mostUnitsTerritory.getUnits();
                ArrayList<Unit> moveUnits = new ArrayList<>();
                for (int i=0; i<numOfMove; i++) {
                    moveUnits.add(units.get(units.size()-1-i));
                }
                MoveAction moveAction = new MoveAction(player, territory, mostUnitsTerritory, moveUnits, map);
                return moveAction;
            }
        }
        return null;
    }

    private boolean isSameTerritory(Territory t1, Territory t2) {
        return t1.getName().equals(t2.getName());
    }

    /**
     * Create AttackAction
     * generate attackAction to the neighboring enemy territory that has lowest num of units
     * @param territory is the territory that being processed
     */
    public AttackAction generateAttackAction(Territory territory) {
        Iterable<Territory> neighbors = territory.getNeighbors();
        PriorityQueue<Territory> minHeap = new PriorityQueue<>((x, y) -> Integer.compare(x.getNumUnits(), y.getNumUnits()));
        for (Territory neighbor: neighbors) {  // Iterate all neighboring neighbors, generate attack actions towards Territory with smallest num of units
            if (!isFriendlyTerritory(neighbor) && !neighbor.isBomb()) {
                minHeap.offer(neighbor);
            }
        }

        Territory weakestEnemy = minHeap.peek();

        if (weakestEnemy != null) {
            AttackAction attackAction = generateFavorableAttackAction(territory, weakestEnemy);
            return attackAction;
        }

        return null;
    }

    /**
     * Calculate the bonus between two territories, and generate an attack if it will win for sure, or null for insufficient bouns
     * @param myTerritory is the territory that being processed
     * @param enemyTerritory is the territory that being processed
     */
    public AttackAction generateFavorableAttackAction(Territory myTerritory, Territory enemyTerritory) {
        ArrayList<Unit> myUnits = myTerritory.getUnits();
        ArrayList<Unit> enemyUnits = enemyTerritory.getUnits();
        Bonus b = new Bonus();

        ArrayList<Unit> fewestFavorableUnits = new ArrayList<>();

        int enemyBonus = 0;
        for (Unit u: enemyUnits) {
            enemyBonus += b.getBonusByLevel(u.getLevel()) + 10;
        }

        int myBonus = 0;
        Collections.reverse(myUnits);
        Iterator<Unit> myUnitIterator = myUnits.iterator();
        while (myBonus < enemyBonus + enemyUnits.size() && myUnitIterator.hasNext()) {
            Unit u = myUnitIterator.next();
            fewestFavorableUnits.add(u);
            myBonus += b.getBonusByLevel(u.getLevel()) + 10;
        }

        if (myBonus < enemyBonus + enemyUnits.size()) {
            return null;
        } else {
            return new AttackAction(player, enemyTerritory, myTerritory, fewestFavorableUnits);
        }
    }

    /**
     * Heap sort the territories based on the num of units they contain
     * @param map is the map being processed
     */
    public PriorityQueue<Territory> getMaxHeapOfTerritories(Map map) {
        PriorityQueue<Territory> answer = new PriorityQueue<>(Collections.reverseOrder((x, y) -> Integer.compare(x.getNumUnits(), y.getNumUnits())));
        for (Iterator<Territory> it = map.getTerritories(); it.hasNext(); ) {
            Territory t = it.next();
            if (t.getOwner().getName().equals(player.getName())) {
                answer.add(t);
            }
        }
        return answer;
    }

}
