package edu.duke.ece651.risc.shared;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *The class contains funtions that handle each round of games for AI
 */
public class ComputerPlayer {
    private Map gameMap;
    private Game game;
    private Player player;
    private int groupID;
    private ComputerAction actionGenerator;

    /**
     * Constructor TPServer
     * @param gameMap the map that this game
     * @param game is the game used for playing
     * @param player is the player object that holds status
     * @param groupID is the groupID for this computerPlayer
     */
    public ComputerPlayer(Map gameMap, Game game, Player player, int groupID) {
        this.gameMap = gameMap;
        this.game = game;
        this.player = player;
        this.groupID = groupID;
        this.actionGenerator = new ComputerAction(player);
    }

    /**
     * Constructor TPServer
     * @param gameMap the map that this game
     * @param game is the game used for playing
     * @param player is the player object that holds status
     * @param groupID is the groupID for this computerPlayer
     * @param seed is for Random
     */
    // For Testing
    public ComputerPlayer(Map gameMap, Game game, Player player, int groupID, int seed) {
        this.gameMap = gameMap;
        this.game = game;
        this.player = player;
        this.groupID = groupID;
        this.actionGenerator = new ComputerAction(seed, player);
    }

    /**
     *The function set GroupID for computer player, also set the territories' information
     */
    public void computerInitialPhase() {
        player.setPlayerNumber(groupID);
        game.setTerritoryOwner(groupID, player);
    }

    /**
     *The function generate placement actions for AI, also set them inside common buffers (roundAction)
     */
    public void computerPlacementPhase() {
        List<Action> placements = actionGenerator.createPlacementActions(10, gameMap, 3);
        for (Action a: placements) {
            a.check();
        }
        this.player.getRoundActions().setActions(placements);
        this.player.getRoundActions().setInfoUpdated(false);
    }

    /**
     *The function generate round actions for AI, also set them inside common buffers (roundAction)
     */
    public void computerRoundActionsPhase() {
        List<Action> actionList = actionGenerator.createRoundActions(gameMap);
        for (Action a: actionList) {
            System.out.println("ComputerPlayer: AI "+player.getName()+" has an action: "+a.getType());
        }
        this.player.getRoundActions().setActions(actionList);
        this.player.getRoundActions().setInfoUpdated(false);
    }

    public Player getPlayer() {
        return this.player;
    }

    /**
     *The function checks does the computer player lose the game
     */
    public boolean isLose() {
        Iterator<Territory> iterator = gameMap.getTerritories();
        boolean ans = true;
        while (iterator.hasNext()) {
            Territory territory = iterator.next();
            ans = ans && !(territory.getOwner().getName().equals(player.getName()));
        }
        return ans;
    }
}
