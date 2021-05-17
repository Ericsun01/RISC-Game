package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SpyTest {
  @Test
  public void test_Spy() {
    Player p1 = new Player("wen", 0, 10, 10, 10);
    Player p2 = new Player("michael", 1, 10, 10, 10);
    Spy s1 = new Spy(p1);
    assertEquals(p1, s1.getOwner());
    s1.setOwner(p2);
    assertEquals(p2, s1.getOwner());
  }

  

}








