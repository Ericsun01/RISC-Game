package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;

public class MapTest {
  @Test
  public void Test_add_get_Territory() {
    Map m1 = new Map();

    Player michael = new Player("Michael",0,10,10,10);
    Territory t1 = new Territory(michael, "Durham",5, 5, 5, 5);
    Territory t2 = new Territory(michael, "Kunshan",7, 7, 7, 7);
    Territory t3 = new Territory(michael, "bobland", 0,0,0,0);

    m1.addTerritory(t1);
    m1.addTerritory(t2);
    m1.addTerritory(t3);
    t1.addNeighbor(t2);
    t2.addNeighbor(t3);
    t3.addNeighbor(t1);
    Iterator<Territory> iter = m1.getTerritories();
    int count = 0;

    for (Iterator<Territory> it = m1.getTerritories(); it.hasNext(); ) {
      Territory t = it.next();
      count++;
    }

    assertEquals(count, 3);

    Map m2 = new Map(m1);
    assertEquals(m2.getTerritoryByName("Durham").getName(), m1.getTerritoryByName("Durham").getName());
    assertEquals(m2.getNumTerritories(), m1.getNumTerritories());
    assertFalse(m1 == m2);
  }


}

