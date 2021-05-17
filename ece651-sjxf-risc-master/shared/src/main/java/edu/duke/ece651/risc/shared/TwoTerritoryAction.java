package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class TwoTerritoryAction implements Action, Serializable {
  protected Player owner;
  protected Territory to;
  protected Territory from;
  protected int numUnits;
  protected ArrayList<Unit> units;
  // protected ArrayList<Spy> spies;
  
  /**
   * Constructor for abstract action happened between two territories, including
   * move and attack
   * 
   * @param player   is the owner of the action
   * @param to       is the destination territory
   * @param from     is the source territory
   * @param numUnits is the number of units involved in the action
   */
  public TwoTerritoryAction(Player player, Territory to, Territory from, int numUnits) {
    this.owner = player;
    this.to = to;
    this.from = from;
    this.numUnits = numUnits;
    this.units = new ArrayList<>();
    for (int i=0; i<numUnits; i++) {
      units.add(new Unit(0));
    }
  }

  /**
   * Constructor for abstract action happened between two territories, including
   * move and attack
   * 
   * @param player   is the owner of the action
   * @param to       is the destination territory
   * @param from     is the source territory
   * @param numUnits is the number of units involved in the action
   * @param units is the units in the action
   */
  public TwoTerritoryAction(Player player, Territory to, Territory from, int numUnits, ArrayList<Unit> units) {
    this.owner = player;
    this.to = to;
    this.from = from;
    this.numUnits = numUnits;
    this.units = units;
  }

  /**
   * Constructor for abstract spy move action happened between two territories
   * 
   * @param player   is the owner of the action
   * @param to       is the destination territory
   * @param from     is the source territory
   * @param spies is the spies in the action
   */
  public TwoTerritoryAction(Player player, Territory to, Territory from, ArrayList<Spy> spies) {
    this.owner = player;
    this.to = to;
    this.from = from;
    this.numUnits = spies.size();
    this.units = new ArrayList<>();
    for(Spy s : spies){
      this.units.add(s);
    }
//    for (int i=0; i<spies.size(); i++) {
//      units.add(new Spy(player));
//    }
  }

  /**
   * executes the action
   */
  abstract public void execute();

  abstract public void execute(Map map);

  /**
   * An old version: Checks if the action is valid
   */
  abstract public String check();


 // abstract public String check(Map map, Player player);

  /**
   * Checks if the action is valid
   */
  abstract public String check(Map map);


  /**
   * Gets the type of action, can be "MoveAction" or "AttackAction"
   */
  abstract public String getType();

  /**
   * @return the destination territory
   */
  public Territory getTo() {
    return to;
  }

  /**
   * @return the source territory
   */
  public Territory getFrom() {
    return from;
  }

  /**
   * @return numUnits involved in the action
   */
  public int getNumUnits() {
    return numUnits;
  }

  public void setTo(Territory to) {
    this.to = to;
  }
  
  public void setFrom(Territory from) {
    this.from = from;
  }

  public Player getOwner() {
    return owner;
  }

  public ArrayList<Unit> getUnitsList() {
    return units;
  }

}
