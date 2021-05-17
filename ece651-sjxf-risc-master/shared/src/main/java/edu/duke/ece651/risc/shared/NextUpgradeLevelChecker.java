package edu.duke.ece651.risc.shared;

public class NextUpgradeLevelChecker extends MaxTechUpgradeActionChecker {
  /**
   *Constructor for chain of responsibility
   *@param next is the next rule in the chain
   */
  public NextUpgradeLevelChecker (MaxTechUpgradeActionChecker next) {
    super(next);
  }

  /**
   *Checks that an attack action is from the executing player to a different player
   *@param action is the action to be checked
   *@return error message or null
   */
	@Override
	protected String checkMyRule(MaxTechUpgradeAction action) {
  
    if(action.getOwner().getMaxTechLevel()!= action.getToLevel()-1){
      return action.getOwner().getName() + "'s maxTech can only be upgraded one level for each turn";
    }
		return null;
	}
}
