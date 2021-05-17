package edu.duke.ece651.risc.shared;

import java.io.Serializable;
/**
 * The class works as shared buffer between clientSocket thread and GUI thread
 * Apply Producer-Consumer idea
 */
public class LoginMessage implements Serializable {
    private String userName;
    private String password;
    private volatile boolean available;

    /**
     * Constructor Login Message
     */
    public LoginMessage() {
        this.userName = null;
        this.password = null;
        this.available = false;
    }

    /**
     * get the action list
     */
    public synchronized String[] getInformation() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        available = false;
        String[] ans = new String[2];
        ans[0] = userName;
        ans[1] = password;
        notify();
        return ans;
    }

    /**
     * set the action list
     * @param userName the userName that ready to send to server
     * @param password the password that ready to send to server
     */
    public synchronized void setInformation(String userName, String password) {
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.available = true;
        this.userName = userName;
        this.password = password;
        notify();
    }

    public void setAvailable(boolean available) { this.available = available; }
}
