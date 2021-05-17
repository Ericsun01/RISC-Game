package edu.duke.ece651.risc.shared;

public class MaxUpgradeLevelChecker extends MaxTechUpgradeActionChecker{
  /**
   *Constructor for chain of responsibility
   *@param next is the next rule in the chain
   */
  public MaxUpgradeLevelChecker (MaxTechUpgradeActionChecker next) {
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
    if(action.getToLevel() > m.getMaxLevel()){
      return action.getOwner().getName() + "s maxTechLevel is " + m.getMaxLevel()+"\n"; 
    }
		return null;
	}
}








