package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class EnoughUnitsCheckerTest {
  @Test
  public void test_enoughUnits() {
    Player p1 = new Player("Bob");
    Player p2 = new Player("Michael");
    
    Territory t1 = new Territory(p1, "t1", 3, 2,2,2);
    Territory t2 = new Territory(p2, "t2", 4,2,2,2);

    AttackAction m1 = new AttackAction(p1, t1, t2, 3);
    AttackAction m2 = new AttackAction(p1, t1, t2, 5);
    
    EnoughUnitsChecker euc = new EnoughUnitsChecker(null);

    assertNull(euc.checkInteraction(m1));
    assertNotNull(euc.checkInteraction(m2));

  }

}
