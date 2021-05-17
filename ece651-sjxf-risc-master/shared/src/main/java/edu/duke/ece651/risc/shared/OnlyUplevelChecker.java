package edu.duke.ece651.risc.shared;

public class OnlyUplevelChecker extends UnitUpgradeActionChecker {
  /**
   *Constructor for chain of responsibility
   *@param next is the next rule in the chain
   */
  public OnlyUplevelChecker (UnitUpgradeActionChecker next) {
    super(next);
  }

  /**
   *Checks that an attack action is from the executing player to a different player
   *@param action is the action to be checked
   *@return error message or null
   */
	@Override
	protected String checkMyRule(UnitUpgradeAction action) {
  
    if(action.getFromLevel() >= action.getToLevel()){
      return "Unit can only be upgraded\n";
    }
		return null;
	}
}
