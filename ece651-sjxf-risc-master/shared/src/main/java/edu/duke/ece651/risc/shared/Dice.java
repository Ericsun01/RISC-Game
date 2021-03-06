package edu.duke.ece651.risc.shared;

import java.util.Random;

public class Dice {
  private Random random;
  private Bonus bonus;

  /**
      *Constructor for a new dice
      *@param random is the random of this dice
      */
   public Dice(Random random){
     this.random = random;
     this.bonus = new Bonus();
   }

  /**
      *Constructor for a new dice
      */
   public Dice(){
     this.random = new Random();
     this.bonus = new Bonus();
   }

  /**
      *Get the result of this roll with bonus
      */
  public int rollDiceWithBonus(int level){
    int res = random.nextInt(20) + 1 + bonus.getBonusByLevel(level);
     return res;
  }
}
