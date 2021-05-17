package edu.duke.ece651.risc.shared;

public class MaxUnitLevelCanUpgradeChecker extends UnitUpgradeActionChecker {
  /**
   *Constructor for chain of responsibility
   *@param next is the next rule in the chain
   */
  public MaxUnitLevelCanUpgradeChecker (UnitUpgradeActionChecker next) {
    super(next);
  }

  /**
   *Checks that an attack action is from the executing player to a different player
   *@param action is the action to be checked
   *@return error message or null
   */
	@Override
	protected String checkMyRule(UnitUpgradeAction action) {
    UnitUpgradeCost m = new UnitUpgradeCost();
    if(action.getToLevel() > m.getMaxLevel() || action.getToLevel() > action.getOwner().getMaxTechLevel()){
      return "Unit's maxTechLevel is " + m.getMaxLevel() + ". Your max tech level is " + action.getOwner().getMaxTechLevel() + ".\n";
    }
		return null;
	}
}










