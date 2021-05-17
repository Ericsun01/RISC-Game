package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UnitUpgradeActionTest {
  @Test
  public void test_check() {
    Player p1 = new Player("Michael");
    p1.setTechResources(1000);
    p1.setMaxTechLevel(6);
    Territory t1 = new Territory(p1, "t1", 0, 0, 0, 0);
    Unit u1 = new Unit(1);
    t1.addUnit(u1);
    UnitUpgradeAction a1 = new UnitUpgradeAction(p1, t1, 1, 2, 1);

    assertNull(a1.check(new Map()));
    //p1.setCanUpgradeMaxTech(false);
    assertEquals(p1.getTechResources(),992);
    assertEquals(p1.getQueuedTechResource(),8);
    assertEquals(a1.getType(), "UnitUpgradeAction");
  }

  @Test
  public void test_execute() {
   Player p1 = new Player("Michael");
    p1.setTechResources(1000);
    p1.setMaxTechLevel(6);
    Territory t1 = new Territory(p1, "t1", 0,0,0,0);
    Unit u1 = new Unit(1);
    t1.addUnit(u1);
    UnitUpgradeAction a1 = new UnitUpgradeAction(p1, t1, 1, 2, 1);

    assertNull(a1.check());
    a1.execute(new Map());
    assertEquals(u1.getLevel(),2);
    //p1.setCanUpgradeMaxTech(false);
    assertEquals(p1.getQueuedTechResource(),0);

    assertEquals(a1.getTerritory(), t1);
    assertNotNull(a1.getType());
  }
}
