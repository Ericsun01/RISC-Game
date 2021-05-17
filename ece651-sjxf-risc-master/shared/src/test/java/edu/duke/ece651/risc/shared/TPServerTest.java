package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TPServerTest {

  @Test
  public void test_PVP_run() {
    try {
      Player p1 = new Player("Bob", 1, 0, 10, 10);
      Player p2 = new Player("Sue", 2, 10, 10, 10);

      V2MapFactory factory = new V2MapFactory();
      Map map = factory.build2PlayerMap(p1, p2);
      Game game = new Game(map);

      Player player1 = new Player("Junqi", 1, 0, 10, 10, false);
      Player player2 = new Player("Stephen", 2, 20, 10, 10, false);

      LoginMessage loginMessage1 = new LoginMessage();
      MapMessage mapMessage1 = new MapMessage();
      ActionMessage actionMessage1 = new ActionMessage();
      RoundMessage roundMessage1 = new RoundMessage();

      LoginMessage loginMessage2 = new LoginMessage();
      MapMessage mapMessage2 = new MapMessage();
      ActionMessage actionMessage2 = new ActionMessage();
      RoundMessage roundMessage2 = new RoundMessage();

      loginMessage1.setInformation("Junqi","123");
      loginMessage2.setInformation("Stephen", "321");

      PlacementAction pa1 = new PlacementAction(player1, map.getTerritoryByName("t1"), 0);
      PlacementAction pa2 = new PlacementAction(player1, map.getTerritoryByName("t2"), 0);
      PlacementAction pa3 = new PlacementAction(player1, map.getTerritoryByName("t3"), 0);
      PlacementAction pa4 = new PlacementAction(player2, map.getTerritoryByName("t4"), 8);
      PlacementAction pa5 = new PlacementAction(player2, map.getTerritoryByName("t5"), 6);
      PlacementAction pa6 = new PlacementAction(player2, map.getTerritoryByName("t6"), 6);
      List<Action> list1 = new ArrayList<>();
      List<Action> list2 = new ArrayList<>();
      List<Action> list3 = new ArrayList<>();
      List<Action> list4 = new ArrayList<>();

      AttackAction atk1 = new AttackAction(player2, map.getTerritoryByName("t1"), map.getTerritoryByName("t4"), 8);
      AttackAction atk2 = new AttackAction(player2, map.getTerritoryByName("t2"), map.getTerritoryByName("t5"), 6);
      AttackAction atk3 = new AttackAction(player2, map.getTerritoryByName("t3"), map.getTerritoryByName("t6"), 6);

      list1.add(pa1);
      list1.add(pa2);
      list1.add(pa3);
      list2.add(pa4);
      list2.add(pa5);
      list2.add(pa6);
      list4.add(atk1);
      list4.add(atk2);
      list4.add(atk3);


      ClientSocket cs1 = new ClientSocket("localhost", 6070, player1, map, loginMessage1, mapMessage1, actionMessage1, roundMessage1);
      ClientSocket cs2 = new ClientSocket("localhost", 6070, player2, map, loginMessage2, mapMessage2, actionMessage2, roundMessage2);
      TPServer tp = new TPServer(6070, 2,0, game);
      TPServer tp2 = new TPServer(6090, 2, game);

      tp.start();
      cs1.start();
      cs2.start();

      Thread.sleep(2000);
      actionMessage1.setActionList(list1);
      actionMessage2.setActionList(list2);
      Thread.sleep(2000);
      actionMessage1.setActionList(list3);
      actionMessage2.setActionList(list4);
      Thread.sleep(2000);


    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

    @Test
    public void test_PVE_run() {
        try {
            Player p1 = new Player("Bob", 1, 10, 10, 10);
            Player p2 = new Player("Sue", 2, 10, 10, 10);

            V2MapFactory factory = new V2MapFactory();
            Map map = factory.build2PlayerMap(p1, p2);
            Game game = new Game(map);

            Player player1 = new Player("Junqi", 1, 0, 10, 10, false);

            LoginMessage loginMessage1 = new LoginMessage();
            MapMessage mapMessage1 = new MapMessage();
            ActionMessage actionMessage1 = new ActionMessage();
            RoundMessage roundMessage1 = new RoundMessage();

            loginMessage1.setInformation("Junqi","123");


            PlacementAction pa1 = new PlacementAction(player1, map.getTerritoryByName("t1"), 4);
            PlacementAction pa2 = new PlacementAction(player1, map.getTerritoryByName("t2"), 3);
            PlacementAction pa3 = new PlacementAction(player1, map.getTerritoryByName("t3"), 3);

            List<Action> list1 = new ArrayList<>();
            List<Action> list2 = new ArrayList<>();


            AttackAction atk1 = new AttackAction(player1, map.getTerritoryByName("t4"), map.getTerritoryByName("t1"), 4);
            AttackAction atk2 = new AttackAction(player1, map.getTerritoryByName("t5"), map.getTerritoryByName("t2"), 3);
            AttackAction atk3 = new AttackAction(player1, map.getTerritoryByName("t6"), map.getTerritoryByName("t3"), 3);

            list1.add(pa1);
            list1.add(pa2);
            list1.add(pa3);
            list2.add(atk1);
            list2.add(atk2);
            list2.add(atk3);



            ClientSocket cs1 = new ClientSocket("localhost", 6080, player1, map, loginMessage1, mapMessage1, actionMessage1, roundMessage1);
            TPServer tp = new TPServer(6080, 1,1, game);

            cs1.start();
            tp.start();
            Thread.sleep(2000);
            actionMessage1.setActionList(list1);
            Thread.sleep(2000);
            actionMessage1.setActionList(list2);
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

