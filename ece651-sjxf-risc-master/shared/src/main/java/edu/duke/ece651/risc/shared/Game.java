package edu.duke.ece651.risc.shared;

import javax.sql.rowset.spi.TransactionalWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    private List<Player> players;
    private List<ComputerPlayer> computerPlayers;
    private volatile Map map;
    private HashMap<String, String> loginInfo;
    private List<Action> placements;
    private List<TwoTerritoryAction> moves;
    private List<TwoTerritoryAction> attacks;
    private List<MaxTechUpgradeAction> techLevelUpgrades;
    private List<CloakUpgradeAction> cloakUpgradeActions;
    private List<CloakAction> cloakActions;
    private List<EmpyreanArmadaAction> empyreanArmadaActions;
    private List<PsionicStormAction> psionicStormActions;
    private List<TachyonicBombAction> tachyonicBombActions;
    private volatile boolean roundIsOver;  // indicate all actions has been executed
    private volatile boolean gameOver; // indicate game is over
    private HashMap<CloakAction, Integer> cloakTable; // <Cloak, remaining rounds of cloaking>
    private HashMap<TachyonicBombAction, Integer> bombTable; // <bomb, remaining rounds of no move and no attack and no spymove>
  

    public Game(Map map) {
        this.players = new ArrayList<>();
        this.computerPlayers = new ArrayList<>();
        this.map = map;
        this.loginInfo = new HashMap<>();
        this.placements = new ArrayList<>();
        this.moves = new ArrayList<>();
        this.attacks = new ArrayList<>();
        this.techLevelUpgrades = new ArrayList<>();
        this.cloakUpgradeActions = new ArrayList<>();
        this.cloakActions = new ArrayList<>();
        this.empyreanArmadaActions = new ArrayList<>();
        this.psionicStormActions = new ArrayList<>();
        this.tachyonicBombActions = new ArrayList<>();
        this.roundIsOver = false;
        this.gameOver = false;
        this.cloakTable = new HashMap<>();
        this.bombTable = new HashMap<>();
    }

    // Only for Game testing
    public Game() {
        this(new Map());
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void addComputerPlayer(ComputerPlayer computerPlayer) {this.computerPlayers.add(computerPlayer);}

    public ComputerPlayer findComputerPlayerByPlayer(Player player) {
        for (ComputerPlayer cp: computerPlayers) {
            if (cp.getPlayer().getName().equals(player.getName())) {
                return cp;
            }
        }
        return null;
    }

    public Iterator<Player> getPlayer() {
        return players.iterator();
    }

    public Map getMap() {
        return this.map;
    }

    public synchronized void setTerritoryOwner(int groupID, Player player) {
        Iterator<Territory> iter = map.getTerritories();
        while (iter.hasNext()) {
            Territory ter = iter.next();
            if (ter.getGroupID() == groupID) {
                ter.setOwner(player);
            }
        }
    }

    // For testing
    public HashMap<String, String> getLoginInfo() {
        return loginInfo;
    }

    public void recordLoginInfo() {
        for (Player player: players) {
            if (player.getIsAI()) {
                continue;
            }
            String[] info = player.getLoginMessage().getInformation();
            String userName = info[0];
            String password = info[1];
            //TODO: Need more logic in the later steps
            if (loginInfo.containsKey(userName)) {
                if (loginInfo.get(userName).equals(password)) {
                    System.out.println("Welcome back, player "+userName);
                } else {
                    System.out.println("Wrong password, please type again, player "+userName);
                }
                continue;
            }
            loginInfo.put(userName, password);
        }
    }

    public void computerPlayerInitialize() {
        for (ComputerPlayer cp: computerPlayers) {
            cp.computerInitialPhase();
        }
    }

    public void waitForRoundEnd() throws InterruptedException{
        boolean allPlayersUpdated;
        do {
            allPlayersUpdated = true;
            for (Player player: players) {
                allPlayersUpdated = allPlayersUpdated && player.getRoundActions().getInfoUpdated();
            }
            Thread.sleep(1000);
            System.out.println("Game: wait for round end-- are you stuck here?");
        } while (!allPlayersUpdated);
    }

    public void doPlacementRound() throws InterruptedException {
        for (Player player: players) {
            if (player.getIsAI()) {
                System.out.println("Game: Oops! You are an AI!");
                ComputerPlayer computerPlayer = findComputerPlayerByPlayer(player);
                computerPlayer.computerPlacementPhase();
            }

            Iterable<Action> iter = player.getRoundActions().getActions();
            for (Action a: iter) {
                this.placements.add(a);
            }
            //player.stageClear = true;
        }
        System.out.println("Game: Ready to do placementActions");
        doActions(placements);
        roundIsOver = true;
        for (Player player: players) {
            if (player.getIsAI()) {
                player.getRoundActions().setInfoUpdated(true);
            }
        }
        waitForRoundEnd();
        // placements = new ArrayList<>();
    }

    public void addOneUnitByTurn() {
        Iterator<Territory> iterator = map.getTerritories();
        while (iterator.hasNext()) {
            Territory ter = iterator.next();
            if (!ter.isBomb()) {
                ter.addUnit(new Unit(0));
            }
        }
    }

    public void addResourceByTurn() {
        for (Player player: players) {
            Iterator<Territory> iterator = map.getTerritories();
            while (iterator.hasNext()) {
                Territory ter = iterator.next();
                if (ter.getOwner().getName().equals(player.getName())) {
                    player.addResource(ter.getFoodProduction(),ter.getTechProduction());
                }
            }
            System.out.println("Game : "+player.getName()+" food resource is "+player.getFoodResources()+", your tech resource is "+player.getTechResources());
        }
    }

    public <T extends Action> void doActions(List<T> list) {
        if (list.size() == 0) {
            return;
        }

        for (T a: list) {
            a.execute();
        }
    }

    public <T extends Action> void doTimedAction(List<T> list, HashMap<T, Integer> table, int time) {
        if (list.size() == 0) {
            return;
        }

        for (T a: list) {
            a.execute();
            table.put(a, time);
        }
    }

    public void updateTachyonicBombResult() {
        if (bombTable.isEmpty()) {
            return;
        }

        Iterator<java.util.Map.Entry<TachyonicBombAction, Integer>> iterator = bombTable.entrySet().iterator();
        while (iterator.hasNext()) {
            java.util.Map.Entry<TachyonicBombAction, Integer> entry = iterator.next();
            if (entry.getValue() > 0) {
                bombTable.put(entry.getKey(), entry.getValue()-1);
            }
            if (entry.getValue() <= 0) {
                entry.getKey().getTo().setBomb(false);
                iterator.remove();
            }
        }
    }

  public void updateCloakResult() {
        if (cloakTable.isEmpty()) {
            return;
        }

        Iterator<java.util.Map.Entry<CloakAction, Integer>> iterator = cloakTable.entrySet().iterator();
        while (iterator.hasNext()) {
            java.util.Map.Entry<CloakAction, Integer> entry = iterator.next();
            if (entry.getValue() > 0) {
                cloakTable.put(entry.getKey(), entry.getValue()-1);
            }
            if (entry.getValue()  <= 0) {
                entry.getKey().getCloakArea().setCloaked(false);
                iterator.remove();
            }
        }
    }

    private boolean computerAliveBuildActions(int i, Player player) {
        ComputerPlayer computerPlayer = findComputerPlayerByPlayer(player);
        //System.out.println("Game: get your player: "+player.getName());
        if (!computerPlayer.isLose()) {
            System.out.println("Game: start generate the actions for round "+i+", "+player.getName());
            computerPlayer.computerRoundActionsPhase();
            return true;
        } else {
            System.out.println("Game: You lose here, don't do anything stupid, "+player.getName());
            player.getRoundActions().setInfoUpdated(true);
            return false;
        }
    }

    private void parseActions(Player player) {
        Iterable<Action> actions = player.getRoundActions().getActions();
        for (Action a: actions) {
            if (a == null) {
                System.out.println("Game: Don't provide null action, "+player.getName());
                continue;
            }
            System.out.println("Gathering actions for player "+player.getName());
            if (a instanceof MoveAction) {
                MoveAction m = (MoveAction)a;
                moves.add(m);
            } else if (a instanceof SpyMoveAction) {
                SpyMoveAction sma = (SpyMoveAction) a;
                moves.add(sma);
            } else if (a instanceof AttackAction) {
                AttackAction at = (AttackAction)a;
                attacks.add(at);
            } else if (a instanceof MaxTechUpgradeAction) {
                MaxTechUpgradeAction mtua = (MaxTechUpgradeAction) a;
                techLevelUpgrades.add(mtua);
            } else if (a instanceof CloakUpgradeAction) {
                CloakUpgradeAction cua = (CloakUpgradeAction)a;
                cloakUpgradeActions.add(cua);
            } else if (a instanceof CloakAction) {
                CloakAction ca = (CloakAction)a;
                cloakActions.add(ca);
            } else if (a instanceof EmpyreanArmadaAction) {
                EmpyreanArmadaAction eaa = (EmpyreanArmadaAction) a;
                empyreanArmadaActions.add(eaa);
            } else if (a instanceof PsionicStormAction) {
                PsionicStormAction psi = (PsionicStormAction) a;
                psionicStormActions.add(psi);
            } else if (a instanceof TachyonicBombAction){
                TachyonicBombAction ta = (TachyonicBombAction)a;
                tachyonicBombActions.add(ta);
            }
        }
    }

    private void actionExecution() {
        doActions(moves);
        doActions(attacks);
        doActions(techLevelUpgrades);
        doActions(cloakUpgradeActions);
        doTimedAction(cloakActions, cloakTable, 3);
        doActions(empyreanArmadaActions);
        doActions(psionicStormActions);
        doTimedAction(tachyonicBombActions, bombTable, 2);

        moves.clear();
        attacks.clear();
        techLevelUpgrades.clear();
        cloakUpgradeActions.clear();
        cloakActions.clear();
        empyreanArmadaActions.clear();
        psionicStormActions.clear();
        tachyonicBombActions.clear();
    }

    private void production() {
        addOneUnitByTurn();
        addResourceByTurn();
    }

    public void gamePlay() throws InterruptedException{
        int i=1;
        while (!gameIsOver()) {
            System.out.println("Game : Round "+(i));
            updateCloakResult();
            updateTachyonicBombResult();

            roundIsOver = false;
            for (Player player: players) {
                if (player.getIsAI() && !computerAliveBuildActions(i, player)) {
                    continue;
                }

                parseActions(player);
                System.out.println("Gather all actions for player "+player.getName());
            }

            actionExecution();

            production();
            roundIsOver = true;
            i++;

            for (Player player: players) {
                if (player.getIsAI()) {
                    player.getRoundActions().setInfoUpdated(true);
                }
            }
            waitForRoundEnd();
        }
        System.out.println("Game is Over! the winner is " + getWinner().getName());
        gameOver = true;
    }

    public synchronized boolean gameIsOver() {
        Iterator<Territory> iter = map.getTerritories();
        String ownerName = iter.next().getOwner().getName();
        while (iter.hasNext()) {
            Territory ter = iter.next();
            if (!ownerName.equals(ter.getOwner().getName())) {
                return false;
            }
        }
        return true;
    }

    public Player getWinner() {
        Iterator<Territory> iterator = map.getTerritories();
        Territory territory = iterator.next();
        Player player = territory.getOwner();

        return player;
    }

    public synchronized boolean getRoundIsOver() {
        return roundIsOver;
    }

    public synchronized boolean getGameOver() {
        return this.gameOver;
    }

    public HashMap<CloakAction, Integer> getCloakTable() {
        return cloakTable;
    }

	public HashMap<TachyonicBombAction, Integer> getBombTable() {
		return bombTable;
	}

}
