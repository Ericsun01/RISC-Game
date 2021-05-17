package edu.duke.ece651.risc.shared;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientSocketTest {
//    @Test
//    public void test_clientRun() throws IOException, ClassNotFoundException {
//        Player p1 = new Player("Bob", 1, 0, 10, 10);
//        Player p2 = new Player("Sue", 2, 10, 10, 10);
//
//        V2MapFactory factory = new V2MapFactory();
//        Map map = factory.build2PlayerMap(p1, p2);
//        Map map2 = factory.build2PlayerMap(p1, p2);
//        Game game = new Game(map);
//
//        Player player1 = new Player("Junqi", 1, 0, 10, 10, false);
//
//        LoginMessage loginMessage1 = new LoginMessage();
//        MapMessage mapMessage1 = new MapMessage();
//        ActionMessage actionMessage1 = new ActionMessage();
//        RoundMessage roundMessage1 = new RoundMessage();
//
//        ClientSocket clientSocket = new ClientSocket("localhost", 6071, player1, map, loginMessage1, mapMessage1, actionMessage1, roundMessage1);
//        RoundResult roundResult1 = null;
//        RoundResult roundResult = new RoundResult(map2, player1);
//        ArrayList<Unit> units = new ArrayList<>();
//        units.add(new Unit(0));
//        units.add(new Unit(0));
//        units.add(new Unit(0));
//        MoveAction ma = new MoveAction(player1, map.getTerritoryByName("t1"), map.getTerritoryByName("t2"), units, map);
//        List<Action> actionList = new ArrayList<>();
//        actionList.add(ma);
//
//        ObjectInputStream mockIs = mock(ObjectInputStream.class);
//        when(mockIs.readObject()).thenReturn(roundResult);
//        ObjectOutputStream mockOs = mock(ObjectOutputStream.class);
//
//        actionMessage1.setActionList(actionList);
//        clientSocket.setObjectStream(mockOs, mockIs);
//
//        String ans = clientSocket.sendActionsRecvResult(roundResult1);
//        System.out.println(ans);
//    }

    @Test
    public void test_endGameMessage() {
        Player p1 = new Player("Bob", 1, 0, 10, 10);
        Player p2 = new Player("Sue", 2, 10, 10, 10);

        Map map = new Map();
        Territory t1 = new Territory(1, p2, "t1", 1, 20, 30);
        Territory t2 = new Territory(1, p2, "t2", 2, 40, 60);
        Territory t3 = new Territory(1, p2, "t3", 1, 20, 30);
        Territory t4 = new Territory(2, p2, "t4", 1, 20, 30);
        Territory t5 = new Territory(2, p2, "t5", 2, 40, 60);
        Territory t6 = new Territory(2, p2, "t6", 1, 20, 30);

        t1.addNeighbor(t2);
        t1.addNeighbor(t4);
        t2.addNeighbor(t3);
        t2.addNeighbor(t5);
        t3.addNeighbor(t6);
        t4.addNeighbor(t5);
        t5.addNeighbor(t6);

        map.addTerritory(t1);
        map.addTerritory(t2);
        map.addTerritory(t3);
        map.addTerritory(t4);
        map.addTerritory(t5);
        map.addTerritory(t6);


        LoginMessage loginMessage1 = new LoginMessage();
        MapMessage mapMessage1 = new MapMessage();
        ActionMessage actionMessage1 = new ActionMessage();
        RoundMessage roundMessage1 = new RoundMessage();

        LoginMessage loginMessage2 = new LoginMessage();
        MapMessage mapMessage2 = new MapMessage();
        ActionMessage actionMessage2 = new ActionMessage();
        RoundMessage roundMessage2 = new RoundMessage();

        ClientSocket clientSocket1 = new ClientSocket("localhost", 6071, p1, map, loginMessage1, mapMessage1, actionMessage1, roundMessage1);
        ClientSocket clientSocket2 = new ClientSocket("localhost", 6071, p2, map, loginMessage2, mapMessage2, actionMessage2, roundMessage2);
        String ans1 = clientSocket1.endGameMessage();
        String ans2 = clientSocket2.endGameMessage();
        assertEquals("Player "+t1.getOwner().getName()+" wins the game, You lose", ans1);
        assertEquals("Congratuations ! You win the game", ans2);
    }
}
