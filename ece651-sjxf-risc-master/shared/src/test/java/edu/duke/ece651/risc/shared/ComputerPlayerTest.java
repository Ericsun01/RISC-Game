package edu.duke.ece651.risc.shared;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ComputerPlayerTest {
    @Test
    public void test_computerInitialPhase() {
        Player p1 = new Player("Bob", 0, 10, 100, 100);
        Player p2 = new Player("Sue", 0, 10, 100, 100);

        V2MapFactory factory = new V2MapFactory();
        Map map = factory.build2PlayerMap(p1, p2);
        Game game = new Game(map);

        ComputerPlayer cp1 = new ComputerPlayer(map, game, p1, 1, 5);
        cp1.computerInitialPhase();

        assertEquals(1, p1.getPlayerNumber());
        Iterator<Territory> iterator = map.getTerritories();
        while (iterator.hasNext()) {
            Territory territory = iterator.next();
            if (territory.getGroupID() == 1) {
                assertEquals("Bob", territory.getOwner().getName());
            }
        }
    }

    @Test
    public void test_computerPlacementPhase() {
        Player p1 = new Player("Bob", 0, 10, 100, 100);
        Player p2 = new Player("Sue", 0, 10, 100, 100);

        V2MapFactory factory = new V2MapFactory();
        Map map = factory.build2PlayerMap(p1, p2);
        Game game = new Game(map);

        ComputerPlayer cp1 = new ComputerPlayer(map, game, p1, 1, 5);
        cp1.computerPlacementPhase();

        assertFalse(p1.getRoundActions().getInfoUpdated());
        Iterable<Action> lists = p1.getRoundActions().getActions();
        int[] numToPlaceExpected = new int[3];
        numToPlaceExpected[0] = 8;
        numToPlaceExpected[1] = 1;
        numToPlaceExpected[2] = 1;

        String[] territoryToPlace = new String[3];
        territoryToPlace[0] = "t1";
        territoryToPlace[1] = "t2";
        territoryToPlace[2] = "t3";
        int i=0;
        for (Action action: lists) {
            if (action instanceof PlacementAction) {
                PlacementAction placementAction = (PlacementAction)action;
                assertEquals(numToPlaceExpected[i], placementAction.getNumUnits());
                assertEquals(territoryToPlace[i++], placementAction.getWhere().getTerritoryName());
            }
        }
    }

    @Test
    public void test_computerRoundActionsPhase() {
        Player p1 = new Player("Bob", 0, 10, 100, 100);
        Player p2 = new Player("Sue", 0, 10, 100, 100);

        V2MapFactory factory = new V2MapFactory();
        Map map = factory.build2PlayerMap(p1, p2);
        Game game = new Game(map);

        ComputerPlayer cp1 = new ComputerPlayer(map, game, p1, 1, 5);
        ComputerPlayer cp2 = new ComputerPlayer(map, game, p2, 2);
        cp1.computerPlacementPhase();

        Iterable<Action> lists = p1.getRoundActions().getActions();
        for (Action action: lists) {
            action.execute();
        }

        cp1.computerRoundActionsPhase();
        assertFalse(p1.getRoundActions().getInfoUpdated());

        String[] expectedActions = new String[4];
        expectedActions[0] = "MaxTechUpgradeAction";
        expectedActions[1] = "AttackAction";
        expectedActions[2] = "AttackAction";
        expectedActions[3] = "AttackAction";
        Iterable<Action> roundActionList = p1.getRoundActions().getActions();

        int i=0;
        for (Action action: roundActionList) {
            assertEquals(expectedActions[i++], action.getType());
        }

        assertSame(p1, cp1.getPlayer());
        assertFalse(cp1.isLose());
        assertFalse(cp2.isLose());
    }
}
