package edu.duke.ece651.risc.shared;

import java.util.HashMap;

public class MaxTechUpgradeCost {
  private HashMap<Integer, Integer> cost;
  private int maxLevel;

  /**
   * constructor for MaxTechUpgradeCost
   */
  public MaxTechUpgradeCost(){
    this.cost = new HashMap<>();
    this.cost.put(2,50);
    this.cost.put(3,75);
    this.cost.put(4,125);
    this.cost.put(5,200);
    this.cost.put(6,300);
    this.maxLevel = 6;
  }

  /**
   * gets the cost of max tech upgrade based on level
   * @param level the level to which we want to upgrade
   * @return cost
   */
  public int getCostByLevel(int level){
    return cost.get(level);
  }

public int getMaxLevel() {
	return maxLevel;
}

public void setMaxLevel(int maxLevel) {
	this.maxLevel = maxLevel;
}

}
