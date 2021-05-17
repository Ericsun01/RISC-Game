package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class PlacementActionTest {

  public ArrayList<Unit> createListUnits (int numUnit) {
    ArrayList<Unit> list = new ArrayList<>();
    for (int i=0; i<numUnit; i++) {
      list.add(new Unit(0));
    }
    return list;
  }

  @Test
  public void test_check_invalid() {
    Player bob = new Player("Bob", 0, 10, 10, 10);
    Territory t1 = new Territory(bob, "NYC", 5, 5, 5);
    Player player = new Player("Alice", 1, 10, 10, 10);
    PlacementAction p1 = new PlacementAction(player, t1, createListUnits(5));
    assertEquals(p1.check(), "You can only place units on your own territory!");

    assertNull(p1.check(new Map()));
    assertEquals(p1.getNumUnits(), 5);
  }

  @Test
  public void test_execute(){
    Map map = new Map();
    Player bob = new Player("Bob", 0, 10, 10, 10);
    Territory t1 = new Territory(bob, "DH", 5, 5, 5);
    map.addTerritory(t1);
    PlacementAction p1 = new PlacementAction(bob, t1, createListUnits(5));
    assertNull(p1.check());
    p1.execute();
    assertEquals(map.getTerritoryByName("DH").getNumUnits(), 5);
  }

  @Test
  public void test_getType(){
    Player bob = new Player("Bob", 0, 10, 10, 10);
    Territory t1 = new Territory(bob, "CL",5, 5, 5);
    PlacementAction p1 = new PlacementAction(bob, t1, createListUnits(5));

    assertEquals(p1.getType(), "PlacementAction");
    p1.setOwner(bob);
  }
}
