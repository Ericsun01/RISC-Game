package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Map implements Serializable {
    private ArrayList<Territory> territories;

    /**
     *Constructor for a new Map
     */
    public Map(){
        this.territories = new ArrayList<Territory>();
    }

    public Map(Map m) {
        this.territories = new ArrayList<>();

        for (Iterator<Territory> it = m.getTerritories(); it.hasNext();) {
            Territory t = it.next();

            Player tp = t.getOwner();
            Player p = new Player(tp.getName(), tp.getPlayerNumber(), tp.getUnitsToPlace(), tp.getFoodResources(), tp.getTechResources());

            this.territories.add(new Territory(t.getGroupID(), p, t.getName(),
                    t.getSize(), t.getFoodProduction(), t.getTechProduction(), t.getUnits()));
        }
        for (Iterator<Territory> it = m.getTerritories(); it.hasNext();) {
            Territory t = it.next();

            for (Territory neighbor : t.getNeighbors()) {
                Territory newNeighbor = this.getTerritoryByName(neighbor.getName());
                this.getTerritoryByName(t.getName()).addNeighbor(newNeighbor);
            }
        }

    }

    /**
     *Add a territory to the territories
     *@param territory is the new added territory
     */
    public void addTerritory(Territory territory){
        territories.add(territory);
    }

    /**
     *Gets an iterable version of the territories.
     *@return an Iterable of territories in the territories
     */
    public Iterator<Territory> getTerritories() {
        return territories.iterator();
    }

    /**
     * Gets a territory by name
     * @return a Territory
     */
    public Territory getTerritoryByName(String name) {
        for (Territory t: territories) {
            if (t.getTerritoryName().equals(name)) {
                return t;
            }
        }
        return null;
    }

    /**
     * @return the number of territories on the map
     */
    public int getNumTerritories() {
        return territories.size();
    }


}





