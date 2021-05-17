package edu.duke.ece651.risc.shared;

import java.io.Serializable;

/**
 * The class works as shared buffer between clientSocket thread and GUI thread
 * Apply Producer-Consumer idea
 */
public class MapMessage implements Serializable{
    private Map gameMap;
    private volatile boolean available;

    /**
     * Constructor Map Message
     */
    public MapMessage() {
        this.gameMap = null;
        this.available = false;
    }

    /**
     * get the game map
     */
    public synchronized Map getGameMap() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        available = false;
        notify();
        return gameMap;
    }

    /**
     * set the game map
     * @param gameMap the map that ready to send
     */
    public synchronized void setGameMap(Map gameMap) {
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        available = true;
        this.gameMap = gameMap;
        notify();
    }

    public boolean getAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {this.available = available;}
}

