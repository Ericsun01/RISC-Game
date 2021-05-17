package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerRunnableTest {
    private ArrayList<Unit> createUnitList(int numOfUnits) {
        ArrayList<Unit> units = new ArrayList<>();
        for (int i=0; i<numOfUnits; i++) {
            units.add(new Unit(0));
        }
        return units;
    }

    @Test
    public void test_checkAndMergeGameActions () {
        Player p1 = new Player("Bob", 1, 0, 10, 10);
        Player p2 = new Player("Sue", 2, 10, 10, 10);
        p1.setMaxTechLevel(4);
        V2MapFactory factory = new V2MapFactory();
        Map map = factory.build2PlayerMap(p1, p2);
        Game game = new Game(map);
        Socket socket1 = new Socket();
        Socket socket2 = new Socket();
        List<Action> actionList = new ArrayList<>();
        List<Action> actionList2 = new ArrayList<>();
        List<Action> actionList3 = new ArrayList<>();
        List<Action> emptyList = new ArrayList<>();

        map.getTerritoryByName("t4").setOwner(p1);
        map.getTerritoryByName("t1").setUnits(createUnitList(8));
        map.getTerritoryByName("t2").setUnits(createUnitList(6));
        map.getTerritoryByName("t3").setUnits(createUnitList(6));
        map.getTerritoryByName("t4").setUnits(createUnitList(6));

        UnitUpgradeAction unitUpgradeAction = new UnitUpgradeAction(p1, map.getTerritoryByName("t1"), 0, 1, 1);
        SpyUpgradeAction spyUpgradeAction = new SpyUpgradeAction(p1, map.getTerritoryByName("t1"));
        MoveAction moveAction = new MoveAction(p1, map.getTerritoryByName("t1"), map.getTerritoryByName("t2"), createUnitList(2), map);
        ArrayList<Spy> spies = new ArrayList<>();
        spies.add(new Spy(p1));
        SpyMoveAction spyMoveAction = new SpyMoveAction(p1, map.getTerritoryByName("t1"), map.getTerritoryByName("t4"), spies, map);
        EmpyreanArmadaAction empyreanArmadaAction = new EmpyreanArmadaAction(p1, map.getTerritoryByName("t6"));
        PsionicStormAction psionicStormAction = new PsionicStormAction(p1, map.getTerritoryByName("t5"));
        AttackAction attackAction1 = new AttackAction(p1, map.getTerritoryByName("t5"), map.getTerritoryByName("t2"), createUnitList(2));
        AttackAction attackAction2 = new AttackAction(p1, map.getTerritoryByName("t5"), map.getTerritoryByName("t4"), createUnitList(2));
        MaxTechUpgradeAction maxTechUpgradeAction = new MaxTechUpgradeAction(p1, 5);
        CloakUpgradeAction cloakUpgradeAction = new CloakUpgradeAction(p1);
        CloakAction cloakAction = new CloakAction(p1, map.getTerritoryByName("t1"));
        TachyonicBombAction tachyonicBombAction = new TachyonicBombAction(p1, map.getTerritoryByName("t5"));

        actionList.add(unitUpgradeAction);
        actionList.add(spyUpgradeAction);
        actionList.add(moveAction);
        actionList.add(spyMoveAction);
        actionList.add(empyreanArmadaAction);
        actionList.add(psionicStormAction);
        actionList.add(attackAction1);
        actionList.add(attackAction2);
        actionList.add(maxTechUpgradeAction);
        actionList.add(cloakUpgradeAction);
        actionList.add(cloakAction);
        actionList.add(tachyonicBombAction);
        emptyList.add(null);

        ServerRunnable serverRunnable1 = new ServerRunnable(socket1, 1, map, p1, game);
        ServerRunnable serverRunnable2 = new ServerRunnable(socket2, 2, map, p2, game);

        serverRunnable1.checkAndMergeGameActions(actionList);
        serverRunnable2.checkAndMergeGameActions(emptyList);

        actionList2.add(attackAction1);
        p1.setTechResources(500);
        p1.setFoodResources(500);
        TachyonicBombAction tachyonicBombAction1 = new TachyonicBombAction(p1, map.getTerritoryByName("t5"));
        AttackAction attackAction3 = new AttackAction(p1, map.getTerritoryByName("t5"), map.getTerritoryByName("t2"), createUnitList(20));
        MoveAction moveAction1 = new MoveAction(p1, map.getTerritoryByName("t1"), map.getTerritoryByName("t2"), createUnitList(20), map);
        SpyMoveAction spyMoveAction1 = new SpyMoveAction(p1, map.getTerritoryByName("t4"), map.getTerritoryByName("t1"), spies, map);
        actionList2.add(tachyonicBombAction1);
        actionList2.add(attackAction3);
        actionList2.add(moveAction1);
        actionList2.add(spyMoveAction1);
        serverRunnable1.checkAndMergeGameActions(actionList2);


        AttackAction attackAction4 = new AttackAction(p1, map.getTerritoryByName("t5"), map.getTerritoryByName("t2"), createUnitList(1));
        actionList3.add(attackAction4);
        serverRunnable1.checkAndMergeGameActions(actionList3);
    }
}
