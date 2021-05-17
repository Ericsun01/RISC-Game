package edu.duke.ece651.risc.shared;

import java.io.Serializable;

public class CloakUpgradeAction extends UpgradeAction implements Serializable {

    /**
     * constructor for CloakUpgradeAction
     * @param owner is the owner of the action
     */
    public CloakUpgradeAction(Player owner) {
        super(owner, 1);
    }

    /**
     * executes this action
     * @param map
     */
    @Override
    public void execute(Map map) {
        execute();
    }

    /**
     * executes this action
     */
    public void execute() {
        owner.dequeueTechResource(100);
        owner.setCanCloak(true);
    }

    /**
     * checks that this action is valid
     * @return
     */
    @Override
    public String check() {
        String ans = null;
        if (owner.getMaxTechLevel() < 3) {
            ans = "You don't have tech level 3, please upgrade cloak action later";
            return ans;
        }

        if (owner.getTechResources() < 100) {
            ans = "You don't have enough tech resource, this upgrade will cost 100";
            return ans;
        }

        owner.subtractTechResource(100);
        owner.queueTechResource(100);
        return ans;
    }

    @Override
    public String check(Map map) {
        return null;
    }

    @Override
    public String getType() {
        // TODO Auto-generated method stub
        return "CloakUpgradeAction";
    }
}
