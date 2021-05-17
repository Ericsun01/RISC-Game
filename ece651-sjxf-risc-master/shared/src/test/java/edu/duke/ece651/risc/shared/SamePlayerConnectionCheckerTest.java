package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class SamePlayerConnectionCheckerTest {
    @Test
    public void test_connectedTerritories() {

        Player p1 = new Player("Bob");
        Player p2 = new Player("Sue");

        Territory t1 = new Territory(p1, "t1", 0, 0, 0, 0);
        Territory t2 = new Territory(p2, "t2", 0, 0, 0, 0);
        Territory t3 = new Territory(p1, "t3", 0, 0, 0, 0);
        Territory t4 = new Territory(p1, "t4", 0, 0, 0, 0);
        Territory t5 = new Territory(p1, "t5", 0, 0, 0, 0);

        t1.addNeighbor(t2);
        t1.addNeighbor(t4);
        t2.addNeighbor(t3);
        t4.addNeighbor(t5);

        MoveAction m1 = new MoveAction(p1, t1, t4, new ArrayList<Unit>(), new Map());
        MoveAction m2 = new MoveAction(p1, t1, t5, new ArrayList<Unit>(), new Map());
        MoveAction m3 = new MoveAction(p1, t1, t3, new ArrayList<Unit>(), new Map());
        MoveAction m4 = new MoveAction(p1, t5, t3, new ArrayList<Unit>(), new Map());
        MoveAction m5 = new MoveAction(p1, t5, t1, new ArrayList<Unit>(), new Map());

        SamePlayerConnectionChecker spcc = new SamePlayerConnectionChecker(null);
        assertNull(spcc.checkMyRule(m1));
        assertNull(spcc.checkMyRule(m2));
        assertNotNull(spcc.checkMyRule(m3));
        assertNotNull(spcc.checkMyRule(m4));
        assertNull(spcc.checkMyRule(m5));

    }

}
