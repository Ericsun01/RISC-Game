package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SpyUpgradeActionTest {
  @Test
  public void test_check() {
    Player p1 = new Player("Bob", 1, 10, 100, 10);
    Unit u1 = new Unit(2);
    Territory t1 = new Territory(p1, "Durham",0, 5, 5, 5);
    t1.addUnit(u1);
    UpgradeAction s1 = new SpyUpgradeAction(p1, t1);
    assertEquals(s1.getType(),"SpyUpgradeAction");
    assertNotNull(s1.check(new Map()));
    p1.setTechResources(150);
    assertNull(s1.check());
    
    assertEquals(p1.getTechResources(), 130);
    assertEquals(p1.getQueuedTechResource(), 20);
    assertEquals(t1.getNumUnitsByLevel(2),0);
    assertEquals(t1.getNumQueuedSpies(), 1);
  }

    @Test
    public void test_execute() {
        Player p1 = new Player("Bob", 1, 10, 100, 150);
        Unit u1 = new Unit(2);
        Territory t1 = new Territory(p1, "Durham",0, 5, 5, 5);
        t1.addUnit(u1);
        SpyUpgradeAction s1 = new SpyUpgradeAction(p1, t1);
        //SpyUpgradeAction s1 = new SpyUpgradeAction(p1);
        p1.setMaxTechLevel(1);
        assertNull(s1.check());
        s1.execute(new Map());

        assertEquals(p1.getTechResources(), 130);
        assertEquals(p1.getQueuedTechResource(), 0);
        assertEquals(t1.getNumQueuedSpies(),0);
        assertEquals(t1.getNumSpies(), 1);
        s1.getTerritory();
    }
}
