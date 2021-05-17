package edu.duke.ece651.risc.shared;

import java.io.Serializable;

/**
 * The class that record map, player status as round result
 */
public class RoundResult implements Serializable {
    private Map map;
    private Player player;

    /**
     * Constructor
     * @param map the map ready to share
     * @param player the player to share
     */
    public RoundResult (Map map, Player player) {
        this.map = map;
        this.player = player;
    }

    public Map getMap () {
        return this.map;
    }

    public Player getPlayer () {
        return this.player;
    }
}
