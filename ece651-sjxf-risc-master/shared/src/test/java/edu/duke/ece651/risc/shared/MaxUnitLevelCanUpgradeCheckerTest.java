package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MaxUnitLevelCanUpgradeCheckerTest {
  @Test
  public void test_check() {
    Player p1 = new Player("Michael");
    Unit u1 = new Unit(0);
    Territory t1 = new Territory(p1, "t1", 0, 0, 0);
    p1.setMaxTechLevel(3);
    t1.addUnit(u1);
    UnitUpgradeAction a1 = new UnitUpgradeAction(p1, t1, 6, 8, 1);

    MaxUnitLevelCanUpgradeChecker c1 = new MaxUnitLevelCanUpgradeChecker(null);
    assertNotNull(c1.checkMyRule(a1));

  }

}


