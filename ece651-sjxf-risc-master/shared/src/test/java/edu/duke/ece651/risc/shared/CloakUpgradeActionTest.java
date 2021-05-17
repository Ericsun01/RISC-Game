package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class CloakUpgradeActionTest {
    @Test
    public void test_check() {
        Player p1 = new Player("Bob", 1, 10, 100, 50);

        CloakUpgradeAction cua1 = new CloakUpgradeAction(p1);
        assertEquals(cua1.check(), "You don't have tech level 3, please upgrade cloak action later");
        p1.setMaxTechLevel(3);
        assertEquals(cua1.check(), "You don't have enough tech resource, this upgrade will cost 100");
        p1.setTechResources(150);
        assertNull(cua1.check());
        assertEquals(p1.getTechResources(), 50);
        assertEquals(p1.getQueuedTechResource(), 100);
        assertFalse(p1.getCanCloak());

        assertNull(cua1.check(new Map()));
        assertEquals(cua1.getType(), "CloakUpgradeAction");
    }

    @Test
    public void test_execute() {
        Player p1 = new Player("Bob", 1, 10, 100, 50);

        CloakUpgradeAction cua1 = new CloakUpgradeAction(p1);
        p1.setMaxTechLevel(3);
        p1.setTechResources(150);
        cua1.check();
        cua1.execute(new Map());

        assertEquals(p1.getTechResources(), 50);
        assertEquals(p1.getQueuedTechResource(), 0);
        assertTrue(p1.getCanCloak());
        
    }
}
