package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Territory implements Serializable {

  private int groupID;
  private Player owner;
  private final String name;
  private ArrayList<Territory> neighbors;
  private int size;
  private int foodProduction;
  private int techProduction;
  private ArrayList<Unit> units;
  private ArrayList<Unit> queuedUnits;
  private boolean cloaked;
  private ArrayList<Spy> spies;
  private ArrayList<Spy> queuedSpies;
  private boolean isHidden;
  private boolean bomb;

  /**
   * Constructor for a new territory
   *
   * @param owner          is the player who owns the territory
   * @param size           is the the size of the territory
   * @param foodProduction is the foodProduction of the territory
   * @param techProduction is the techProduction of the territory
   */
  public Territory(Player owner, String name, int numUnits, int size, int foodProduction, int techProduction) {
    this.groupID = 1;
    this.owner = owner;
    this.name = name;
    this.neighbors = new ArrayList<Territory>();
    this.size = size;
    this.foodProduction = foodProduction;
    this.techProduction = techProduction;
    this.units = new ArrayList<Unit>();
    for (int i=0; i<numUnits; i++) {
      this.units.add(new Unit(0));
    }
    this.queuedUnits = new ArrayList<Unit>();
    this.cloaked = false;
    this.spies = new ArrayList<>();
    this.queuedSpies = new ArrayList<>();
    this.isHidden = false;
    this.bomb = false;
  }

  public Territory(int groupID, Player owner, String name, int numUnits, int size, int foodProduction, int techProduction) {
    this(owner, name, numUnits, size, foodProduction, techProduction);
    this.groupID = groupID;
  }

  public Territory(Player owner, String name, int size, int foodProduction, int techProduction) {
    this(1, owner, name, 0, size, foodProduction, techProduction);
  }


  public Territory(int groupID, Player owner, String name, int size, int foodProduction, int techProduction) {
    this(groupID, owner, name, 0, size, foodProduction, techProduction);
  }

  public Territory(int groupID, Player owner, String name, int size, int foodProduction, int techProduction, ArrayList<Unit> units) {
    this(groupID, owner, name, size, foodProduction, techProduction);
    this.units = units;
  }

  public int getGroupID() {
    return this.groupID;
  }

  public String getName() {
    return this.name;
  }


  /**
   * Add a neighbor for the territory
   *
   * @param territory is the new neightbor
   */
  public void addNeighbor(Territory territory) {
    if(!this.hasNeighbor(territory)){
      neighbors.add(territory);
    }
    if (!territory.hasNeighbor(this)) {
      territory.addNeighbor(this);
    }
  }

  public String getTerritoryName() {
    return this.name;
  }

  /**
   * checks that the territory has the provided neighbor
   * @param territory is the territory to check for
   * @return true if neighbor, false otherwise
   */
  public boolean hasNeighbor(Territory territory) {
    return neighbors.contains(territory);
  }

  /**
   * add unit to territory
   * @param unit
   */
  public void addUnit(Unit unit) {
    units.add(unit);
    Collections.sort(units);
  }

  /**
   * remove unit from territory
   * @param unit
   */
  public void subtractUnit(Unit unit) {
    units.remove(unit);
    Collections.sort(units);
  }

  /**
   * remove unit from territory
   * @param level
   */
  public void subtractUnit(int level) {
    for (Unit u: units) {
      if (u.getLevel() == level) {
        units.remove(u);
        break;
      }
    }
    Collections.sort(units);
  }

  /**
   * remove unit from queue
   * @param level
   */
  public void dequeUnit(int level){
    for(Unit u: queuedUnits){
      if (u.getLevel() == level) {
        queuedUnits.remove(u);
        break;
      }
    }
    Collections.sort(units);
  }

  /**
   * Set a new owner for the territory
   *
   * @param player is the new owner's string
   */
  public void setOwner(Player player) {
    this.owner = player;
  }

  /**
   * return the owner of the territory
   */
  public Player getOwner() {
    return this.owner;
  }

  /**
   * @return numUnits of the terriroty
   */
  public int getNumUnits() {
    return units.size();
  }

  /**
   * @return numUnits in queue
   */
  public int getNumQueueUnits() {
    return queuedUnits.size();
  }

  /**
   * Gets an iterable version of the neighbors
   *
   * @return an Iterable of territories in the neighbor
   */
  public Iterable<Territory> getNeighbors() {
    return neighbors;
  }

  /**
   * @return size of the terriroty
   */
  public int getSize() {
    return size;
  }

  /**
   * @return foodProduction of the terriroty
   */
  public int getFoodProduction() {
    return foodProduction;
  }

  /**
   * @return techProduction of the terriroty
   */
  public int getTechProduction() {
    return techProduction;
  }

  /**
   * put a spy in queue
   * @param owner
   */
  public void queueSpy(Player owner) {
    queuedSpies.add(new Spy(owner));
  }

  /**
   * remove spy from queue
   * @param owner
   */
  public void dequeueSpy(Player owner) {
    Iterator<Spy> iter = queuedSpies.iterator();
    while (iter.hasNext()) {
      Spy s = iter.next();
      if (s.getOwner().getName().equals(owner.getName())) {
        iter.remove();
        break;
      }
    }
  }

  /**
   * clear all units and spies from territory
   */
  public void clearUnits(){
    units.clear();
    spies.clear();
  }

  /**
   * add a spy
   * @param owner
   */
  public void addSpy(Player owner) {
    spies.add(new Spy(owner));
  }

  /**
   * remove a spy
   * @param owner
   */
  public void subtractSpy(Player owner) {
    Iterator<Spy> iter = spies.iterator();
    while (iter.hasNext()) {
      Spy s = iter.next();
      if (s.getOwner().getName().equals(owner.getName())) {
        iter.remove();
        break;
      }
    }
  }

  /**
   * queue a unit
   * @param unit
   */
  public void queueUnit(Unit unit){
    queuedUnits.add(unit);
    Collections.sort(queuedUnits);
  }

  /**
   * queue a unit
   * @param level
   */
  public void queueUnit(int level){
    Unit u = new Unit(level);
    queuedUnits.add(u);
    Collections.sort(queuedUnits);
  }

  /**
   * get unit by level if it is there
   * @param level
   * @return
   */
  public Unit tryGetUnitByLevel(int level) {
    for (Unit u: units) {
      if (u.getLevel() == level) {
        return u;
      }
    }
    return null;
  }

  public int getNumUnitsByLevel (int level) {
    int numOfUnit = 0;
    for (Unit u: units) {
      if (u.getLevel() == level) {
        numOfUnit++;
      }
    }
    return numOfUnit;
  }

  public void dequeUnit(Unit unit){
    queuedUnits.remove(unit);
    Collections.sort(queuedUnits);
  }

  public void setCloaked (boolean isCloaked) {
    this.cloaked = isCloaked;
  }

  public boolean getCloaked () {
    return this.cloaked;
  }

  /**
   * @return total number of spies
   */
  public int getNumSpies() {
    return spies.size();
  }

  /**
   * 
   * @param owner is the owner of the spies
   * @return the number of spies of this specific owner
   */
  public int getSpiesOf(Player owner){
    int count = 0;
    for(Spy s : spies){
      if(s.getOwner().equals(owner)){
        count++;
      }
    }
    return count;
  }

  /**
   * @param p
   * @return number of spies for specified player
   */
  public int getNumSpies(Player p) {
    int counter = 0;
    for (Spy s: spies) {
      if (s.getOwner().getName().equals(p.getName())) {
        counter++;
      }
    }
    return counter;
  }

  public int getNumQueuedSpies() {
    return queuedSpies.size();
  }

  /**
   * promotes a unit to spy
   * @param unit
   * @param owner
   */
  public void promoteToSpy(Unit unit, Player owner) {
    subtractUnit(unit.getLevel());
    spies.add(new Spy(owner));
  }

  /**
   * checks that the territory is neighboring a territory of the specified player
   * @param p
   * @return
   */
  public boolean isNextTo(Player p) {
    for (Territory neighbor: neighbors) {
      if (neighbor.getOwner().getName().equals(p.getName())) {
        return true;
      }
    }
    return false;
  }

  public boolean isHidden() {
    return isHidden;
  }

  public void setHidden(boolean isHidden) {
    this.isHidden = isHidden;

  }

  public boolean isBomb() {
    return bomb;
  }
  
  public void setBomb(boolean bomb) {
    this.bomb = bomb;
  }

  public ArrayList<Unit> getUnits() {
    return units;
  }

  public ArrayList<Unit> getQueuedUnits() {
    return queuedUnits;
  }

  public ArrayList<Spy> getQueuedSpies(){
    return queuedSpies;
  }

  public void setUnits(ArrayList<Unit> units) {
    this.units = units;
  }

  public ArrayList<Spy> getSpies() {
    return spies;
  }

  public void setSpies(ArrayList<Spy> newSpies) {
    spies = newSpies;
  }

}
