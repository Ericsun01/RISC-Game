package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class PlacementAction implements Action, Serializable {
    private Player owner;
    private ArrayList<Unit> units;
    private Territory where;

    /**
     * The constructor of placement action
     *
     * @param owner is the player of this
     */
    public PlacementAction(Player owner,Territory where, ArrayList<Unit> units){
        this.owner = owner;
        this.where = where;
        this.units = units;
    }

    public PlacementAction(Player owner, Territory where, int numOfUnits) {
        this.owner = owner;
        this.where = where;
        this.units = new ArrayList<>();
        for (int i=0; i<numOfUnits; i++) {
            units.add(new Unit(0));
        }
    }

    /**
     * checks that the action is valid
     * @return error message if invalid
     */
    @Override
    public String check(){
        String error = null;
        if (!this.owner.getName().equals(where.getOwner().getName())){
            error = "You can only place units on your own territory!";
        }
        if (units.size() > owner.getUnitsToPlace()) {
            error = "Too many units to place!";
        }
        if (error != null) {
            owner.addUnitsToPlace(owner.getQueuedUnitsToPlace());
            owner.setQueuedUnitsToPlace(0);
            return error;
        }

        //Queue units for execution
        owner.addQueuedUnitsToPlace(units.size());
        owner.subtractUnitsToPlace(units.size());

        return null;
    }

    @Override
    public String check(Map map){
        return null;
    }

    /**
     * executes the action
     */
    @Override
    public void execute(){
        for (Unit u: units) {
            //map.getTerritoryByName(where.getTerritoryName()).addUnit(u);
            //where.addUnit(u);
            where.addUnit(new Unit(0));
        }
    }

    @Override
    public String getType() {
        return "PlacementAction";
    }

    public int getNumUnits() {
        return units.size();
    }

    public Territory getWhere() {return where;}

    public Player getPlayer() {return owner;}

    public void setOwner(Player player) {
        this.owner = player;
    }

}
