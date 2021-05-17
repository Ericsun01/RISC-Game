package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoundMessageConsumer extends Thread{
    private Player player;
    private RoundMessage roundMessage;
    private RoundResult roundResult;

    public RoundMessageConsumer (Player player, RoundMessage roundMessage) {
        this.player = player;
        this.roundMessage = roundMessage;
        this.roundResult = null;
    }

    public void run () {
        roundResult = roundMessage.getRoundResult();
    }

    public RoundResult getGameResult() {
        return this.roundResult;
    }
}

class RoundMessageProducer extends Thread{
    private Player player;
    private RoundMessage roundMessage;
    private RoundResult roundResult;

    public RoundMessageProducer (Player player, RoundMessage roundMessage, RoundResult roundResult) {
        this.player = player;
        this.roundMessage = roundMessage;
        this.roundResult = roundResult;
    }

    public void run () {
        roundMessage.setRoundResult(roundResult);
    }
}

public class RoundMessageTest {
    @Test
    public void test_RoundMessage_Get_And_Set() {
        try {
            RoundMessage roundMessage = new RoundMessage();

            Player player1 = new Player("Bob");
            Player player2 = new Player("Sue");
            V2MapFactory factory = new V2MapFactory();
            Map map = factory.build2PlayerMap(player1, player2);

            RoundResult roundResult =  new RoundResult(map, player1);

            RoundMessageProducer roundMessageProducer = new RoundMessageProducer(player1, roundMessage, roundResult);
            RoundMessageConsumer roundMessageConsumer = new RoundMessageConsumer(player1, roundMessage);

            roundMessageConsumer.start();
            Thread.sleep(100);

            RoundResult roundResult1 = roundMessageConsumer.getGameResult();
            assertNull(roundResult1);

            roundMessageProducer.start();
            Thread.sleep(100);

            roundResult1 = roundMessageConsumer.getGameResult();
            Player player = roundResult1.getPlayer();
            Map map1 = roundResult1.getMap();

            assertEquals("Bob", player.getName());
            assertEquals(6, map1.getNumTerritories());
            assertFalse(roundMessage.getAvailable());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

