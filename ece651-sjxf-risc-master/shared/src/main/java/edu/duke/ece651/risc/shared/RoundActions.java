package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * The class works as shared buffer between serverRunnable, game thread (server main thread)
 * Apply Producer-Consumer idea
 */
public class RoundActions implements Serializable {
    private List<Action> actions;
    private volatile boolean available;
    private volatile boolean infoUpdated; // indicate each player has already updated their information

    /**
     * Constructor Rouund Action
     */
    public RoundActions() {
        this.actions = new ArrayList<>();
        this.available = false;
        this.infoUpdated = false;
    }

    /**
     * get the round actions
     */
    public synchronized Iterable<Action> getActions() {
        while (!available) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.available = false;
        this.notify();
        return actions;
    }

    /**
     * set the round actions
     * @param actionsToAdd the actions ready to send
     */
    public synchronized void setActions(List<Action> actionsToAdd) {
        while (available) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.actions = actionsToAdd;
        this.available = true;
        this.notify();
    }

    public boolean getAvailable () {
        return this.available;
    }

    public synchronized boolean getInfoUpdated() {
        return this.infoUpdated;
    }

    public synchronized void setInfoUpdated(boolean infoUpdated) {
        this.infoUpdated = infoUpdated;
    }
}
