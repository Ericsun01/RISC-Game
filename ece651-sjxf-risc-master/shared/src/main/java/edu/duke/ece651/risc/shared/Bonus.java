package edu.duke.ece651.risc.shared;

import java.util.HashMap;

public class Bonus {
  private HashMap<Integer, Integer> bonus;

  /**
   *Constructor for a new bonus
   */
  public Bonus(){
    this.bonus = new HashMap<>();
    this.bonus.put(0, 0);
    this.bonus.put(1, 1);
    this.bonus.put(2, 3);
    this.bonus.put(3, 5);
    this.bonus.put(4, 8);
    this.bonus.put(5, 11);
    this.bonus.put(6, 15);
  }
  
  /**
   *@return the bonus corresponding to input level
   */
  public int getBonusByLevel(int level){
    return bonus.get(level);
  }
}
