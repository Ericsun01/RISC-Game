package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class TachyonicBombActionTest {
  @Test
  public void test_check_and_execute() {
    Player p1 = new Player("Bob", 2, 10, 100, 10);
    Player p2 = new Player("wen", 2, 10, 100, 10);
    Territory t1 = new Territory(p1, "Durham",0, 5, 5, 5);
    Unit u1 = new Unit(2);
    t1.addUnit(u1);
    t1.addSpy(p2);
    
    TachyonicBombAction t = new TachyonicBombAction(p1, t1);
    assertNotNull(t.check());
    p1.setMaxTechLevel(4);
    assertNotNull(t.check());
    
    p1.setTechResources(300);
    assertNotNull(t.check());
    p1.setFoodResources(300);
    assertNull(t.check());
    
    assertEquals(p1.getTechResources(), 0);
    assertEquals(p1.getQueuedTechResource(), 300);
    assertEquals(p1.getFoodResources(),0);
    assertEquals(p1.getQueuedFoodResources(), 300);
    assertEquals(t1.getNumSpies(),1);
    assertEquals(t1.getNumUnits(),1);

    t.execute();
    assertEquals(p1.getQueuedTechResource(), 0);
    assertEquals(p1.getQueuedFoodResources(), 0);
    assertEquals(t1.isBomb(), true);
    assertEquals(t1.getNumUnits(), 0);
    assertEquals(t1.getNumSpies(), 0);
    assertEquals(t.getType(), "TachyonicBombAction");
    assertEquals(t.getTo(), t1);
    
  }

  @Test
  public void test_checkmap() {
    Player p1 = new Player("Bob", 2, 10, 300, 300);
    Player p2 = new Player("Eve", 2, 10, 300, 300);
    
    Map map = new Map();
    Territory t1 = new Territory(p1, "sd", 5, 5, 10, 50);
    Territory t2 = new Territory(p2, "fs", 3, 7, 15, 50);
    map.addTerritory(t1);
    map.addTerritory(t2);
    p1.setMaxTechLevel(4);
    p2.setMaxTechLevel(4);
    TachyonicBombAction t = new TachyonicBombAction(p1, t1);
    assertNull(t.check(map));
  }

}







