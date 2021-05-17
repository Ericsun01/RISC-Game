package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BonusTest {
  @Test
  public void test_getBonusByLevel() {
    Bonus b = new Bonus();
    assertEquals(b.getBonusByLevel(0), 0);
    assertEquals(b.getBonusByLevel(1), 1);
    assertEquals(b.getBonusByLevel(2), 3);
    assertEquals(b.getBonusByLevel(3), 5);
    assertEquals(b.getBonusByLevel(4), 8);
    assertEquals(b.getBonusByLevel(5), 11);
    assertEquals(b.getBonusByLevel(6), 15);
  }

}
