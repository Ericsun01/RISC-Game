package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.Game;
import edu.duke.ece651.risc.shared.Player;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GameIntializeTest {
    @Test
    public void test_gameInitialze() {
        GameIntialize gameIntializer = new GameIntialize();

        Game gameOf2 = gameIntializer.createGame(2);
        Game gameOf3 = gameIntializer.createGame(3);
        Game gameOf4 = gameIntializer.createGame(4);
        Game gameOf5 = gameIntializer.createGame(5);

        assertEquals(gameOf2.getMap().getNumTerritories(), 6);
        assertEquals(gameOf3.getMap().getNumTerritories(), 9);
        assertEquals(gameOf4.getMap().getNumTerritories(), 12);
        assertEquals(gameOf5.getMap().getNumTerritories(), 15);

        assertNull(gameIntializer.createGame(6));
    }
}
