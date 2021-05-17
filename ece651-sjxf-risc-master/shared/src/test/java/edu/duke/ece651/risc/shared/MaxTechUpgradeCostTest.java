package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MaxTechUpgradeCostTest {
  @Test
  public void test_map() {
    MaxTechUpgradeCost m = new MaxTechUpgradeCost();
    assertEquals(m.getCostByLevel(2),50);
    assertEquals(m.getCostByLevel(3),75);
    assertEquals(m.getCostByLevel(4),125);
    assertEquals(m.getCostByLevel(5),200);
    assertEquals(m.getCostByLevel(6),300);

    m.setMaxLevel(8);
    assertEquals(m.getMaxLevel(), 8);
  }

}
