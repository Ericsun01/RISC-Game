package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class V2MapFactoryTest {

  private boolean areNeighbors(Map m, String t1, String t2) {
    return m.getTerritoryByName(t1).hasNeighbor(m.getTerritoryByName(t2));
  } 

  @Test
  public void test_2player() {
    Player p1 = new Player("Bob");
    Player p2 = new Player("Sue");
    
    V2MapFactory f = new V2MapFactory();
    Map p2map = f.build2PlayerMap(p1, p2);

    assertTrue(areNeighbors(p2map, "t1", "t2"));
    assertTrue(areNeighbors(p2map, "t2", "t3"));
    assertTrue(areNeighbors(p2map, "t3", "t6"));
    assertTrue(areNeighbors(p2map, "t4", "t5"));
    assertTrue(areNeighbors(p2map, "t5", "t6"));
    assertTrue(areNeighbors(p2map, "t4", "t1"));
    assertTrue(areNeighbors(p2map, "t2", "t5"));
    assertFalse(areNeighbors(p2map, "t5", "t3"));

    assertEquals(p2map.getTerritoryByName("t1").getOwner(), p1);
    assertEquals(p2map.getTerritoryByName("t2").getOwner(), p1);
    assertEquals(p2map.getTerritoryByName("t3").getOwner(), p1);
    assertEquals(p2map.getTerritoryByName("t4").getOwner(), p2);
    assertEquals(p2map.getTerritoryByName("t5").getOwner(), p2);
    assertEquals(p2map.getTerritoryByName("t6").getOwner(), p2);

  }

  @Test
  public void test_3player() {
    Player p1 = new Player("Bob");
    Player p2 = new Player("Sue");
    Player p3 = new Player("Wen");
    
    V2MapFactory f = new V2MapFactory();
    Map p2map = f.build3PlayerMap(p1, p2, p3);

    assertTrue(areNeighbors(p2map, "t9", "t6"));
    assertTrue(areNeighbors(p2map, "t7", "t8"));
    assertTrue(areNeighbors(p2map, "t8", "t5"));
    assertTrue(areNeighbors(p2map, "t8", "t9"));
    assertTrue(areNeighbors(p2map, "t9", "t6"));
    assertFalse(areNeighbors(p2map, "t8", "t6"));

    assertEquals(p2map.getTerritoryByName("t1").getOwner(), p1);
    assertEquals(p2map.getTerritoryByName("t2").getOwner(), p1);
    assertEquals(p2map.getTerritoryByName("t3").getOwner(), p1);
    assertEquals(p2map.getTerritoryByName("t4").getOwner(), p2);
    assertEquals(p2map.getTerritoryByName("t5").getOwner(), p2);
    assertEquals(p2map.getTerritoryByName("t6").getOwner(), p2);
    assertEquals(p2map.getTerritoryByName("t7").getOwner(), p3);
    assertEquals(p2map.getTerritoryByName("t8").getOwner(), p3);
    assertEquals(p2map.getTerritoryByName("t9").getOwner(), p3);

  }

  @Test
  public void test_4player() {
    Player p1 = new Player("Bob");
    Player p2 = new Player("Sue");
    Player p3 = new Player("Wen");
    Player p4 = new Player("michael");
    
    V2MapFactory f = new V2MapFactory();
    Map p2map = f.build4PlayerMap(p1, p2, p3, p4);

    assertTrue(areNeighbors(p2map, "t7", "t10"));
    assertTrue(areNeighbors(p2map, "t8", "t11"));
    assertTrue(areNeighbors(p2map, "t9", "t12"));
    assertTrue(areNeighbors(p2map, "t10", "t11"));
    assertTrue(areNeighbors(p2map, "t11", "t12"));
    assertFalse(areNeighbors(p2map, "t4", "t12"));

    assertEquals(p2map.getTerritoryByName("t1").getOwner(), p1);
    assertEquals(p2map.getTerritoryByName("t2").getOwner(), p1);
    assertEquals(p2map.getTerritoryByName("t3").getOwner(), p1);
    assertEquals(p2map.getTerritoryByName("t4").getOwner(), p2);
    assertEquals(p2map.getTerritoryByName("t5").getOwner(), p2);
    assertEquals(p2map.getTerritoryByName("t6").getOwner(), p2);
    assertEquals(p2map.getTerritoryByName("t7").getOwner(), p3);
    assertEquals(p2map.getTerritoryByName("t8").getOwner(), p3);
    assertEquals(p2map.getTerritoryByName("t9").getOwner(), p3);
    assertEquals(p2map.getTerritoryByName("t10").getOwner(), p4);
    assertEquals(p2map.getTerritoryByName("t11").getOwner(), p4);
    assertEquals(p2map.getTerritoryByName("t12").getOwner(), p4);

  }

  @Test
  public void test_5player() {
    Player p1 = new Player("Bob");
    Player p2 = new Player("Sue");
    Player p3 = new Player("Wen");
    Player p4 = new Player("michael");
    Player p5 = new Player("Xue");
    
    V2MapFactory f = new V2MapFactory();
    Map p2map = f.build5PlayerMap(p1, p2, p3, p4,p5);

    assertTrue(areNeighbors(p2map, "t12", "t15"));
    assertTrue(areNeighbors(p2map, "t11", "t14"));
    assertTrue(areNeighbors(p2map, "t10", "t13"));
    assertTrue(areNeighbors(p2map, "t15", "t14"));
    assertTrue(areNeighbors(p2map, "t14", "t13"));
    assertFalse(areNeighbors(p2map, "t14", "t12"));

    assertEquals(p2map.getTerritoryByName("t1").getOwner(), p1);
    assertEquals(p2map.getTerritoryByName("t2").getOwner(), p1);
    assertEquals(p2map.getTerritoryByName("t3").getOwner(), p1);
    assertEquals(p2map.getTerritoryByName("t4").getOwner(), p2);
    assertEquals(p2map.getTerritoryByName("t5").getOwner(), p2);
    assertEquals(p2map.getTerritoryByName("t6").getOwner(), p2);
    assertEquals(p2map.getTerritoryByName("t7").getOwner(), p3);
    assertEquals(p2map.getTerritoryByName("t8").getOwner(), p3);
    assertEquals(p2map.getTerritoryByName("t9").getOwner(), p3);
    assertEquals(p2map.getTerritoryByName("t10").getOwner(), p4);
    assertEquals(p2map.getTerritoryByName("t11").getOwner(), p4);
    assertEquals(p2map.getTerritoryByName("t12").getOwner(), p4);
    assertEquals(p2map.getTerritoryByName("t13").getOwner(), p5);
    assertEquals(p2map.getTerritoryByName("t14").getOwner(), p5);
    assertEquals(p2map.getTerritoryByName("t15").getOwner(), p5);

  }

}








