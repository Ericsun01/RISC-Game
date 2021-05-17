package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class AttackAction extends TwoTerritoryAction implements Serializable {

  /**
   *Constructor for a new AttackAction
   *@param player is the player of attacker
   *@param to is the attacked territory
   *@param from is the attack territory
   *@param numUnits is the attacked Units
   */
  public AttackAction(Player player, Territory to, Territory from, int numUnits, ArrayList<Unit> units) {
    super(player, to, from, numUnits, units);
  }

  public AttackAction(Player player, Territory to, Territory from, ArrayList<Unit> units) {
    this(player, to, from, units.size(), units);
  }

  public AttackAction(Player player, Territory to, Territory from, int numUnits) {
    super(player, to, from, numUnits);

  }

    /**
     * executes the attack
     * @param map
     */
    @Override
    public void execute(Map map){
        Territory fromMap = map.getTerritoryByName(from.getTerritoryName());
        Territory toMap = map.getTerritoryByName(to.getTerritoryName());
        setFrom(fromMap);
        setTo(toMap);
        execute();

    }

    /**
     * executes the attack
     */
    @Override
    public void execute(){
        Dice attackDice = new Dice();
        Dice defendDice = new Dice();
        execute(attackDice, defendDice);
    }

  /**
   *Execute attack action with Dice injection
   */
	public void execute(Dice attackDice, Dice defendDice) {
    // if(from.getName() == "Armada"){
    //   System.out.println(from.getName() + " is attacking " + to.getName());
    // }
      owner.dequeueFoodresources(units.size());
      for(Unit unit:units){
        //from.subtractUnit(unit);
        from.dequeUnit(unit.getLevel());
      }

      boolean highestAttackTurn = true;
      while(to.getNumUnits() > 0 && units.size() > 0) {

        Unit attackUnit, defenderUnit;
        if(highestAttackTurn){
          attackUnit = units.get(units.size()-1);
          defenderUnit = to.getUnits().get(0);
        }
        else{
          attackUnit = units.get(0);
          defenderUnit = to.getUnits().get(to.getUnits().size()-1);
        }

        int attackNumber = attackDice.rollDiceWithBonus(attackUnit.getLevel());
        int defenderNumber = defendDice.rollDiceWithBonus(defenderUnit.getLevel());

        if(attackNumber > defenderNumber){
          to.getUnits().remove(defenderUnit);
          // if(from.getName() == "Armada"){
          //   System.out.println(from.getName() + "wins for one round");
          // }
          Collections.sort(to.getUnits());
        }
        else{
          units.remove(attackUnit);
          // if(from.getName() == "Armada"){
          //   System.out.println(to.getName() + "wins for one round");
          // }
          Collections.sort(units);
        }
        highestAttackTurn = !highestAttackTurn;

      }

      if(units.size() > 0){
        to.setOwner(owner);
        for(Unit unit:units){
          to.addUnit(unit);
        }
      }

//      System.out.println("AttackAction: after execute, "+owner.getName()+" Territory "+from.getName()+" has units "+from.getNumUnits()+", has queue units "+from.getNumQueueUnits()
//                +". Enemy territory "+to.getName()+" has units "+to.getNumUnits()+", has queue units "+to.getNumQueueUnits());
	}

  /**
   *Checks if action is valid and reduce the foodResource, subUnits for from territory
   */
  @Override
  public String check() {
    TwoTerritoryActionChecker attackChecker = new EnoughUnitsChecker(new AttackOwnershipChecker(new NeighborTerritoryChecker(new EnoughFoodResourceChecker(null))));
    String answer = attackChecker.checkInteraction(this);
    if(to.isBomb()==true){
      return "No player can move/attack units to TachyonixBomb's territory in the next turn\n";
    }
    if(answer==null){
      owner.subtractFoodResources(numUnits);
      owner.queueFoodresources(numUnits);
      // TODO: move unit to the queue's unit, remove them from the units
      //from.subtractNumUnits(numUnits);
//      System.out.println("AttackAction: "+owner.getName()+" wants "+units.size()+" units from Territory "+from.getName()+" to attack Territory "+to.getName());
//      System.out.println("AttackAction: before check, "+owner.getName()+" Territory "+from.getName()+" has units "+from.getNumUnits()+", has queue units "+from.getNumQueueUnits()
//      +". Enemy territory "+to.getName()+" has units "+to.getNumUnits()+", has queue units "+to.getNumQueueUnits());
      for(Unit u:units){
        from.queueUnit(u.getLevel());
        from.subtractUnit(u.getLevel());
      }
    }
//    System.out.println("AttackAction: after check, "+owner.getName()+" Territory "+from.getName()+" has units "+from.getNumUnits()+", has queue units "+from.getNumQueueUnits()
//            +". Enemy territory "+to.getName()+" has units "+to.getNumUnits()+", has queue units "+to.getNumQueueUnits());
    return answer;
  }

  public String check(Map map) {
    return check();
  }

  /**
   *@return the type of this action as a String
   */
  @Override
  public String getType() {
    return "AttackAction";
  }

  public void mergeAction(AttackAction otherAction) {
    this.units.addAll(otherAction.getUnitsList());
    for(Unit unit: otherAction.getUnitsList()){
      this.from.queueUnit(unit.getLevel());
      otherAction.getFrom().dequeUnit(unit.getLevel());
    }
    otherAction.getUnitsList().clear();
  }

}
