package edu.duke.ece651.risc.shared;


import java.io.Serializable;

public class Unit implements Comparable<Unit>, Serializable {
    public static final int MAX_LEVEL = 6;
    private int level;


    /**
     *Constructor for a new unit
     *@param level is the level of this unit
     */
    public Unit(int level){
     this.level = level;
    }

    /**
      *Set the level of this unit
      *@param level is the level to be set
      */
    public void setLevel(int level){
     this.level = level;
    }

    /**
     *return the level of this unit
     */
    public int getLevel() {
    return level;
    }

    @Override
    public int compareTo(Unit other) {
    return (Integer.valueOf(level)).compareTo(Integer.valueOf(other.level));
  }
}













