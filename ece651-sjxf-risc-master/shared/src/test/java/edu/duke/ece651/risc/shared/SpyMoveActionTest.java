package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import jdk.jfr.Timestamp;

public class SpyMoveActionTest {
  public Map createMap(Player p1, Player p2){
    // owner, name, numUnits, size, foodProduction, techProduction
    Territory t1 = new Territory(p1, "t1", 10, 4, 5, 5);
    Territory t2 = new Territory(p1, "t2", 10, 2, 5, 5);
    Territory t3 = new Territory(p1, "t3", 10, 5, 5, 5);
    Territory t4 = new Territory(p1, "t4", 10, 6, 5, 5);
    Territory t5 = new Territory(p2, "t5", 10, 4, 5, 5);
    Territory t6 = new Territory(p2, "t6", 5, 4, 5, 5);
    t1.addNeighbor(t2);
    t1.addNeighbor(t3);
    t2.addNeighbor(t3);
    t2.addNeighbor(t4);
    t3.addNeighbor(t4);
    t4.addNeighbor(t5);
    t5.addNeighbor(t6);
    Map map = new Map();
    map.addTerritory(t1);
    map.addTerritory(t2);
    map.addTerritory(t3);
    map.addTerritory(t4);
    map.addTerritory(t5);
    map.addTerritory(t6);
    t1.addSpy(p1);
    t1.addSpy(p1);
    t1.addSpy(p1);
    t1.addSpy(p1);
    t1.addSpy(p1);
    t1.addSpy(p1);
    t4.addSpy(p1);
    t4.addSpy(p2);
    return map;
  }

  @Test
  public void test_getType() {
    Player alice = new Player("Alice", 10);
    Territory t1 = new Territory(alice, "t1", 10, 5, 10, 10);
    Territory t2 = new Territory(alice, "t2", 10, 6, 10, 10);
    Spy s1 = new Spy(alice);
    ArrayList<Spy> spylist = new ArrayList<>();
    spylist.add(s1);

    SpyMoveAction sma = new SpyMoveAction(alice, t1, t2, 1, spylist, null);
    assertEquals(sma.getType(), "SpyMoveAction");
  }

  @Test
  public void test_check(){
    Player bob = new Player("Bob", 10);
    Player alice = new Player("Alice", 10);
    Map map = createMap(bob, alice);

    Spy s1 = new Spy(bob);
    Spy s2 = new Spy(bob);
    Spy s3 = new Spy(alice);
    Spy s4 = new Spy(bob);
    Spy s5 = new Spy(bob);
    Unit u1 = new Unit(0);
    ArrayList<Spy> sl1 = new ArrayList<>();
    sl1.add(s1);
    sl1.add(s2);
    ArrayList<Spy> sl2 = new ArrayList<>();
    sl2.add(s1);
    ArrayList<Spy> sl3 = new ArrayList<>();
    sl3.add(s1);
    sl3.add(s2);
    sl3.add(s4);
    sl3.add(s5);

    System.out.println("At first, In t1, Bob has "+map.getTerritoryByName("t1").getSpiesOf(bob));
    SpyMoveAction sma1 = new SpyMoveAction(bob, map.getTerritoryByName("t2"), map.getTerritoryByName("t1"), 2, sl1, map);

    SpyMoveAction smaNEW = new SpyMoveAction(bob, map.getTerritoryByName("t2"), map.getTerritoryByName("t1"), sl1, map);
    map.getTerritoryByName("t2").setBomb(true);
    assertNotNull(sma1.check());
    map.getTerritoryByName("t2").setBomb(false);
    
    assertNull(sma1.check()); // valid
    // System.out.println("After first check, In t1, Bob has "+map.getTerritoryByName("t1").getSpiesOf(bob));
    SpyMoveAction sma2 = new SpyMoveAction(bob, map.getTerritoryByName("t5"), map.getTerritoryByName("t4"), 2, sl1, map);
    assertNotNull(sma2.check()); // has 2 spies in that territory, but only one of that owner
    SpyMoveAction sma3 = new SpyMoveAction(bob, map.getTerritoryByName("t6"), map.getTerritoryByName("t4"), 2, sl1, map);
    assertNotNull(sma3.check()); // across not neighbor and not the same owner
  // how to test not spy unit? 
    // System.out.println("Before 4, In t1, Bob has "+map.getTerritoryByName("t1").getSpiesOf(bob));
    SpyMoveAction sma4 = new SpyMoveAction(alice, map.getTerritoryByName("t5"), map.getTerritoryByName("t4"), 1, sl2, map);
    assertEquals(sma4.check(),"Invalid action! You are moving a spy that not belongs to you!");
    // System.out.println("Before invalid, In t1, Bob has "+map.getTerritoryByName("t1").getSpiesOf(bob));
    SpyMoveAction sma5 = new SpyMoveAction(bob, map.getTerritoryByName("t3"), map.getTerritoryByName("t1"), 4, sl3, map);
    // System.out.println("In t1, Bob has "+map.getTerritoryByName("t1").getSpiesOf(bob));
    // System.out.println("In t4, Bob has "+map.getTerritoryByName("t4").getSpiesOf(bob));
    // System.out.println("In t4, Alice has "+map.getTerritoryByName("t4").getSpiesOf(alice));
    assertEquals(sma5.check(),"Invalid action from t1 to t3. t1 only has 2 food resources left.");// not enough food resource
    smaNEW.getSpies();
    smaNEW.getSpiesNum();
  }

  @Test
  public void test_execute(){
    Player bob = new Player("Bob", 10);
    Player alice = new Player("Alice", 10);
    Map map = createMap(bob, alice);

    Spy s1 = new Spy(bob);
    Spy s2 = new Spy(bob);
    Spy s3 = new Spy(alice);
    Spy s4 = new Spy(bob);
    Spy s5 = new Spy(bob);
    Unit u1 = new Unit(0);
    ArrayList<Spy> sl1 = new ArrayList<>();
    sl1.add(s1);
    sl1.add(s2);
    ArrayList<Spy> sl2 = new ArrayList<>();
    sl2.add(s1);
    ArrayList<Spy> sl3 = new ArrayList<>();
    sl3.add(s1);
    sl3.add(s2);
    sl3.add(s4);
    sl3.add(s5);

    System.out.println("At first, In t1, Bob has "+map.getTerritoryByName("t1").getSpiesOf(bob));
    assertEquals(map.getTerritoryByName("t1").getSpiesOf(bob), 6);
    SpyMoveAction sma1 = new SpyMoveAction(bob, map.getTerritoryByName("t2"), map.getTerritoryByName("t1"), 2, sl1, map);
    assertNull(sma1.check());
    assertEquals(map.getTerritoryByName("t1").getSpiesOf(bob), 4);
    assertEquals(map.getTerritoryByName("t2").getSpiesOf(bob), 0);
    sma1.execute();
    assertEquals(map.getTerritoryByName("t1").getSpiesOf(bob), 4);
    assertEquals(map.getTerritoryByName("t2").getSpiesOf(bob), 2);
  }
}
