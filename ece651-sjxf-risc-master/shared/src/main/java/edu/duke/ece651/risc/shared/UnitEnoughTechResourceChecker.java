package edu.duke.ece651.risc.shared;

public class UnitEnoughTechResourceChecker extends UnitUpgradeActionChecker {
  /**
   *Constructor for chain of responsibility
   *@param next is the next rule in the chain
   */
  public UnitEnoughTechResourceChecker (UnitUpgradeActionChecker next) {
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
    if ( ( m.getCostByLevel(action.getToLevel()) - m.getCostByLevel(action.getFromLevel()) ) * action.getNumUnits() > action.getOwner().getTechResources()){
      return "Not enough techResources to upgrade " + action.getOwner().getName() + "'s unit\n";
    }
		return null;
	}
}









