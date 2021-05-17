package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MaxTechEnoughTechResourcesCheckerTest {
  @Test
  public void test_check() {
    Player p1 = new Player("Michael");
    p1.setTechResources(1000);
    MaxTechUpgradeAction a1 = new MaxTechUpgradeAction(p1, 2);
    MaxTechUpgradeActionChecker c1 = new MaxTechEnoughTechResourcesChecker(null);

    assertNull(c1.checkInteraction(a1));
    p1.setTechResources(1);
    MaxTechUpgradeAction a2 = new MaxTechUpgradeAction(p1, 2);
    MaxTechUpgradeActionChecker c2 = new MaxTechEnoughTechResourcesChecker(null);

    assertNotNull(c2.checkInteraction(a2));

  }

}
