package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

public class UnitTest {
  @Test
  public void test_setLevel() {
    Unit u = new Unit(0);
    u.setLevel(1);
    assertEquals(u.getLevel(), 1);

    Unit mockUnit = mock(Unit.class);
    when(mockUnit.getLevel()).thenReturn(1);
    assertEquals(1, mockUnit.getLevel());
    verify(mockUnit).getLevel();
  }

  @Test
  public void test_compareTo() {
    Unit u1 = new Unit(0);
    Unit u2 = new Unit(1);
    Unit u3 = new Unit(2);
    Unit u4 = new Unit(2);

    assertTrue(u1.compareTo(u2)<0);
    assertTrue(u3.compareTo(u2)>0);
    assertTrue(u3.compareTo(u4)==0);
  }
}










