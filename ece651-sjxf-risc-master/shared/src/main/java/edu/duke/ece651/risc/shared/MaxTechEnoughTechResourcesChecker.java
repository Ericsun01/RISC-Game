package edu.duke.ece651.risc.shared;

public class MaxTechEnoughTechResourcesChecker extends MaxTechUpgradeActionChecker {

  /**
   *Constructor for chain of responsibility
   *@param next is the next rule in the chain
   */
  public MaxTechEnoughTechResourcesChecker (MaxTechUpgradeActionChecker next) {
    super(next);
  }

  /**
   *Checks that an attack action is from the executing player to a different player
   *@param action is the action to be checked
   *@return error message or null
   */
	@Override
	protected String checkMyRule(MaxTechUpgradeAction action) {
    MaxTechUpgradeCost m = new MaxTechUpgradeCost();
    if(m.getCostByLevel(action.getToLevel()) > action.getOwner().getTechResources()){
      return "No enough foodResources to upgrade " + action.getOwner().getName() + "'s maxTechLevel.\n";
    }
		return null;
	}

}










