package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.junit.jupiter.api.Test;

public class AttackActionTest {
  @Test
  public void test_execute_defender_win() {
    Player michael = new Player("Michael", 0, 10, 10, 10);
    Player wen = new Player("WEN", 1, 10, 10,10);
    Territory to = new Territory(michael, "Durham",4, 5, 5, 5);
    Territory from = new Territory(wen, "China",5, 5, 5, 5);
    
    Unit u1 = new Unit(3);
    Unit u2 = new Unit(2);
    Unit u3 = new Unit(1);

    Unit u4 = new Unit(4);
    Unit u5 = new Unit(5);
    Unit u6 = new Unit(1);
    Unit u7 = new Unit(3);

    ArrayList<Unit> Action_units = new ArrayList<>();
    Action_units.add(u1);
    Action_units.add(u2);
    Action_units.add(u3);
    Collections.sort(Action_units);

    ArrayList<Unit> to_units = new ArrayList<>();
    to_units.add(u4);
    to_units.add(u5);
    to_units.add(u6);
    to_units.add(u7);
    Collections.sort(to_units);

    to.setUnits(to_units);

    AttackAction a = new AttackAction(wen, to, from, 3, Action_units);

    Random r1 = new Random(1);
    Random r2 = new Random(2);
    Dice attackDice = new Dice(r1);
    Dice defendDice = new Dice(r2);

    //assertEquals(a.check(),"s");
    //assertEquals(wen.getQueuedFoodResources(), 3);
    a.execute(attackDice, defendDice);
    assertTrue(to.getOwner().equals(michael));
    //assertEquals(wen.getQueuedFoodResources(), 0);
  }

  @Test
  public void test_execute_attackWin() {
    Player michael = new Player("Michael", 0, 10, 10, 10);
    Player wen = new Player("WEN", 1, 10,10, 10);
    Territory to = new Territory(michael, "Durham",1, 5, 5, 5);
    Territory from = new Territory(wen, "China",5, 5, 5, 5);
    
    Unit u1 = new Unit(3);
    Unit u2 = new Unit(2);
    Unit u3 = new Unit(1);

    Unit u4 = new Unit(4);
    Unit u5 = new Unit(5);
    Unit u6 = new Unit(1);
    Unit u7 = new Unit(3);

    ArrayList<Unit> Action_units = new ArrayList<>();
    Action_units.add(u1);
    Action_units.add(u2);
    Action_units.add(u3);
    Collections.sort(Action_units);

    ArrayList<Unit> to_units = new ArrayList<>();
    to_units.add(u4);

    to.setUnits(to_units);

    AttackAction a = new AttackAction(wen, to, from, 3, Action_units);

    Random r1 = new Random(1);
    Random r2 = new Random(2);
    Dice attackDice = new Dice(r1);
    Dice defendDice = new Dice(r2);

    a.execute(attackDice, defendDice);
    assertTrue(to.getOwner().equals(wen));
    assertEquals(a.getType(), "AttackAction");
  }

  @Test
  public void test_checker() {
    Player p1 = new Player("Bob",2);
    Player p2 = new Player("Michael",2);
    
    Territory t1 = new Territory(p2, "t1", 3, 2,2,2);
    Territory t2 = new Territory(p1, "t2", 4,2,2,2);
    t1.addNeighbor(t2);
    Map map = new Map();
    map.addTerritory(t1);
    map.addTerritory(t2);
    t1.setBomb(true);
    AttackAction m1 = new AttackAction(p1, t1, t2, 2);
    assertNotNull(m1.check());
    t1.setBomb(false);
    assertNull(m1.check());
    assertEquals(t2.getNumUnits(), 2);
    //m1.execute(map);
    //assertEquals(t2.getNumUnits(), 2);
    assertEquals(p1.getQueuedFoodResources(), 2);
    assertEquals(p1.getFoodResources(),0);

  }

  @Test
  public void test_map() {
    Player michael = new Player("Michael");
    Player wen = new Player("WEN");
    Territory to = new Territory(michael, "Durham",4, 5, 5, 5);
    Territory from = new Territory(wen, "China",5, 5, 5, 5);
    to.addNeighbor(from);

    Map m = new Map();
    m.addTerritory(to);
    m.addTerritory(from);

    Unit u1 = new Unit(0);
    Unit u2 = new Unit(0);
    Unit u3 = new Unit(0);

    ArrayList<Unit> units = new ArrayList<>();
    units.add(u1);
    units.add(u2);
    units.add(u3);

    AttackAction aa = new AttackAction(wen, to, from, units);
    aa.check(m);
    aa.execute(m);

    wen.setFoodResources(300);
    aa.check(m);

  }

  @Test
  public void test_executemap() {

    Player michael = new Player("Michael");
    Player wen = new Player("WEN");
    Territory to = new Territory(michael, "Durham",4, 5, 5, 5);
    Territory from = new Territory(wen, "China",5, 5, 5, 5);
    
    Unit u1 = new Unit(3);
    Unit u2 = new Unit(2);
    Unit u3 = new Unit(1);

    Unit u4 = new Unit(4);
    Unit u5 = new Unit(5);
    Unit u6 = new Unit(1);
    Unit u7 = new Unit(3);

    ArrayList<Unit> Action_units = new ArrayList<>();
    Action_units.add(u1);
    Action_units.add(u2);
    Action_units.add(u3);
    Collections.sort(Action_units);

    ArrayList<Unit> to_units = new ArrayList<>();
    to_units.add(u4);
    to_units.add(u5);
    to_units.add(u6);
    to_units.add(u7);
    Collections.sort(to_units);

    to.setUnits(to_units);

    Map m = new Map();
    m.addTerritory(to);
    m.addTerritory(from);

    AttackAction a = new AttackAction(wen, to, from, 3, Action_units);

    Random r1 = new Random(1);
    Random r2 = new Random(2);
    Dice attackDice = new Dice(r1);
    Dice defendDice = new Dice(r2);

    a.execute(attackDice, defendDice);
    assertTrue(to.getOwner().equals(michael));

  }

  @Test
  public void test_mergeAction() {
    Player p1 = new Player("bob", 1, 10, 100, 10);
    Player p2 = new Player("sue", 2, 10, 100, 10);

    Territory t1 = new Territory(1, p1, "t1", 10, 1, 10, 10);
    Territory t2 = new Territory(2, p2, "t2", 10, 1, 10, 10);
    Territory t3 = new Territory(1, p1, "t3", 10, 1, 10, 10);

    t1.addNeighbor(t2);
    t2.addNeighbor(t3);

    ArrayList<Unit> units1 = new ArrayList<>();
    ArrayList<Unit> units2 = new ArrayList<>();
    ArrayList<Unit> units3 = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      units1.add(new Unit(0));
      units2.add(new Unit(0));
      units3.add(new Unit(0));
    }

    AttackAction aa1 = new AttackAction(p1, t2, t1, units1);
    AttackAction aa2 = new AttackAction(p1, t2, t3, units3);

    aa1.check();
    aa2.check();

    aa1.mergeAction(aa2);

    assertEquals(aa1.getUnitsList().size(), 20);
    assertEquals(aa2.getUnitsList().size(), 0);
    assertEquals(t3.getNumQueueUnits(), 0);
    assertEquals(t1.getNumQueueUnits(), 20);
    assertEquals(t1.getNumUnits(), 0);
    assertEquals(t3.getNumUnits(), 0);


  }

  @Test
  public void test_weired() {
    /*
    Player michael = new Player("Michael", 0, 10, 10, 10);
    Player wen = new Player("WEN", 1, 10, 10,10);
    Territory to = new Territory(michael, "Durham",4, 5, 5, 5);
    //Territory from = new Territory(wen, "China",5, 5, 5, 5);
    Territory from = new Territory(wen, "China",3, 5, 5, 5);
    
    Unit u1 = new Unit(3);
    Unit u2 = new Unit(2);
    Unit u3 = new Unit(1);

    Unit u4 = new Unit(4);
    Unit u5 = new Unit(5);
    Unit u6 = new Unit(1);
    Unit u7 = new Unit(3);

    Unit u11 = new Unit(3);
    Unit u12 = new Unit(2);
    Unit u13 = new Unit(1);

    Unit u14 = new Unit(4);
    Unit u15 = new Unit(5);
    Unit u16 = new Unit(1);
    Unit u17 = new Unit(3);
    ArrayList<Unit> Action_units = new ArrayList<>();
    Action_units.add(u1);
    Action_units.add(u2);
    Action_units.add(u3);
    Collections.sort(Action_units);

    ArrayList<Unit> from_units = new ArrayList<>();
    from_units.add(u11);
    from_units.add(u12);
    from_units.add(u13);
    Collections.sort(from_units);

    from.setUnits(from_units);

    ArrayList<Unit> to_units = new ArrayList<>();
    to_units.add(u4);
    to_units.add(u5);
    to_units.add(u6);
    to_units.add(u7);
    Collections.sort(to_units);

    to.setUnits(to_units);

    ArrayList<Unit> a1_units = new ArrayList<>();
    a1_units.add(u14);
    a1_units.add(u15);
    a1_units.add(u16);
    a1_units.add(u17);
    Collections.sort(a1_units);

    AttackAction a = new AttackAction(wen, to, from, 3, Action_units);
    AttackAction a1 = new AttackAction(michael, from, to, 4, a1_units);
    
    Random r1 = new Random(1);
    Random r2 = new Random(2);
    Dice attackDice = new Dice(r1);
    Dice defendDice = new Dice(r2);
    assertNotNull(a.check());
    assertNotNull(a1.check());
    System.out.print("to territory's numUnits: ");
    System.out.print(to.getNumUnits());

    //assertEquals(a.check(),"s");
    //assertEquals(wen.getQueuedFoodResources(), 3);
    a.execute(attackDice, defendDice);
    a1.execute(attackDice, defendDice);
    assertEquals(to.getOwner().getName(), "WEN");
    //assertTrue(to.getOwner().equals(wen));
    assertTrue(from.getOwner().equals(michael));
    //assertEquals(wen.getQueuedFoodResources(), 0);
    */
  }

//  @Test
//  public void test_weiredMap() {
//    Player michael = new Player("Michael", 0, 10, 10, 10);
//    Player wen = new Player("WEN", 1, 10, 10,10);
//    Territory to = new Territory(michael, "Durham",4, 5, 5, 5);
//    //Territory from = new Territory(wen, "China",5, 5, 5, 5);
//    Territory from = new Territory(wen, "China",3, 5, 5, 5);
//
//    to.addNeighbor(from);
//    Unit u1 = new Unit(3);
//    Unit u2 = new Unit(2);
//    Unit u3 = new Unit(1);
//
//    Unit u4 = new Unit(4);
//    Unit u5 = new Unit(5);
//    Unit u6 = new Unit(1);
//    Unit u7 = new Unit(3);
//
//    Unit u11 = new Unit(3);
//    Unit u12 = new Unit(2);
//    Unit u13 = new Unit(1);
//
//    Unit u14 = new Unit(4);
//    Unit u15 = new Unit(5);
//    Unit u16 = new Unit(1);
//    Unit u17 = new Unit(3);
//    ArrayList<Unit> Action_units = new ArrayList<>();
//    Action_units.add(u1);
//    Action_units.add(u2);
//    Action_units.add(u3);
//    Collections.sort(Action_units);
//
//    ArrayList<Unit> from_units = new ArrayList<>();
//    from_units.add(u11);
//    from_units.add(u12);
//    from_units.add(u13);
//    Collections.sort(from_units);
//
//    from.setUnits(from_units);
//
//    ArrayList<Unit> to_units = new ArrayList<>();
//    to_units.add(u4);
//    to_units.add(u5);
//    to_units.add(u6);
//    to_units.add(u7);
//    Collections.sort(to_units);
//
//    to.setUnits(to_units);
//
//    ArrayList<Unit> a1_units = new ArrayList<>();
//    a1_units.add(u14);
//    a1_units.add(u15);
//    a1_units.add(u16);
//    a1_units.add(u17);
//    Collections.sort(a1_units);
//
//    Map m = new Map();
//    m.addTerritory(to);
//    m.addTerritory(from);
//
//    AttackAction a = new AttackAction(wen, to, from, 3, Action_units);
//    AttackAction a1 = new AttackAction(michael, from, to, 4, a1_units);
//
//    Random r1 = new Random(1);
//    Random r2 = new Random(2);
//    Dice attackDice = new Dice(r1);
//    Dice defendDice = new Dice(r2);
//
//    assertNull(a.check(m));
//    assertNull(a1.check(m));
//    //System.out.print(a.check(m));
//    //System.out.print(a1.check(m));
//    System.out.print(to.getNumUnits());
//
//    System.out.print("to territory's numUnits: ");
//    System.out.print(to.getNumUnits());
//
//    //assertEquals(a.check(),"s");
//    //assertEquals(wen.getQueuedFoodResources(), 3);
//    //a.execute(attackDice, defendDice);
//    //a1.execute(attackDice, defendDice);
//    a.execute(m);
//    a1.execute(m);
//    assertEquals(to.getOwner().getName(), "WEN");
//    assertTrue(to.getOwner().equals(wen));
//    assertTrue(from.getOwner().equals(michael));
//    assertEquals(from.getOwner().getName(), "Michael");
//    assertEquals(from.getNumUnits(),4);
//    assertEquals(to.getNumUnits(),3);
//
//    AttackAction b = new AttackAction(wen, from, to, 3, Action_units);
//    AttackAction b1 = new AttackAction(michael, to, from, 4, a1_units);
//
//    assertNull(b1.check(m));
//    assertNull(b.check(m));
//    //assertNull(b1.check(m));
//    b1.execute(m);
//    b.execute(m);
//    //b1.execute(m);
//    //assertEquals(to.getOwner().getName(), "WEN");
//    assertTrue(to.getOwner().equals(michael));
//    assertTrue(from.getOwner().equals(wen));
//    //assertEquals(from.getOwner().getName(), "Michael");
//    assertEquals(from.getNumUnits(),3);
//    assertEquals(to.getNumUnits(),4);
//
//
//    //assertEquals(wen.getQueuedFoodResources(), 0);
//  }

  

  
}












