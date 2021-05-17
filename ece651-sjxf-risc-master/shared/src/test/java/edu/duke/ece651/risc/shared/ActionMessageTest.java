package edu.duke.ece651.risc.shared;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Consumer extends Thread{
    private Player player;
    private ActionMessage actionMessage;
    private List<Action> actionList;

    public Consumer (Player player, ActionMessage actionMessage) {
        this.player = player;
        this.actionMessage = actionMessage;
        this.actionList = null;
    }

    public void run () {
        actionList = actionMessage.getActionList();
        for (Action a: actionList) {
            if (a instanceof PlacementAction) {
                PlacementAction pa = (PlacementAction) a;
                //displayInfo(pa);
            }
        }
    }

    public List<Action> getActions() {
        return this.actionList;
    }

    public void displayInfo(PlacementAction placementAction) {
        String result = "Player "+placementAction.getPlayer().getName()+"\n"+
                "has placement actions on Territory :"+placementAction.getWhere().getName()+"\n"+
                "has units to place :"+placementAction.getNumUnits();
        System.out.println(result);
    }
}

class Producer extends Thread{
    private Player player;
    private ActionMessage actionMessage;
    private Map map;

    public Producer (Player player, ActionMessage actionMessage, Map map) {
        this.player = player;
        this.actionMessage = actionMessage;
        this.map = map;
    }

    public void run () {
        List<Action> list = new ArrayList<>();

        PlacementAction placementAction1 = new PlacementAction(player, map.getTerritoryByName("t1"), 5);
        PlacementAction placementAction2 = new PlacementAction(player, map.getTerritoryByName("t2"), 5);
        PlacementAction placementAction3 = new PlacementAction(player, map.getTerritoryByName("t3"), 5);
        list.add(placementAction1);
        list.add(placementAction2);
        list.add(placementAction3);

        actionMessage.setActionList(list);
    }
}

public class ActionMessageTest {
    @Test
    public void test_ActionMessage_Get_And_Set() {
        try {
            ActionMessage actionMessage = new ActionMessage();
            Player player1 = new Player("Bob");
            Player player2 = new Player("Sue");
            V2MapFactory factory = new V2MapFactory();
            Map map = factory.build2PlayerMap(player1, player2);

            Producer producer = new Producer(player1, actionMessage, map);
            Consumer consumer = new Consumer(player1, actionMessage);

            consumer.start();
            Thread.sleep(100);

            List<Action> actionList = consumer.getActions();
            assertNull(actionList);

            producer.start();
            Thread.sleep(100);

            actionList = consumer.getActions();
            String[] expectedTerritory = new String[3];
            expectedTerritory[0] = "t1";
            expectedTerritory[1] = "t2";
            expectedTerritory[2] = "t3";
            int i=0;
            for (Action a: actionList) {
                if (a instanceof PlacementAction) {
                    PlacementAction pa = (PlacementAction) a;
                    assertEquals(expectedTerritory[i++], pa.getWhere().getName());
                    assertEquals("Bob", pa.getPlayer().getName());
                    assertEquals(5, pa.getNumUnits());
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
