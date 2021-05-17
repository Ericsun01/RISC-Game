package edu.duke.ece651.risc.shared;

import java.io.Serializable;

public class CloakAction implements Action, Serializable {
    private Player owner;
    private Territory cloakArea;

    public CloakAction(Player owner, Territory cloakArea) {
        this.owner = owner;
        this.cloakArea = cloakArea;
    }

    /**
     *  Execute this action
     *  */
    public void execute() {
        owner.dequeueTechResource(20);
        cloakArea.setCloaked(true);
    }

    /**
     * Check the rules for this action
     * @return null if valid, error msg String if invalid
     **/

    public String check() {
        if (!owner.getCanCloak()) {
            return "Please upgrade Cloak Action first";
        }

        if (owner.getTechResources() < 20) {
            return "You have not enough tech resources for cloaking";
        }

        owner.subtractTechResource(20);
        owner.queueTechResource(20);
        return null;
    }

    public String check(Map map) {
        return check();
    }

    /**
     * Get the type of this action
     * @return String of this type representation
     */
    public String getType() {
        return "cloakAction";
    }

    public Territory getCloakArea() {
        return this.cloakArea;
    }

    public Player getOwner() {
        return this.owner;
    }
}
