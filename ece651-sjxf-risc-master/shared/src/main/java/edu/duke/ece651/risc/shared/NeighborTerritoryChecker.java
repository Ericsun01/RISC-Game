package edu.duke.ece651.risc.shared;

public class NeighborTerritoryChecker extends TwoTerritoryActionChecker {

  /**
   *Constructor for chaining rule checkers
   *@param next is the next rule to check
   */
  public NeighborTerritoryChecker(TwoTerritoryActionChecker next) {
    super(next);
  }

  /**
   *Checks that the two territories are neighbors
   *@param action is the action to be checked
   *@return error message or null
   */
	@Override
	protected String checkMyRule(TwoTerritoryAction action) {
		if (!action.getFrom().hasNeighbor(action.getTo())) {
        return "Attack order from " + action.getFrom().getName() + " to " + action.getTo().getName() + " is invalid. These territories do not neighbor eachother";
    }
		return null;
	}

}

