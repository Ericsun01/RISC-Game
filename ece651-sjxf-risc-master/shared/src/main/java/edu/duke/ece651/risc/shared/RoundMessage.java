package edu.duke.ece651.risc.shared;

import java.io.Serializable;
/**
 * The class works as shared buffer between serverRunnable, clientSocket thread and GUI thread
 * Apply Producer-Consumer idea
 */
public class RoundMessage {
    private RoundResult roundResult;
    private boolean available;

    /**
     * Constructor Rouund Message
     */
    public RoundMessage() {
        this.roundResult = null;
        this.available = false;
    }

    /**
     * get the round result
     */
    public synchronized RoundResult getRoundResult() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        available = false;
        notify();
        return roundResult;
    }

    /**
     * set the round result
     * @param roundResult the result ready to share
     */
    public synchronized void setRoundResult(RoundResult roundResult) {
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        available = true;
        this.roundResult = roundResult;
        notify();
    }

    public boolean getAvailable() {
        return available;
    }
    public void setAvailable(boolean available) { this.available = available; }
}
