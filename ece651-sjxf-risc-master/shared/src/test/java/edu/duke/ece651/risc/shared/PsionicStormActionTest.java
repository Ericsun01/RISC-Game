package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import jdk.jfr.Timestamp;

public class PsionicStormActionTest {

  public ArrayList<Territory> createMap(Player p1, Player p2, Player p3){
    // owner, name, numUnits, size, foodProduction, techProduction
    Territory t1 = new Territory(p1, "t1", 10, 4, 5, 30);
    Territory t2 = new Territory(p1, "t2", 10, 2, 5, 30);
    Territory t3 = new Territory(p1, "t3", 10, 5, 5, 30);
    Territory t4 = new Territory(p1, "t4", 10, 6, 5, 30);
    Territory t5 = new Territory(p2, "t5", 10, 4, 5, 20);
    Territory t6 = new Territory(p2, "t6", 5, 4, 5, 20);
    Territory t7 = new Territory(p3, "t7", 12, 4, 5, 30);
    Territory t8 = new Territory(p3, "t8", 5, 3, 5, 20);
    t1.addNeighbor(t2);
    t1.addNeighbor(t3);
    t2.addNeighbor(t3);
    t2.addNeighbor(t4);
    t3.addNeighbor(t4);
    t4.addNeighbor(t5);
    t5.addNeighbor(t6);
    t5.addNeighbor(t7);
    t5.addNeighbor(t8);
    ArrayList<Territory> tList = new ArrayList<>();
    tList.add(t1);
    tList.add(t2);
    tList.add(t3);
    tList.add(t4);
    tList.add(t5);
    tList.add(t6);
    tList.add(t7);
    tList.add(t8);
    t5.addSpy(p1); // for psa2, it's a spy of the owner of action, will not be eliminate
    t5.addSpy(p2); // will be eliminate
    return tList;
  }

  @Test
  public void test_getType() {
    Player bob = new Player("Bob", 10);
    Territory t1 = new Territory(bob, "t1", 10, 5, 10, 10);
    Territory t2 = new Territory(bob, "t2", 10, 6, 10, 10);

    PsionicStormAction psa = new PsionicStormAction(bob, t1);
    assertEquals(psa.getType(), "PsionicStorm");
    assertEquals(psa.getOwner(), bob);
    assertEquals(psa.getTo(), t1);
  }

  @Test
  public void test_check(){
    Player bob = new Player("Bob", 500, 500);
    Player alice = new Player("Alice", 100, 400);
    Player john = new Player("John", 500, 500);
    ArrayList<Territory> territories = createMap(bob, alice, john);
    PsionicStormAction psa1 = new PsionicStormAction(bob, territories.get(0));
    assertEquals(psa1.check(), "Invalid Psionic Storm, your max tech level hasn't reach level 4!");
    bob.setMaxTechLevel(4);
    PsionicStormAction psa2 = new PsionicStormAction(bob, territories.get(4));
    assertNull(psa2.check());
    alice.setMaxTechLevel(4);
    PsionicStormAction psa3 = new PsionicStormAction(alice, territories.get(4));
    assertEquals(psa3.check(), "Invalid Psionic Storm, you don't have enough resource to use this weapon!");
  }

  @Test
  public void test_execute(){
    Player bob = new Player("Bob", 500, 500);
    Player alice = new Player("Alice", 100, 400); // insufficient for a storm
    Player john = new Player("John", 500, 35); // 35-20 = 15, ok for t7, 15<20, only remove 15 for t8
    ArrayList<Territory> territories = createMap(bob, alice, john);
    Map map = new Map();
    for(Territory t : territories){
      map.addTerritory(t);
    }
    bob.setMaxTechLevel(4);
    PsionicStormAction psa2 = new PsionicStormAction(bob, territories.get(4));
    assertNull(psa2.check(map));
    assertEquals(psa2.getTargets().size(), 4);
    assertEquals(bob.getFoodResources(), 350);
    assertEquals(bob.getTechResources(), 50);
    psa2.execute();
    assertEquals(psa2.getOwner(), bob);
    assertEquals(territories.get(6).getNumUnits(), 2); //12-10=2
    assertEquals(territories.get(7).getNumUnits(), 0); //only has 5, so remove 5
  }


}
