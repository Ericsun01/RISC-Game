package edu.duke.ece651.risc.shared;

public abstract class TwoTerritoryActionChecker {
    private TwoTerritoryActionChecker next;

    /**
     * The constructor to chain checkers 
     *@param next is the next rule to check
     */
    public TwoTerritoryActionChecker(TwoTerritoryActionChecker next) {
      this.next = next;
    }
  
    /**
     *Checks the rule of the current implementing rule checker 
     *@param action is the action to be checked
     */
    protected abstract String checkMyRule(TwoTerritoryAction action);
  
    /**
     * A recursion runs through the chain of rules
     *@param action is the action to be checked
     */
    public String checkInteraction(TwoTerritoryAction action) {
      if (checkMyRule(action) != null) {
        return checkMyRule(action);
      }
      if (next != null) {
        return next.checkInteraction(action);
      }
      return null;
    }
}
