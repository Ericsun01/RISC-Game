package edu.duke.ece651.risc.shared;

public class MoveOwnershipChecker extends TwoTerritoryActionChecker {

    /**
     *Constructor for chaining rules
     *@param next is the next rule in the chain
     */
    public MoveOwnershipChecker(TwoTerritoryActionChecker next) {
      super(next);
    }

    /**
     *Checks that a move command is between two territories of the executing player
     *@param action is the action to be checked
     *@return error message or null
     */
    @Override
    protected String checkMyRule(TwoTerritoryAction action) {
        if (action.getTo().getOwner() != action.getOwner() || action.getFrom().getOwner() != action.getOwner()) {
            return "Provided move from " + action.getFrom().getTerritoryName() + " to " + action.getTo().getTerritoryName()+ " is invalid. One or both of those territories is not owned by you.";
        }
        return null;
    }

}

