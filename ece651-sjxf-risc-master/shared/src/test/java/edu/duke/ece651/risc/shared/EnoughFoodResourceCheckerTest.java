package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class EnoughFoodResourceCheckerTest {
  @Test
  public void test_food() {
    Player p1 = new Player("Bob",2);
    Player p2 = new Player("Michael",2);
    
    Territory t1 = new Territory(p1, "t1", 3, 2,2,2);
    Territory t2 = new Territory(p2, "t2", 4,2,2,2);

    AttackAction m1 = new AttackAction(p1, t1, t2, 2);
    AttackAction m2 = new AttackAction(p2, t1, t2, 3);
    
    EnoughFoodResourceChecker euc = new EnoughFoodResourceChecker(null);

    assertNull(euc.checkInteraction(m1));
    assertNotNull(euc.checkInteraction(m2));
  }

}
