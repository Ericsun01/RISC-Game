package edu.duke.ece651.risc.shared;

import java.util.ArrayList;

public class MoveAction extends TwoTerritoryAction {
  private Map map;
  /**
   *Constructor for a new MoveAction
   *@param player is the player of this action
   *@param to is the destination territory
   *@param from is the source territory
   *@param numUnits is the moved Units
   */
  public MoveAction(Player player, Territory to, Territory from, int numUnits, ArrayList<Unit> units) {
    super(player, to, from, numUnits, units);
    this.map = new Map();
  }

  public MoveAction(Player player, Territory to, Territory from, ArrayList<Unit> units, Map map) {
    this(player, to, from, units.size(), units);
    this.map = map;
  }

  
  @Override
  public String check(){
    return check(this.map);
  }
 
  /**
   *Check for valid action
   */
  @Override
  public String check(Map map) {
    this.from = map.getTerritoryByName(from.getName());
    TwoTerritoryActionChecker moveChecker = new EnoughUnitsChecker(new MoveOwnershipChecker(new SamePlayerConnectionChecker(null)));
    String answer = moveChecker.checkInteraction(this);
    if(to.isBomb()==true){
      return "No player can move/attack units to TachyonixBomb's territory in the next turn";
    }
    if(answer == null){
      int needFoodResources = Utility.find_min_size(map, from, to) * numUnits;
      if(owner.getFoodResources() < needFoodResources){
        answer = "Invalid action from " + from.getName() + " to " + to.getName() + ". " +
          from.getName() + " only has " + owner.getFoodResources() + " food resources left";
          // newly add before test:
          return answer;
      }
      owner.subtractFoodResources(needFoodResources);
      owner.queueFoodresources(needFoodResources);
//      System.out.println("MoveAction: "+owner.getName()+" wants "+units.size()+" units from Territory "+from.getName()+" move to Territory "+to.getName());
//      System.out.println("MoveAction: before check, "+owner.getName()+" Territory "+from.getName()+" has units "+from.getNumUnits()+", has queue units "+from.getNumQueueUnits()
//              +". destination territory "+to.getName()+" has units "+to.getNumUnits()+", has queue units "+to.getNumQueueUnits());
      // TODO: move unit to the queue's unit, remove them from the units
      for(Unit u:units){
        //System.out.println("MoveAction: "+owner.getName()+" has unit level: "+u.getLevel()+" in checking process");
        from.queueUnit(u.getLevel());
        from.subtractUnit(u.getLevel());
      }
    }
//    System.out.println("MoveAction: after check, "+owner.getName()+" Territory "+from.getName()+" has units "+from.getNumUnits()+", has queue units "+from.getNumQueueUnits()
//            +". Destination territory "+to.getName()+" has units "+to.getNumUnits()+", has queue units "+to.getNumQueueUnits());

    return answer;
  }



  /**
   *@return type of the class type as a String
   */
  @Override
  public String getType() {
    return "MoveAction";
  }

  @Override
  public void execute(Map map) {
    // TODO Auto-generated method stub
    for(Unit u : this.units){
      map.getTerritoryByName(from.getName()).dequeUnit(u.getLevel());
      map.getTerritoryByName(to.getName()).addUnit(u);

    }
    int min_size = Utility.find_min_size(map, from, to);
    owner.dequeueFoodresources(min_size * numUnits);
    // from.subtractUnit(u);
//    System.out.println("MoveAction: after execute, "+owner.getName()+" Territory "+from.getName()+" has units "+from.getNumUnits()+", has queue units "+from.getNumQueueUnits()
//            +". Destination territory "+to.getName()+" has units "+to.getNumUnits()+", has queue units "+to.getNumQueueUnits());
  }

  @Override
  public void execute() {
    // TODO Auto-generated method stub
    execute(this.map);
  }


}

