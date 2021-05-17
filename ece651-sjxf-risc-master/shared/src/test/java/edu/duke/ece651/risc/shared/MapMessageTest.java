package edu.duke.ece651.risc.shared;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapConsumer extends Thread{
    private Player player;
    private MapMessage mapMessage;
    private Map map;

    public MapConsumer (Player player, MapMessage mapMessage) {
        this.player = player;
        this.mapMessage = mapMessage;
        this.map = null;
    }

    public void run () {
        map = mapMessage.getGameMap();
    }

    public Map getMap () {
        return this.map;
    }
}

class MapProducer extends Thread{
    private Player player;
    private MapMessage mapMessage;
    private Map map;

    public MapProducer (Player player, MapMessage mapMessage, Map map) {
        this.player = player;
        this.mapMessage = mapMessage;
        this.map = map;
    }

    public void run () {
        mapMessage.setGameMap(map);
    }
}

public class MapMessageTest {
    @Test
    public void test_MapMessage_Get_And_Set() {
        try {
            MapMessage mapMessage = new MapMessage();
            Player player1 = new Player("Bob");
            Player player2 = new Player("Sue");
            V2MapFactory factory = new V2MapFactory();
            Map map = factory.build2PlayerMap(player1, player2);

            MapProducer mapProducer = new MapProducer(player1, mapMessage, map);
            MapConsumer mapConsumer = new MapConsumer(player1, mapMessage);

            mapConsumer.start();
            Thread.sleep(100);

            Map playerMap = mapConsumer.getMap();
            assertNull(playerMap);

            mapProducer.start();
            Thread.sleep(100);

            playerMap = mapConsumer.getMap();
            String[] expectedTerritory = new String[6];
            expectedTerritory[0] = "t1";
            expectedTerritory[1] = "t2";
            expectedTerritory[2] = "t3";
            expectedTerritory[3] = "t4";
            expectedTerritory[4] = "t5";
            expectedTerritory[5] = "t6";

            assertFalse(mapMessage.getAvailable());

            Iterator<Territory> iterator = playerMap.getTerritories();
            int i=0;
            while (iterator.hasNext()) {
                Territory territory = iterator.next();
                assertEquals(expectedTerritory[i++], territory.getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}