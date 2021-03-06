package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UnitUpgradeCostTest {
  @Test
  public void test_map() {
    UnitUpgradeCost m = new UnitUpgradeCost();
    assertEquals(m.getCostByLevel(0),0);
    assertEquals(m.getCostByLevel(1),3);
    assertEquals(m.getCostByLevel(2),11);
    assertEquals(m.getCostByLevel(3),30);
    assertEquals(m.getCostByLevel(4),55);
    assertEquals(m.getCostByLevel(5),90);
    assertEquals(m.getCostByLevel(6),140);
  }

}
