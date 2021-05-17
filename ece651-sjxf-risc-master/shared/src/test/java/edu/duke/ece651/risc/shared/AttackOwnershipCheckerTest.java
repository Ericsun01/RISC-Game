package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AttackOwnershipCheckerTest {
  @Test
  public void test_myRule() {
    Player p1 = new Player("Bob");
    Player p2 = new Player("Michael");
    
    Territory t1 = new Territory(p1, "t1", 3, 2,2,2);
    Territory t2 = new Territory(p1, "t2", 4,2,2,2);
    Territory t3 = new Territory(p2, "t3", 4,2,2,2);
    Territory t4 = new Territory(p2, "t4",4,2,2,2);

    AttackAction m1 = new AttackAction(p1, t3, t1, 0);
    AttackAction m2 = new AttackAction(p2, t1, t3, 0);
    AttackAction m3 = new AttackAction(p1, t1, t1, 0);
    AttackAction m4 = new AttackAction(p2, t4, t3, 0);
    AttackAction m5 = new AttackAction(p1, t3, t4, 0);
    AttackAction m6 = new AttackAction(p1, t2, t3, 0);
    
    AttackOwnershipChecker aoc = new AttackOwnershipChecker(null);

    assertNull(aoc.checkMyRule(m1));
    assertNull(aoc.checkMyRule(m2));
    assertNotNull(aoc.checkMyRule(m3));
    assertNotNull(aoc.checkMyRule(m4));
    assertNotNull(aoc.checkMyRule(m5));
    assertNotNull(aoc.checkMyRule(m6));
  }

}










