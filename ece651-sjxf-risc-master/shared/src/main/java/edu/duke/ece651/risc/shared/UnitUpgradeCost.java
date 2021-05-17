package edu.duke.ece651.risc.shared;

import java.util.HashMap;

public class UnitUpgradeCost {
  private HashMap<Integer, Integer> cost;
  private int maxLevel;

  /**
   * Constructor for UnitUpgradeCost
   */
  public UnitUpgradeCost(){
    this.cost = new HashMap<>();
    this.cost.put(0,0);
    this.cost.put(1,3);
    this.cost.put(2,11);
    this.cost.put(3,30);
    this.cost.put(4,55);
    this.cost.put(5,90);
    this.cost.put(6,140);
    this.maxLevel = 6;
  }

  /**
   * gets cost by level
   * @param level
   * @return int
   */
  public int getCostByLevel(int level){
    return cost.get(level);
  }

public int getMaxLevel() {
	return maxLevel;
}
}
