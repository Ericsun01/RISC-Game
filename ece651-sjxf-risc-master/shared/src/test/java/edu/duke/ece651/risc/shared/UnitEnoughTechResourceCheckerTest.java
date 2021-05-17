package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UnitEnoughTechResourceCheckerTest {
  @Test
  public void test_check() {
    Player p1 = new Player("Michael");
    p1.setTechResources(1000);
    Unit u1 = new Unit(0);
    Territory t1 = new Territory(p1, "t1", 0, 0, 0, 0);
    UnitUpgradeAction a1 = new UnitUpgradeAction(p1, t1, 0, 1, 1);
    UnitUpgradeActionChecker c1 = new UnitEnoughTechResourceChecker(null);

    assertNull(c1.checkInteraction(a1));

    p1.setTechResources(1);
    UnitUpgradeAction a2 = new UnitUpgradeAction(p1, t1, 0, 1, 1);
    UnitUpgradeActionChecker c2 = new UnitEnoughTechResourceChecker(null);

    assertNotNull(c2.checkInteraction(a2));
  }

}
