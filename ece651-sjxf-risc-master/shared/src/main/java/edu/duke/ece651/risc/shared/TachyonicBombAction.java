package edu.duke.ece651.risc.shared;

import java.io.Serializable;

/**
 * Destroys all units and spies on targeted territory
 * makes territory impassable for 2 turns
 */
public class TachyonicBombAction implements Action, Serializable {
  public static final int TECH_COST = 300;
  public static final int FOOD_COST = 300;
  
  public static final int REQUIRED_LEVEL = 4;

  private Player owner;
  private Territory to;

  /**
   * Constructor for tachyonic bomb
   * @param owner is the owner of the action
   * @param to is the target territory
   */
  public TachyonicBombAction(Player owner, Territory to){
    this.owner = owner;
    this.to = to;
  }

  /**
   * executes the action
   */
  public void execute(){
    owner.dequeueFoodresources(FOOD_COST);
    owner.dequeueTechResource(TECH_COST);
    to.clearUnits();
    to.setBomb(true);
  }

  /**
   * checks that the action is valid
   * @return error message if invalid
   */
  public String check(){
    if(this.owner.getMaxTechLevel() < REQUIRED_LEVEL){
      return "Invalid TachyonicBomb, your max tech level hasn't reach level 4!";
    }
    if(this.owner.getTechResources() < TECH_COST || this.owner.getFoodResources() < FOOD_COST){
      return "Invalid TachyonicBomb, you don't have enough resource to use this weapon!";
    }
    owner.subtractFoodResources(FOOD_COST);
    owner.queueFoodresources(FOOD_COST);
    owner.subtractTechResource(TECH_COST);
    owner.queueTechResource(TECH_COST);
    return null;
  }
  
  public String check(Map map){
    this.to = map.getTerritoryByName(to.getName());
    return check();
  }

  public String getType(){
    return "TachyonicBombAction";
  }

  public Territory getTo() {
    return to;
  }


  
  
  
}

