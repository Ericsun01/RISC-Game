package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NextUpgradeLevelCheckerTest {
  @Test
  public void test_check() {
    Player p1 = new Player("Michael");

    MaxTechUpgradeAction a1 = new MaxTechUpgradeAction(p1, 2);

    NextUpgradeLevelChecker c1 = new NextUpgradeLevelChecker(null);
    assertNull(c1.checkMyRule(a1));
    //p1.setCanUpgradeMaxTech(false);
    MaxTechUpgradeAction a2 = new MaxTechUpgradeAction(p1, 4);

    
    assertNotNull(c1.checkMyRule(a2));
  }

}
