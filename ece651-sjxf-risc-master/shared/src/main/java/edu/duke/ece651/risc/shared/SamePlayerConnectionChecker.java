package edu.duke.ece651.risc.shared;

import java.util.HashSet;
import java.util.LinkedList;

public class SamePlayerConnectionChecker extends TwoTerritoryActionChecker {

    public SamePlayerConnectionChecker(TwoTerritoryActionChecker next) {
        super(next);
    }

    /**
     *Uses breadth-first search to find whether there is an allied path between the two territories
     *@param action is the action to be checked
     *@return String error message or null
     */
    @Override
    protected String checkMyRule(TwoTerritoryAction action) {
        Territory from = action.getFrom();
        HashSet<Territory> checkedTerritories = new HashSet<Territory>();
        LinkedList<Territory> queue = new LinkedList<Territory>();
        checkedTerritories.add(from);
        queue.add(from);
        Territory curTerritory;
        while (queue.size() != 0) {
            curTerritory = queue.poll();
            checkedTerritories.add(curTerritory);
            //Go through neighbors for unvisited territories of the same owner
            for (Territory t: curTerritory.getNeighbors()) {
                if (!checkedTerritories.contains(t) && t.getOwner() == action.getOwner()) {
                    if (t == action.getTo()) {
                        return null;
                    }
                    else {
                        queue.add(t);
                    }
                }
            }
        }
        return "Invalid command from " + from.getName() + " to " + action.getTo().getName() + ". These territories are not connected by your own territories.";
    }

    public String checkMyRule(Territory to, Territory from) {
        //TODO: Change to move action (doesn't change logic, but less surprise)
        AttackAction aa = new AttackAction(from.getOwner(), to, from, 0);
        return checkMyRule(aa);
    }

}
