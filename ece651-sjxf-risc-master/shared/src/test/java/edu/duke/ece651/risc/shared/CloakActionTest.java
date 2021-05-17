package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CloakActionTest {
    @Test
    public void test_check() {
        Player p1 = new Player("Bob", 1, 10, 100, 100);
        Player p2 = new Player("Eve", 2, 10, 100, 100);

        Map map = new Map();
        Territory t1 = new Territory(p1, "Mordor", 5, 5, 10, 10);
        Territory t2 = new Territory(p2, "Gondor", 3, 7, 15, 5);
        map.addTerritory(t1);
        map.addTerritory(t2);
        Game game = new Game(map);

        CloakAction ca1 = new CloakAction(p1, t1);
        CloakAction ca2 = new CloakAction(p2, t2);
        CloakUpgradeAction cua2 = new CloakUpgradeAction(p2);

        p2.setMaxTechLevel(3);
        cua2.check();
        cua2.execute();
        assertEquals(ca1.check(new Map()), "Please upgrade Cloak Action first");
        assertEquals(ca2.check(), "You have not enough tech resources for cloaking");
    }

    @Test
    public void test_execute() {
        Player p1 = new Player("Bob", 1, 10, 100, 120);
        Player p2 = new Player("Eve", 2, 10, 100, 150);

        Map map = new Map();
        Territory t1 = new Territory(p1, "Mordor", 5, 5, 10, 50);
        Territory t2 = new Territory(p2, "Gondor", 3, 7, 15, 50);
        map.addTerritory(t1);
        map.addTerritory(t2);
        Game game = new Game(map);

        p1.setMaxTechLevel(3);
        p2.setMaxTechLevel(3);
        CloakAction ca1 = new CloakAction(p1, t1);
        CloakAction ca2 = new CloakAction(p2, t2);
        CloakUpgradeAction cua1 = new CloakUpgradeAction(p1);
        CloakUpgradeAction cua2 = new CloakUpgradeAction(p2);

        cua1.check();
        cua1.execute();
        cua2.check();
        cua2.execute();

        ca1.check();
        ca1.execute();
        ca2.check();
        ca2.execute();

        assertTrue(t1.getCloaked());
        assertEquals(p2.getTechResources(), 30);

        assertEquals(ca1.getOwner(),p1);
        assertNotNull(ca1.getType());
    }
}
