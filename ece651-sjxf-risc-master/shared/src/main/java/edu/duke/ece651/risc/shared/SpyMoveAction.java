package edu.duke.ece651.risc.shared;

import java.util.ArrayList;

public class SpyMoveAction extends TwoTerritoryAction{
    private Map map;
    private ArrayList<Spy> spies;

    /**
   *Constructor for a new MoveAction
   *@param player is the owner of this action, and the owner of this spy
   *@param to is the destination territory
   *@param from is the source territory
   *@param spies is the moved spy
   */
    public SpyMoveAction(Player player, Territory to, Territory from, int numUnits, ArrayList<Spy> spies, Map map){
        super(player, to, from, spies);
        this.map = map;
        this.spies = new ArrayList<>(spies);
    }

    public SpyMoveAction(Player player, Territory to, Territory from, ArrayList<Spy> spies, Map map) {
        this(player, to, from, spies.size(), spies, map);
    }

    @Override
    public String check(){
        return check(this.map);
    }
 

    @Override
    public String check(Map map){
        this.from = map.getTerritoryByName(from.getName());
        this.to = map.getTerritoryByName(to.getName());
        String ans = null;
        TwoTerritoryActionChecker spyMoveChecker = null;
        if(to.isBomb()){
          return "No player can move/attack units to Tachyonic Bomb's territory in the next turn";
        }
        // to & from both of the owner : normal action
        if(from.getOwner().getName().equals(owner.getName()) && to.getOwner().getName().equals(owner.getName())){
            spyMoveChecker = new EnoughSpiesChecker(new SamePlayerConnectionChecker(null));
            ans = spyMoveChecker.checkInteraction(this);
        }else{
            // to & from both of an enemy : need to be neighbor
            if(to.hasNeighbor(from)){
                spyMoveChecker = new EnoughSpiesChecker(null);
                ans = spyMoveChecker.checkInteraction(this);
            }else{
                ans = "Invalid action from " + from.getName() + " to " + to.getName() + ". Spies can't travel across more than 1 territory of enemy in one turn!";
            }
        }
        if(ans == null){
            ans = checkSpyOwnership();
            if(ans != null){
                return ans;
            }
            // check food resource
            int needFoodResources = Utility.find_min_size(map, from, to) * numUnits;
            if(owner.getFoodResources() < needFoodResources){
                ans = "Invalid action from " + from.getName() + " to " + to.getName() + ". " +
                from.getName() + " only has " + owner.getFoodResources() + " food resources left.";
                // has to check if should return here
                return ans;
            }
            owner.subtractFoodResources(needFoodResources);
            owner.queueFoodresources(needFoodResources);
            // System.out.println("MoveAction: "+owner.getName()+" wants "+units.size()+" units from Territory "+from.getName()+" move to Territory "+to.getName());
            // System.out.println("MoveAction: before check, "+owner.getName()+" Territory "+from.getName()+" has units "+from.getNumUnits()+", has queue units "+from.getNumQueueUnits()
            //         +". destination territory "+to.getName()+" has units "+to.getNumUnits()+", has queue units "+to.getNumQueueUnits());
            //move spies to the queue's spies, remove them from the spies list
            for(Spy spy : spies){
                // System.out.println("Checkout: "+owner.getName()+" has spy in checking process");
                map.getTerritoryByName(from.getName()).queueSpy(owner);
                map.getTerritoryByName(from.getName()).subtractSpy(owner);
            }
        }
        return ans;
    }

    /**
     * The utility function to check the spy ownership
     * @return the error msg
     */
    public String checkSpyOwnership(){
        for(Unit s : units){
            Spy spy = null;
            if(s instanceof Spy){
                spy = (Spy)s;
            }else{
                return  "Invalid action, one or more units you are moving is not spy!";
            }
            if(spy.getOwner() != owner){
                return  "Invalid action! You are moving a spy that not belongs to you!";
            }
        }
        return null;
    }

    /**
   *@return type of the class type as a String
   */
    @Override
    public String getType() {
        return "SpyMoveAction";
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        execute(map);
    }

    @Override
    public void execute(Map map) {
        // TODO Auto-generated method stub
        System.out.println("SpyMoveAction: before execute, "+owner.getName()+" Territory "+from.getName()+" has your spy units "+from.getNumSpies(owner)+
                ". Destination territory "+to.getName()+" has your Spy units "+to.getNumSpies(owner));
        for(Spy s : spies){
            map.getTerritoryByName(from.getName()).dequeueSpy(owner);
            map.getTerritoryByName(to.getName()).addSpy(owner);
        }
        int min_size = Utility.find_min_size(map, from, to);
        owner.dequeueFoodresources(min_size * numUnits);
         System.out.println("SpyMoveAction: after execute, "+owner.getName()+" Territory "+from.getName()+" has your spy units "+from.getNumSpies(owner)+
                 ". Destination territory "+to.getName()+" has your Spy units "+to.getNumSpies(owner));
    }

    public ArrayList<Spy> getSpies() {
        return spies;
    }

    public int getSpiesNum() {
        return units.size();
    }
}
