package edu.duke.ece651.risc.shared;

public class EnoughFoodResourceChecker extends TwoTerritoryActionChecker{
  
  public EnoughFoodResourceChecker(TwoTerritoryActionChecker next) {
    super(next);
  }

  /**
   *Checks that the from territory has foodResource for the action
   *@param action is the action to check
   *@return String error message or null
   */
	@Override
	protected String checkMyRule(TwoTerritoryAction action) {
    if(action.getOwner().getFoodResources() < action.getNumUnits()){
      return "Invalid action from " + action.getFrom().getName() + " to " + action.getTo().getName() + ". " +
        action.getFrom().getName() + " only has " + action.getOwner().getFoodResources() + "foodResources left\n";
    }
    return null;
	}
}










