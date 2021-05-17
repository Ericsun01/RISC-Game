package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PlayerTest {
    @Test
    public void test_setName_getName() {
        Player player = new Player("Alpha",0,10,10,10);
        assertEquals("Alpha",player.getName());
        player.setName("Beta");
        assertEquals("Beta", player.getName());
    }


    @Test
    public void test_miscellaneous() {
        Player p1 = new Player("Bob", 1, 2, 0, 0, false);
        assertFalse(p1.getIsAI());

        p1.decrementUnitsToPlace();
        assertEquals(p1.getUnitsToPlace(), 1);

        p1.setUnitsToPlace(3);
        assertEquals(p1.getUnitsToPlace(), 3);

        p1.subtractQueuedUnitsToPlace(4);
        assertEquals(p1.getQueuedUnitsToPlace(), -2);

        p1.setPassword("hi");
        assertEquals(p1.getPassword(), "hi");

        p1.setPlayerNumber(2);
        assertEquals(p1.getPlayerNumber(), 2);

        p1.getLoginMessage();
        p1.getRoundActions();
        p1.setQueuedFoodResources(10);
        assertEquals(p1.getQueuedFoodResources(), 10);
        p1.setQueuedTechResource(10);
        assertEquals(p1.getQueuedTechResource(), 10);

        p1.addTechResource(20);
        assertEquals(p1.getTechResources(), 20);

    }
}
