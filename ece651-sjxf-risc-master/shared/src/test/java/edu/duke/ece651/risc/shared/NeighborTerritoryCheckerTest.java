package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NeighborTerritoryCheckerTest {
  @Test
  public void test_e() {
    

    Player p1 = new Player("Bob");
    
    Territory t1 = new Territory(p1, "t1", 3, 2,2,2);
    Territory t2 = new Territory(p1, "t2", 4,2,2,2);
    Territory t3 = new Territory(p1, "t3", 4,2,2,2);

    t1.addNeighbor(t2);

    AttackAction m1 = new AttackAction(p1, t1, t2, 0);
    AttackAction m2 = new AttackAction(p1, t2, t1, 0);
    AttackAction m3 = new AttackAction(p1, t1, t3, 0);
    
    NeighborTerritoryChecker ntc = new NeighborTerritoryChecker(null);
    assertNull(ntc.checkMyRule(m1));
    assertNull(ntc.checkMyRule(m2));
    assertNotNull(ntc.checkMyRule(m3));


  }

}
