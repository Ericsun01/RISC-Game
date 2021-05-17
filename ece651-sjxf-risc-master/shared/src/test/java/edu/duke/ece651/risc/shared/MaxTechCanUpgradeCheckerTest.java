package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MaxTechCanUpgradeCheckerTest {
  @Test
  public void test_check() {
    Player p1 = new Player("Michael");

    MaxTechUpgradeAction a1 = new MaxTechUpgradeAction(p1, 2);

    MaxTechCanUpgradeChecker c1 = new MaxTechCanUpgradeChecker(null);
    assertNull(c1.checkMyRule(a1));
    //p1.setCanUpgradeMaxTech(false);
    MaxTechUpgradeAction a2 = new MaxTechUpgradeAction(p1, 2);

    
    MaxTechCanUpgradeChecker c2 = new MaxTechCanUpgradeChecker(null);
    assertNotNull(c2.checkMyRule(a2));

  }
}










