package edu.duke.ece651.risc.shared;

public class EnoughSpiesChecker extends TwoTerritoryActionChecker{

  public EnoughSpiesChecker(TwoTerritoryActionChecker next) {
    super(next);
  }

  /**
   *Checks that the from territory has enough spies of action owner for the spy action
   *@param action is the action to check
   *@return String error message or null
   */
	@Override
	protected String checkMyRule(TwoTerritoryAction action) {
		if (action.getFrom().getSpiesOf(action.getOwner()) < action.getNumUnits()) {
      return "Invalid action from " + action.getFrom().getTerritoryName() + " to " + action.getTo().getTerritoryName() + ". " +
        action.getFrom().getTerritoryName() + " only has " + action.getFrom().getSpiesOf(action.getOwner()) + " spies of " + action.getOwner().getName() + " left to move.\n";
    }
		return null;
	}


}
