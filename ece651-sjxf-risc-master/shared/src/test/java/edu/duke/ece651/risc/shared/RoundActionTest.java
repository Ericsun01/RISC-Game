package edu.duke.ece651.risc.shared;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoundActionConsumer extends Thread{
    private Player player;
    private RoundActions roundActions;
    private Iterable<Action> actionList;

    public RoundActionConsumer (Player player, RoundActions roundActions) {
        this.player = player;
        this.roundActions = roundActions;
        this.actionList = null;
    }

    public void run () {
        actionList = roundActions.getActions();
    }

    public Iterable<Action> getGameActions() {
        return this.actionList;
    }
}

class RoundActionProducer extends Thread{
    private Player player;
    private RoundActions roundActions;
    private List<Action> actionList;

    public RoundActionProducer (Player player, RoundActions roundActions, List<Action> actions) {
        this.player = player;
        this.roundActions = roundActions;
        this.actionList = actions;
    }

    public void run () {
        roundActions.setActions(actionList);
    }
}

public class RoundActionTest {
    @Test
    public void test_RoundAction_Get_And_Set() {
        try {
            RoundActions roundActions = new RoundActions();
            Player player1 = new Player("Bob");
            Player player2 = new Player("Sue");
            V2MapFactory factory = new V2MapFactory();
            Map map = factory.build2PlayerMap(player1, player2);

            ArrayList<Unit> units1 = new ArrayList<>();
            units1.add(new Unit(0));
            units1.add(new Unit(0));
            units1.add(new Unit(0));
            MoveAction moveAction = new MoveAction(player1, map.getTerritoryByName("t1"), map.getTerritoryByName("t2"), units1, map);
            AttackAction attackAction = new AttackAction(player1, map.getTerritoryByName("t3"), map.getTerritoryByName("t6"), 2);

            List<Action> actionList = new ArrayList<>();
            actionList.add(moveAction);
            actionList.add(attackAction);

            RoundActionProducer producer = new RoundActionProducer(player1, roundActions, actionList);
            RoundActionConsumer consumer = new RoundActionConsumer(player1, roundActions);

            consumer.start();
            Thread.sleep(100);

            Iterable<Action> actions = consumer.getGameActions();
            assertNull(actions);

            producer.start();
            Thread.sleep(100);
            roundActions.setInfoUpdated(true);

            actions = consumer.getGameActions();
            String[] expectedActionType = new String[2];
            expectedActionType[0] = "MoveAction";
            expectedActionType[1] = "AttackAction";
            int i=0;
            for (Action a: actions) {
                assertEquals(expectedActionType[i++], a.getType());
            }

            assertFalse(roundActions.getAvailable());
            assertTrue(roundActions.getInfoUpdated());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
