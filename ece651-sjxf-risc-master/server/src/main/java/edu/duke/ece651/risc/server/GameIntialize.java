package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.Game;
import edu.duke.ece651.risc.shared.Map;
import edu.duke.ece651.risc.shared.Player;
import edu.duke.ece651.risc.shared.V2MapFactory;

import java.util.List;

/**
 * Initialize games that relates to different size of map
 */
public class GameIntialize {
    private V2MapFactory factory;

    /**
     * Constructor for GameInitializer
     */
    public GameIntialize() {
        this.factory = new V2MapFactory();
    }

    /**
     * Create Game based on different number of player
     * @param numPlayer indicates the size of the map we need
     */
    public Game createGame(int numPlayer) {
        switch (numPlayer) {
            case 2: return createGameOfTwo();
            case 3: return createGameOfThree();
            case 4: return createGameOfFour();
            case 5: return createGameOfFive();
        }
        return null;
    }


    /**
     * Create Game that has a map of 2
     */
    public Game createGameOfTwo() {
        Player player1 = new Player("Bob", 0, 10, 100, 10);
        Player player2 = new Player("Sue", 0, 10, 100, 10);

        Map map = factory.build2PlayerMap(player1, player2);
        Game game = new Game(map);
        return game;
    }

    /**
     * Create Game that has a map of 3
     */
    public Game createGameOfThree() {
        Player player1 = new Player("Bob", 0, 10, 100, 10);
        Player player2 = new Player("Sue", 0, 10, 100, 10);
        Player player3 = new Player("Andy", 0, 10, 100, 10);

        Map map = factory.build3PlayerMap(player1, player2, player3);
        Game game = new Game(map);
        return game;
    }

    /**
     * Create Game that has a map of 4
     */
    public Game createGameOfFour() {
        Player player1 = new Player("Bob", 0, 10, 100, 10);
        Player player2 = new Player("Sue", 0, 10, 100, 10);
        Player player3 = new Player("Andy", 0, 10, 100, 10);
        Player player4 = new Player("Eve", 0, 10, 100, 10);

        Map map = factory.build4PlayerMap(player1, player2, player3, player4);
        Game game = new Game(map);
        return game;
    }

    /**
     * Create Game that has a map of 5
     */
    public Game createGameOfFive() {
        Player player1 = new Player("Bob", 0, 10, 100, 10);
        Player player2 = new Player("Sue", 0, 10, 100, 10);
        Player player3 = new Player("Andy", 0, 10, 100, 10);
        Player player4 = new Player("Eve", 0, 10, 100, 10);
        Player player5 = new Player("Jack", 0, 10, 100, 10);

        Map map = factory.build5PlayerMap(player1, player2, player3, player4, player5);
        Game game = new Game(map);
        return game;
    }
}
