package edu.duke.ece651.risc.shared;

public class AttackOwnershipChecker extends TwoTerritoryActionChecker {

  /**
   *Constructor for chain of responsibility
   *@param next is the next rule in the chain
   */
  public AttackOwnershipChecker(TwoTerritoryActionChecker next) {
    super(next);
  }

  /**
   *Checks that an attack action is from the executing player to a different player
   *@param action is the action to be checked
   *@return error message or null
   */
	@Override
	protected String checkMyRule(TwoTerritoryAction action) {
		if (action.getTo().getOwner() == action.getOwner() || action.getFrom().getOwner() != action.getOwner()) {
      return "Provided attack action from " + action.getFrom().getName() + " to " + action.getTo().getName() + " is invalid. The source territory must be owned by you, and the destination territory must be owned by an enemy.\n";
    }
		return null;
	}

}









