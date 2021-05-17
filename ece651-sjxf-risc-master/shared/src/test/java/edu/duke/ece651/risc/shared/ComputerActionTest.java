package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComputerActionTest {
    @Test
    public void test_createPlacementActions() {
        Player p1 = new Player("Bob", 1, 10, 100, 100);
        Player p2 = new Player("Eve", 2, 10, 100, 100);

        Map map = new Map();
        Territory t1 = new Territory(1, p1, "t1", 5, 10, 10);
        Territory t2 = new Territory(1, p1, "t2", 7, 15, 5);
        Territory t3 = new Territory(1, p1, "t3", 5, 10, 10);
        Territory t4 = new Territory(2, p2, "t4", 7, 15, 5);
        Territory t5 = new Territory(2, p2, "t5", 5, 10, 10);
        Territory t6 = new Territory(2, p2, "t6", 7, 15, 5);
        map.addTerritory(t1);
        map.addTerritory(t2);
        map.addTerritory(t3);
        map.addTerritory(t4);
        map.addTerritory(t5);
        map.addTerritory(t6);
        Game game = new Game(map);

        ComputerAction ca1 = new ComputerAction(10, p1);
        List<Action> actionList = ca1.createPlacementActions(10, map, 3);
        for (Action a: actionList) {
            if (a instanceof PlacementAction) {
                PlacementAction pa = (PlacementAction)a;
                pa.check();
                pa.execute();
            }
        }

        Iterator<Territory> iterator = map.getTerritories();
        int[] numOfUnitsExpected = new int[3];
        numOfUnitsExpected[0] = 4;
        numOfUnitsExpected[1] = 1;
        numOfUnitsExpected[2] = 5;
        int i=0;
        while (iterator.hasNext()) {
            Territory territory = iterator.next();
            if (territory.getOwner().getName().equals(p1.getName())) {
                assertEquals(territory.getNumUnits(), numOfUnitsExpected[i++]);
            }
           // System.out.println(territory.getOwner().getName()+"'s territory has units: "+territory.getNumUnits());
        }

        //displayView(p1, map);

    }

    private void simulateAttack(ArrayList<Unit> t1Units, ArrayList<Unit> t2Units, boolean shouldWin, int numUnitsExpected) {
        Game game = new Game();
        Player p1 = new Player("p1", 1, 10, 10, 10);
        Player p2 = new Player("p2", 2, 10, 10, 10);
        Random r1 = new Random(0);
        Random r2 = new Random(1);

        int wins = 0;
        for (int i = 0; i < 1000; i++) {
            Territory t1 = new Territory(p1, "t1", 0,10, 10, 10);
            Territory t2 = new Territory(p2, "t2", 0, 10, 10, 10);
            t1.addNeighbor(t2);

            Map map = new Map();
            map.addTerritory(t1);
            map.addTerritory(t2);

            for (Unit u: t1Units) {
                t1.addUnit(u);
            }

            for (Unit u: t2Units) {
                t2.addUnit(u);
            }

            //ComputerPlayer cp = new ComputerPlayer(map, game, p1, 0);
            ComputerAction ca = new ComputerAction(p1);
            AttackAction aa = ca.generateFavorableAttackAction(t1, t2);
            if (aa != null) {
                aa.check();
                aa.execute(new Dice(r1), new Dice(r2));
            } else {
//                System.out.println(cp.calculateTotalBonus(t1.getUnits()));
//                System.out.println(cp.calculateTotalBonus(t2.getUnits()));
                break;
            }

            if (t2.getOwner().equals(p1)) {
                wins++;
            }
            if (wins == 1) {
                assertEquals(numUnitsExpected, aa.getNumUnits());
            }
        }
        System.out.println(wins);
        if (shouldWin) {
            assertTrue(wins > 500);
        } else {
            assertTrue(wins == 0);
        }
    }

    private ArrayList<Unit> generateUnitsArray(int lvl0, int lvl1, int lvl2, int lvl3, int lvl4, int lvl5, int lvl6) {
        ArrayList<Unit> answer = new ArrayList<>();
        for (int i = 0; i < lvl0; i++) {
            answer.add(new Unit(0));
        }
        for (int i = 0; i < lvl1; i++) {
            answer.add(new Unit(1));
        }
        for (int i = 0; i < lvl2; i++) {
            answer.add(new Unit(2));
        }
        for (int i = 0; i < lvl3; i++) {
            answer.add(new Unit(3));
        }
        for (int i = 0; i < lvl4; i++) {
            answer.add(new Unit(4));
        }
        for (int i = 0; i < lvl5; i++) {
            answer.add(new Unit(5));
        }
        for (int i = 0; i < lvl6; i++) {
            answer.add(new Unit(6));
        }
        return answer;
    }

    @Test
    public void test_generateFavorableAttackAction() {
        ArrayList<Unit> t1 = new ArrayList<>();
        t1.add(new Unit(2));
        t1.add(new Unit(0));

        ArrayList<Unit> t2 = new ArrayList<>();
        t2.add(new Unit(0));
        t2.add(new Unit(1));

        simulateAttack(t1, t2, true, 2);

        t1 = generateUnitsArray(20,0,0,30,0,0,1);
        t2 = generateUnitsArray(20,0,0,30,0,0,0);
        simulateAttack(t1, t2, false, 0);

        t1 = generateUnitsArray(21, 0, 0, 30, 0, 2, 0);
        t2 = generateUnitsArray(20, 0, 0, 30, 0, 0, 0);
        simulateAttack(t1, t2, true, 53);

        t1 = generateUnitsArray(0, 0, 0, 0, 0, 0, 20);
        t2 = generateUnitsArray(20, 0, 0, 0, 0, 0, 0);
        simulateAttack(t1, t2, true, 9);
    }

    @Test
    public void test_getMaxHeap() {
        Player p = new Player("Bob", 1);
        Player p2 = new Player("Sue", 2);
        Territory t1 = new Territory(p, "t1", 1, 0, 0, 0);
        Territory t2 = new Territory(p, "t2", 5, 0, 0, 0);
        Territory t3 = new Territory(p, "t3", 23, 0, 0, 0);
        Territory t4 = new Territory(p, "t4", 8, 0, 0, 0);
        Territory t5 = new Territory(p2, "t5", 53, 0, 0, 0);

        Map m = new Map();
        m.addTerritory(t1);
        m.addTerritory(t2);
        m.addTerritory(t3);
        m.addTerritory(t4);
        m.addTerritory(t5);

        ComputerAction computerAction = new ComputerAction(p);
        PriorityQueue<Territory> heap = computerAction.getMaxHeapOfTerritories(m);

        assertEquals(heap.poll().getNumUnits(), 23);
        assertEquals(heap.poll().getNumUnits(), 8);
        assertEquals(heap.poll().getNumUnits(), 5);
        assertEquals(heap.poll().getNumUnits(), 1);
    }

    @Test
    public void test_generateMaxTechUpgradeAction() {
        V2MapFactory factory = new V2MapFactory();
        Player p1 = new Player("Bob", 1, 10, 100, 50);
        Player p2 = new Player("Sue", 2, 10, 100, 50);
        Map map = factory.build2PlayerMap(p1, p2);
        PlacementAction[] array = new PlacementAction[6];
        array[0] = new PlacementAction(p1, map.getTerritoryByName("t1"), 2);
        array[1] = new PlacementAction(p1, map.getTerritoryByName("t2"), 3);
        array[2] = new PlacementAction(p1, map.getTerritoryByName("t3"), 5);
        array[3] = new PlacementAction(p2, map.getTerritoryByName("t4"), 8);
        array[4] = new PlacementAction(p2, map.getTerritoryByName("t5"), 1);
        array[5] = new PlacementAction(p2, map.getTerritoryByName("t6"), 1);
        for (int i=0; i<6; i++) {
            array[i].check();
            array[i].execute();
        }
        ComputerAction computerAction = new ComputerAction(5, p1);
        assertEquals(50, p1.getTechResources());
        assertEquals(1, p1.getMaxTechLevel());
//        System.out.println("Bob's tech resource before maxTechUpgrade: "+p1.getTechResources());
//        System.out.println("Bob's tech level before maxTechUpgrade: "+p1.getMaxTechLevel());
        MaxTechUpgradeAction maxTechUpgradeAction = computerAction.generateMaxTechUpgradeAction();
        maxTechUpgradeAction.check();
        maxTechUpgradeAction.execute();
        assertEquals(0, p1.getTechResources());
        assertEquals(2, p1.getMaxTechLevel());
//        System.out.println("Bob's tech resource after maxTechUpgrade: "+p1.getTechResources());
//        System.out.println("Bob's tech level after maxTechUpgrade: "+p1.getMaxTechLevel());

    }

    @Test
    public void test_generateAndExecuteUnitUpgradeAction() {
        V2MapFactory factory = new V2MapFactory();
        Player p1 = new Player("Bob", 1, 10, 100, 28);
        Player p2 = new Player("Sue", 2, 10, 100, 50);
        Map map = factory.build2PlayerMap(p1, p2);
        PlacementAction[] array = new PlacementAction[6];
        array[0] = new PlacementAction(p1, map.getTerritoryByName("t1"), 2);
        array[1] = new PlacementAction(p1, map.getTerritoryByName("t2"), 3);
        array[2] = new PlacementAction(p1, map.getTerritoryByName("t3"), 5);
        array[3] = new PlacementAction(p2, map.getTerritoryByName("t4"), 8);
        array[4] = new PlacementAction(p2, map.getTerritoryByName("t5"), 1);
        array[5] = new PlacementAction(p2, map.getTerritoryByName("t6"), 1);
        for (int i=0; i<6; i++) {
            array[i].check();
            array[i].execute();
        }

        //System.out.println("Bob's techResource before execute UnitUpgrade: "+p1.getTechResources());
        assertEquals(28, p1.getTechResources());
        ComputerAction computerAction = new ComputerAction(5, p1);
        computerAction.generateAndExecuteUnitUpgradeAction(map.getTerritoryByName("t1"));
        computerAction.generateAndExecuteUnitUpgradeAction(map.getTerritoryByName("t2"));
        computerAction.generateAndExecuteUnitUpgradeAction(map.getTerritoryByName("t3"));
//        System.out.println("Bob's techResource after execute UnitUpgrade: "+p1.getTechResources());
//        System.out.println("t1 has level 1 units: "+map.getTerritoryByName("t1").getNumUnitsByLevel(1));
//        System.out.println("t2 has level 1 units: "+map.getTerritoryByName("t2").getNumUnitsByLevel(1));
//        System.out.println("t3 has level 1 units: "+map.getTerritoryByName("t3").getNumUnitsByLevel(1));
        assertEquals(1, p1.getTechResources());
        assertEquals(2, map.getTerritoryByName("t1").getNumUnitsByLevel(1));
        assertEquals(3, map.getTerritoryByName("t2").getNumUnitsByLevel(1));
        assertEquals(4, map.getTerritoryByName("t3").getNumUnitsByLevel(1));
    }

    @Test
    public void test_generateMoveAction() {
        V2MapFactory factory = new V2MapFactory();
        Player p1 = new Player("Bob", 100);
        Player p2 = new Player("Sue", 100);
        Map map = factory.build2PlayerMap(p1, p2);
        PlacementAction[] array = new PlacementAction[6];
        array[0] = new PlacementAction(p1, map.getTerritoryByName("t1"), 2);
        array[1] = new PlacementAction(p1, map.getTerritoryByName("t2"), 3);
        array[2] = new PlacementAction(p1, map.getTerritoryByName("t3"), 5);
        array[3] = new PlacementAction(p2, map.getTerritoryByName("t4"), 8);
        array[4] = new PlacementAction(p2, map.getTerritoryByName("t5"), 1);
        array[5] = new PlacementAction(p2, map.getTerritoryByName("t6"), 1);
        for (int i=0; i<6; i++) {
            array[i].check();
            array[i].execute();
        }

        ComputerAction computerAction = new ComputerAction(5, p1);
        PriorityQueue<Territory> friendlyTerritoryHeap = computerAction.getMaxHeapOfTerritories(map);
        MoveAction moveAction = computerAction.generateMoveAction(map.getTerritoryByName("t1"), friendlyTerritoryHeap, map);
        moveAction.check();
        moveAction.execute();
        assertEquals(map.getTerritoryByName("t1").getNumUnits(), 3);
        //System.out.println(map.getTerritoryByName("t1").getNumUnits());
    }

    @Test
    public void test_generateAttackAction() {
        V2MapFactory factory = new V2MapFactory();
        Player p1 = new Player("Bob", 100);
        Player p2 = new Player("Sue", 100);
        Map map = factory.build2PlayerMap(p1, p2);
        PlacementAction[] array = new PlacementAction[6];
        array[0] = new PlacementAction(p1, map.getTerritoryByName("t1"), 3);
        array[1] = new PlacementAction(p1, map.getTerritoryByName("t2"), 2);
        array[2] = new PlacementAction(p1, map.getTerritoryByName("t3"), 5);
        array[3] = new PlacementAction(p2, map.getTerritoryByName("t4"), 1);
        array[4] = new PlacementAction(p2, map.getTerritoryByName("t5"), 2);
        array[5] = new PlacementAction(p2, map.getTerritoryByName("t6"), 7);
        for (int i=0; i<6; i++) {
            array[i].check();
            array[i].execute();
        }

        ComputerAction computerAction = new ComputerAction(5, p1);
        AttackAction attackAction = computerAction.generateAttackAction(map.getTerritoryByName("t1"));
        assertEquals("Bob", map.getTerritoryByName("t1").getOwner().getName());
        assertEquals("Sue", map.getTerritoryByName("t4").getOwner().getName());
        assertEquals(3, map.getTerritoryByName("t1").getNumUnits());

//        System.out.println("Before attack, t1 belongs to "+map.getTerritoryByName("t1").getOwner().getName()+", has units: "+map.getTerritoryByName("t1").getNumUnits());
//        System.out.println("Before attack, t4 belongs to "+map.getTerritoryByName("t4").getOwner().getName()+", has units: "+map.getTerritoryByName("t4").getNumUnits());

        attackAction.check();
        Random random1 = new Random(0);
        Random random2 = new Random(10);
        attackAction.execute(new Dice(random1), new Dice(random2));

        assertEquals("Bob", map.getTerritoryByName("t1").getOwner().getName());
        //assertEquals("Bob", map.getTerritoryByName("t4").getOwner().getName());
        assertEquals(1, map.getTerritoryByName("t1").getNumUnits());

//        System.out.println("After attack, t1 belongs to "+map.getTerritoryByName("t1").getOwner().getName()+", has units: "+map.getTerritoryByName("t1").getNumUnits());
//        System.out.println("After attack, t4 belongs to "+map.getTerritoryByName("t4").getOwner().getName()+", has units: "+map.getTerritoryByName("t4").getNumUnits());

    }

    private void displayView(Player player, Map map) {
        Iterator<Territory> iter = map.getTerritories();
        System.out.println("Player's name: "+player.getName()+", has level "+player.getMaxTechLevel()+", has food resource: "+player.getFoodResources()+", has tech resource: "+player.getTechResources());
        while (iter.hasNext()) {
            Territory ter = iter.next();
            String view = "Territory "+ter.getTerritoryName()+": \n" +
                    "The owner is: " + ter.getOwner().getName() + "\n" +
                    "The num of units is: " + ter.getNumUnits();
            System.out.println(view);
        }
    }

    @Test
    public void test_createRoundActions() {
        V2MapFactory factory = new V2MapFactory();
        Player p1 = new Player("Bob", 1, 10, 100, 100);
        Player p2 = new Player("Sue", 2, 10, 100, 100);
        Map map = factory.build2PlayerMap(p1, p2);
        PlacementAction[] array = new PlacementAction[6];
        array[0] = new PlacementAction(p1, map.getTerritoryByName("t1"), 2);
        array[1] = new PlacementAction(p1, map.getTerritoryByName("t2"), 3);
        array[2] = new PlacementAction(p1, map.getTerritoryByName("t3"), 5);
        array[3] = new PlacementAction(p2, map.getTerritoryByName("t4"), 4);
        array[4] = new PlacementAction(p2, map.getTerritoryByName("t5"), 4);
        array[5] = new PlacementAction(p2, map.getTerritoryByName("t6"), 2);
        for (int i=0; i<6; i++) {
            array[i].check();
            array[i].execute();
        }

        //displayView(p1, map);
        ComputerAction computerAction = new ComputerAction(5, p1);
        List<Action> actionList = computerAction.createRoundActions(map);
        List<MoveAction> moveActionList = new ArrayList<>();
        List<AttackAction> attackActionList = new ArrayList<>();
        List<MaxTechUpgradeAction> upgradeActionList = new ArrayList<>();

        for (Action action: actionList) {
            if (action instanceof MoveAction) {
                MoveAction moveAction = (MoveAction)action;
                moveActionList.add(moveAction);
            } else if (action instanceof MaxTechUpgradeAction) {
                MaxTechUpgradeAction maxTechUpgradeAction = (MaxTechUpgradeAction)action;
                upgradeActionList.add(maxTechUpgradeAction);
            } else if (action instanceof AttackAction) {
                AttackAction attackAction = (AttackAction)action;
                attackActionList.add(attackAction);
            }
        }

        if (upgradeActionList.size() > 0) {
            for (MaxTechUpgradeAction max: upgradeActionList) {
                //System.out.println(max.getType()+" Execution !");
                max.execute();
            }
        }

        if (moveActionList.size() > 0) {
            for (MoveAction ma: moveActionList) {
                //System.out.println("Move from "+ma.getFrom().getName()+", to "+ma.getTo().getName()+", with "+ma.getNumUnits()+" units");
                ma.execute();
            }
        }

        if (attackActionList.size() > 0) {
            for (AttackAction atk: attackActionList) {
                //System.out.println("Attack from "+atk.getFrom().getName()+", to "+atk.getTo().getName()+", with "+atk.getNumUnits()+" units");
                Random r1 = new Random(5);
                Random r2 = new Random(5);
                atk.execute(new Dice(r1), new Dice(r2));
            }
        }

        //System.out.println(map.getTerritoryByName("t6").getNumUnitsByLevel(1));
        //displayView(p1, map);
        assertEquals(3, map.getTerritoryByName("t1").getNumUnits());
        assertEquals(3, map.getTerritoryByName("t2").getNumUnits());
        assertEquals(2, map.getTerritoryByName("t3").getNumUnits());
        assertEquals("Bob", map.getTerritoryByName("t6").getOwner().getName());
        assertEquals(2, map.getTerritoryByName("t6").getNumUnits());
    }
}
