package edu.duke.ece651.risc.shared;

import java.io.Serializable; 
import java.util.ArrayList;

public class PsionicStormAction implements Action, Serializable{
    /**
     * Psionic storm: for that destination territory
     * all neighboring territories -10 units, eliminate all the spies, also -20 tech resources/Territory. 
     * requires player level 4, 
     * need -450 tech resources, -150 food resources
     * question: does "to" should be stormed? should "to" be enemy territory?
     */
    public static final int TECH_COST = 450;
    public static final int FOOD_COST = 150;

    public static final int TECH_ATTACK = 20;
    public static final int UNIT_ATTACK = 10;

    public static final int REQUIRED_LEVEL = 4;
    
    private Player owner;
    private Territory to;
    private ArrayList<Territory> targets = new ArrayList<>();
    
    public PsionicStormAction(Player owner, Territory to){
        this.owner = owner;
        this.to = to;
        Iterable<Territory> neighbors = to.getNeighbors();
        if(!to.getOwner().getName().equals(this.owner.getName())){
            this.targets.add(to);
        }
        for (Territory neighbor: neighbors) { // iterate the to territory's neighbors
            if (!neighbor.getOwner().getName().equals(owner.getName())) {   // add to the target list if neighbor not belongs to action owner
                this.targets.add(neighbor);
            }
        }
    }

    /**
     * executes the action
     */
    public void execute(){
        for(Territory target : targets){
            ArrayList<Unit> queuedUnits = new ArrayList<>(); //avoid concurrent exception
            ArrayList<Spy> queuedSpies = new ArrayList<>();
            for(Unit u : target.getQueuedUnits()){
                queuedUnits.add(u);
            }
            for(Unit u : queuedUnits){
                target.dequeUnit(u);
            }
            target.getOwner().dequeueTechResource(target.getOwner().getQueuedTechResource());
            for(Spy s : target.getQueuedSpies()){
                queuedSpies.add(s);
            }
            for(Spy s : queuedSpies){
                target.dequeueSpy(s.getOwner());
            }
        }
        owner.dequeueFoodresources(FOOD_COST);
        owner.dequeueTechResource(TECH_COST);
    }

    /**
     * Check how many units left in each target territory, queue 10 units, queue all the spies not from the action owner, queue tech resource of each enemy
     * Check the level of the owner, queue tech & food resource of the action owner
     */
    public String check(){
        if(this.owner.getMaxTechLevel() < REQUIRED_LEVEL){
            return "Invalid Psionic Storm, your max tech level hasn't reach level 4!";
        }
        if(this.owner.getTechResources() < TECH_COST || this.owner.getFoodResources() < FOOD_COST){
            return "Invalid Psionic Storm, you don't have enough resource to use this weapon!";
        }
        for(Territory target : this.targets){
            ArrayList<Unit> originUnits = new ArrayList<>();
            ArrayList<Spy> originSpies = new ArrayList<>();
            for(Unit u :  target.getUnits()){
                originUnits.add(u);
            }
            for(Spy s : target.getSpies()){
                originSpies.add(s);
            }
            int count = 0;
            for(Unit u : originUnits){
                if(count < UNIT_ATTACK){
                    target.queueUnit(u.getLevel());
                    target.subtractUnit(u.getLevel());
                    count++;
                }
            }
            if(target.getOwner().getTechResources() < TECH_ATTACK){
                target.getOwner().queueTechResource(target.getOwner().getTechResources());
                target.getOwner().subtractTechResource(target.getOwner().getTechResources());
            }else{
                target.getOwner().queueTechResource(TECH_ATTACK);
                target.getOwner().subtractTechResource(TECH_ATTACK);
            }
            for(Spy s : originSpies){
                if(!s.getOwner().equals(this.owner)){
                    target.queueSpy(s.getOwner());
                    target.subtractSpy(s.getOwner());
                }
            }
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
        return "PsionicStorm";
    }

    public Player getOwner(){
        return this.owner;
    }

    public Territory getTo(){
        return this.to;
    }

    public ArrayList<Territory> getTargets(){
        return this.targets;
    }

}
