package edu.duke.ece651.risc.shared;

public abstract class UnitUpgradeActionChecker {
    private UnitUpgradeActionChecker next;

    /**
     * The constructor to chain checkers
     *@param next is the next rule to check
     */
    public UnitUpgradeActionChecker(UnitUpgradeActionChecker next) {
        this.next = next;
    }

    /**
     *Checks the rule of the current implementing rule checker
     *@param action is the action to be checked
     */
    protected abstract String checkMyRule(UnitUpgradeAction action);

    /**
     * A recursion runs through the chain of rules
     *@param action is the action to be checked
     */
    public String checkInteraction(UnitUpgradeAction action) {
        if (checkMyRule(action) != null) {
            return checkMyRule(action);
        }
        if (next != null) {
            return next.checkInteraction(action);
        }
        return null;
    }
}
