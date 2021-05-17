package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MaxUpgradeLevelCheckerTest {
  @Test
  public void test_check() {
    Player p1 = new Player("Michael");

    MaxTechUpgradeAction a1 = new MaxTechUpgradeAction(p1, 8);

    
    MaxUpgradeLevelChecker c1 = new MaxUpgradeLevelChecker(null);
    assertNotNull(c1.checkMyRule(a1));

    MaxTechUpgradeAction a2 = new MaxTechUpgradeAction(p1, 4);

    assertNull(c1.checkMyRule(a2));

    
  }

}
