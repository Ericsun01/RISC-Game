package edu.duke.ece651.risc.shared;

public class EnoughUnitsChecker extends TwoTerritoryActionChecker {

  public EnoughUnitsChecker(TwoTerritoryActionChecker next) {
    super(next);
  }

  /**
   *Checks that the from territory has enough units for the action
   *@param action is the action to check
   *@return String error message or null
   */
	@Override
	protected String checkMyRule(TwoTerritoryAction action) {
		if (action.getFrom().getNumUnits() < action.getNumUnits()) {
      return "Invalid action from " + action.getFrom().getTerritoryName() + " to " + action.getTo().getTerritoryName() + ". " +
        action.getFrom().getTerritoryName() + " only has " + action.getFrom().getNumUnits() + " units left to move.\n";
    }
		return null;
	}

}












