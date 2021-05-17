package edu.duke.ece651.risc.shared;


import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TerritoryTest {
  @Test
  public void test_Territory() {
    Player michael = new Player("Michael",0,10,10,10);
    Territory t1 = new Territory(michael, "Durham",5, 5, 5, 5);
    Player wen = new Player("wen",1,10,10,10);

    ArrayList<Unit> list = new ArrayList<>();
    for (int i=0; i<10; i++) {
      list.add(new Unit(0));
    }

    t1.setUnits(list);
    t1.setOwner(wen);
    assertEquals(t1.getNumUnits(), 10);
    assertEquals(t1.getSize(), 5);
    assertEquals(t1.getFoodProduction(), 5);
    assertEquals(t1.getTechProduction(), 5);

    Territory t2 = new Territory(wen, "Kunshan",5, 5, 5, 5);


    t1.addNeighbor(t2);
    assertTrue(t2.hasNeighbor(t1));
    Iterable<Territory> neighbors = t1.getNeighbors();
    int count = 0;
    for (Territory t: neighbors) {
      count++;
    }
    assertEquals(count, 1);

    assertEquals(t1.getOwner().getName(),"wen");
    assertEquals(t1.getTerritoryName(), "Durham");

    assertTrue(t1.isNextTo(wen));
    assertFalse(t1.isNextTo(michael));

    assertFalse(t1.isHidden());
    t1.setHidden(true);
    assertTrue(t1.isHidden());

  }

  @Test
  public void test_modify() {
    Player michael = new Player("Michael", 0, 10, 10, 10);
    //Territory t1 = new Territory(michael, "Durham",5, 5, 5, 5);
    Territory t1 = new Territory(michael, "Durham",10, 5, 5, 5);
    Player wen = new Player("wen", 1,10,10,10);

    //ArrayList<Unit> list = new ArrayList<>();
    //for (int i=0; i<10; i++) {
    //list.add(new Unit(0));
    //}

    //t1.setUnits(list);
    t1.subtractUnit(0);
    assertEquals(t1.getNumUnits(), 9);

    Unit u1 = new Unit(3);
    Unit u2 = new Unit(2);
    Unit u3 = new Unit(1);

    for (int i=0; i<9; i++) {
      t1.subtractUnit(0);
    }
    t1.addUnit(u1);
    t1.addUnit(u2);
    t1.addUnit(u3);

    int i = 1;
    for(Unit u: t1.getUnits()){
      assertEquals(u.getLevel(), i);
      i++;
    }
    t1.subtractUnit(u2);
    i = 1;
    for(Unit u: t1.getUnits()){
      assertEquals(u.getLevel(), i);
      i+=2;
    }

    t1.queueUnit(u1);
    t1.queueUnit(u2);
    t1.dequeUnit(u1);
    i = 2;
    for(Unit u: t1.getQueuedUnits()){
      assertEquals(u.getLevel(), i);
    }

  }

  @Test
  public void test_spystuff() {
    Player p = new Player("bob", 1);
    ArrayList<Unit> units = new ArrayList<Unit>();
    Territory t = new Territory(0, p, "t", 1, 1, 1, units);
    assertEquals(t.getGroupID(), 0);

    t.addSpy(p);
    assertEquals(t.getNumSpies(), 1);
    ArrayList<Spy> spies = t.getSpies();
    assertEquals(spies.size(), 1 );

    t.subtractSpy(p);
    assertEquals(t.getNumSpies(), 0);

    Unit u = new Unit(1);
    t.addUnit(u);
    assertNotNull(t.tryGetUnitByLevel(1));
    assertNull(t.tryGetUnitByLevel(2));

    assertEquals(t.getNumUnitsByLevel(1), 1);

    t.promoteToSpy(u, p);
    assertEquals(t.getNumUnits(), 0);
    assertEquals(t.getNumSpies(), 1);

    assertEquals(t.getNumSpies(p), 1);
    
  }
}











