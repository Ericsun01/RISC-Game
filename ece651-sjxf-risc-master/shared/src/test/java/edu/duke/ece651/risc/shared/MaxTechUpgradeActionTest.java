package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MaxTechUpgradeActionTest {
  @Test
  public void test_check() {
    Player p1 = new Player("Michael");
    p1.setTechResources(1000);

    MaxTechUpgradeAction a1 = new MaxTechUpgradeAction(p1, 2);

    assertNull(a1.check(new Map()));
    //p1.setCanUpgradeMaxTech(false);
    assertEquals(p1.getTechResources(),950);
    assertEquals(p1.getQueuedTechResource(),50);
    assertEquals(a1.getType(), "MaxTechUpgradeAction");
  }

  @Test
  public void test_execute() {
    Player p1 = new Player("Michael");
    p1.setTechResources(1000);

    MaxTechUpgradeAction a1 = new MaxTechUpgradeAction(p1, 2);
    a1.check();
    a1.execute(new Map());
    //p1.setCanUpgradeMaxTech(false);
    assertEquals(p1.getMaxTechLevel(),2);
    assertEquals(p1.getQueuedTechResource(),0);
    assertTrue(p1.isCanUpgradeMaxTech());
  }

}










