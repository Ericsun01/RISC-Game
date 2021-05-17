package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

public class MoveActionTest {

  public Map createMap(Player p1, Player p2){
    // owner, name, numUnits, size, foodProduction, techProduction
    Territory t1 = new Territory(p1, "t1", 10, 4, 5, 5);
    Territory t2 = new Territory(p1, "t2", 10, 2, 5, 5);
    Territory t3 = new Territory(p1, "t3", 10, 5, 5, 5);
    Territory t4 = new Territory(p1, "t4", 10, 6, 5, 5);
    Territory t5 = new Territory(p2, "t5", 10, 4, 5, 5);
    Territory t6 = new Territory(p1, "t6", 5, 4, 5, 5);
    t1.addNeighbor(t2);
    t1.addNeighbor(t3);
    t2.addNeighbor(t3);
    t2.addNeighbor(t4);
    t3.addNeighbor(t4);
    t4.addNeighbor(t5);
    Map map = new Map();
    map.addTerritory(t1);
    map.addTerritory(t2);
    map.addTerritory(t3);
    map.addTerritory(t4);
    map.addTerritory(t5);
    map.addTerritory(t6);
    return map;
  }
  @Test
  public void test_min_dist() {
    Player bob = new Player("Bob", 0, 10, 10, 10);
    Territory t1 = new Territory(bob, "t1",4, 5, 5);
    Territory t2 = new Territory(bob, "t2",2, 5, 5);
    Territory t3 = new Territory(bob, "t3",5, 5, 5);
    Territory t4 = new Territory(bob, "t4",6, 5, 5);
    t1.addNeighbor(t2);
    t1.addNeighbor(t3);
    t2.addNeighbor(t3);
    t2.addNeighbor(t4);
    t3.addNeighbor(t4);
    Unit u1 = new Unit(3);
    Unit u2 = new Unit(2);
    Unit u3 = new Unit(1);
    ArrayList<Unit> Action_units = new ArrayList<>();
    Action_units.add(u1);
    Action_units.add(u2);
    Action_units.add(u3);
    Collections.sort(Action_units);

    Map map = new Map();
    map.addTerritory(t1);
    map.addTerritory(t2);
    map.addTerritory(t3);
    map.addTerritory(t4);
    MoveAction ma = new MoveAction(bob, t1, t4, 3, Action_units);
    assertEquals(Utility.find_min_size(map, t1, t4),6);
  }

  @Test
  public void test_getType() {
    Player bob = new Player("Bob", 10);
    Territory t1 = new Territory(bob, "t1", 10, 5, 10, 10);
    Territory t2 = new Territory(bob, "t2", 10, 6, 10, 10);

    Unit u1 = new Unit(3);
    Unit u2 = new Unit(2);
    Unit u3 = new Unit(1);
    ArrayList<Unit> t1_units = new ArrayList<>();
    t1_units.add(u1);
    t1_units.add(u2);
    t1_units.add(u3);

    MoveAction ma = new MoveAction(bob, t1, t2, 3, t1_units);
    assertEquals(ma.getType(), "MoveAction");
  }

  @Test
  public void test_check(){
    Player bob = new Player("Bob", 10);
    Player alice = new Player("Alice", 10);
    Map map = createMap(bob, alice);

    Unit u1 = new Unit(3);
    Unit u2 = new Unit(2);
    Unit u3 = new Unit(1);
    ArrayList<Unit> t1_units = new ArrayList<>();
    t1_units.add(u1);
    t1_units.add(u2);
    t1_units.add(u3);

    MoveAction ma = new MoveAction(bob, map.getTerritoryByName("t1"), map.getTerritoryByName("t2"),  t1_units, map);
    map.getTerritoryByName("t1").setBomb(true);
    assertNotNull(ma.check(map));

    // System.out.println("BOB has " + bob.getFoodResources()  + " and this action need "  + Utility.find_min_size(map, map.getTerritoryByName("t2"), map.getTerritoryByName("t1")) *ma.getNumUnits());
    map.getTerritoryByName("t1").setBomb(false);
    assertNull(ma.check(map)); // valid
    // not enough food resource
    MoveAction ma2 = new MoveAction(bob, map.getTerritoryByName("t2"), map.getTerritoryByName("t1"),  t1_units, map);

    assertNotNull(ma2.check(map));
    // destination not owner
    MoveAction ma3 = new MoveAction(bob, map.getTerritoryByName("t1"), map.getTerritoryByName("t5"), t1_units, map);

    assertNotNull(ma3.check(map));
    // connection fail
    MoveAction ma4 = new MoveAction(bob, map.getTerritoryByName("t1"), map.getTerritoryByName("t6"),  t1_units, map);
    assertNotNull(ma4.check());
    //assertNull(ma.check()); // a dummy line for 100% coverage, should be deleted
  }
  

  @Test
  public void test_execute(){
    Player bob = new Player("Bob", 10);
    Map map = createMap(bob, null);

    Unit u1 = new Unit(0);
    Unit u2 = new Unit(0);
    Unit u3 = new Unit(0);
    ArrayList<Unit> t1_units = new ArrayList<>();
    t1_units.add(u1);
    t1_units.add(u2);
    t1_units.add(u3);

    MoveAction ma = new MoveAction(bob, map.getTerritoryByName("t1"), map.getTerritoryByName("t2"), t1_units, map);

    ma.check(map);
    ma.execute();
    assertEquals(map.getTerritoryByName("t1").getNumUnits(), 13);
    assertEquals(map.getTerritoryByName("t2").getNumUnits(), 7);
  }
}







