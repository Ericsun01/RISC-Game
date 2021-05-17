package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class OnlyUplevelCheckerTest {
  @Test
  public void test_check() {
    Player p1 = new Player("Michael");
    p1.setMaxTechLevel(5);
    Unit u1 = new Unit(3);
    Territory t1 = new Territory(p1, "t1", 0, 0, 0, 0);
    t1.addUnit(u1);
    UnitUpgradeAction a1 = new UnitUpgradeAction(p1, t1, 3, 2, 1);


    OnlyUplevelChecker c1 = new OnlyUplevelChecker(null);
    assertNotNull(c1.checkMyRule(a1));

    UnitUpgradeAction a2 = new UnitUpgradeAction(p1, t1, 3, 4, 1);
    assertNull(c1.checkMyRule(a2));
  }

}
