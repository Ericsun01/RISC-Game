package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.List;
/**
 * The class works as shared buffer between clientSocket thread and GUI thread
 * Apply Producer-Consumer idea
 */
public class ActionMessage implements Serializable{
    private List<Action> actionList;
    private volatile boolean available;

    /**
     * Constructor Action Message
     */
    public ActionMessage() {
        this.actionList = null;
        this.available = false;
    }

    /**
     * get the action list
     */
    public synchronized List<Action> getActionList() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        available = false;
        notify();
        return actionList;
    }

    /**
     * set the action list
     * @param actionList the actions that ready for use
     */
    public synchronized void setActionList(List<Action> actionList) {
        //TODO: set up breakpoint here
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        available = true;
        this.actionList = actionList;
        notify();
    }

    public void setAvailable(boolean available) { this.available = available; }
}

