package edu.duke.ece651.risc.shared;

import java.io.Serializable; 
import java.util.ArrayList;

public class EmpyreanArmadaAction implements Action, Serializable{

    public static final int TECH_COST = 200;
    public static final int FOOD_COST = 400;
    public static final int REQUIRED_LEVEL = 4;
    public static final int UNITS_DEPLOYED = 15;

    private Player owner;
    private Territory to;
    private Territory army;
    private AttackAction armadaAttack;

    /**
     * Constructor for EmpyreanArmadaAction
     * @param owner is the owner of the action
     * @param to is the territory for the action
     */
    public EmpyreanArmadaAction(Player owner, Territory to){
        this.owner = owner;
        this.to = to;
        if(!to.getOwner().equals(owner)){
            army = new Territory(owner, "Armada", 0, 0, 0, 0);
            ArrayList<Unit> attackUnits = new ArrayList<>();
            for(int i = 0; i < UNITS_DEPLOYED; i++){
                army.addUnit(new Unit(6));
                attackUnits.add(new Unit(6));
            }
            armadaAttack = new AttackAction(owner, to, army, attackUnits);
        }   
    }

    /**
     * executes the action
     */
    public void execute(){
        if(this.to.getOwner().equals(this.owner)){
            for(int i = 0; i < UNITS_DEPLOYED; i++){
                to.addUnit(new Unit(6));
            }
        }else{
            if(armadaAttack != null){
                armadaAttack.execute();
            }  
        }
    }

    /**
     * Check the level of the owner, queue tech & food resource of the action owner
     * Check the ownership of the target territory, if it's the owner of the action, queue units to place,
     * if it's enemy, make a new Utopia territory and make an attack action
     */
    public String check(){
        if(this.owner.getMaxTechLevel() < REQUIRED_LEVEL){
            return "Invalid Empyrean Armada Action, your max tech level hasn't reach level 4!\n";
        }
        if(this.owner.getTechResources() < TECH_COST || this.owner.getFoodResources() < FOOD_COST){
            return "Invalid Empyrean Armada Action, you don't have enough resource to use this weapon!\n";
        }
        // normal units if drop on owners territory
        if(this.to.getOwner().equals(this.owner)){
            owner.addQueuedUnitsToPlace(UNITS_DEPLOYED);
        }else{
            if(army != null){
                for(int i = 0; i < UNITS_DEPLOYED; i++){
                    army.queueUnit(6);
                    army.subtractUnit(6);
                }
            }else{
                return "Invalid Armada Action, unknown error!";
            }
        }
        owner.subtractFoodResources(FOOD_COST);
        owner.queueFoodresources(FOOD_COST);
        owner.subtractTechResource(TECH_COST);
        owner.queueTechResource(TECH_COST);
        return null;
    }

    /**
     * checks that the action is valid
     * @param map
     * @return
     */
    public String check(Map map){
        this.to = map.getTerritoryByName(to.getName());
        return check();
    }

    public String getType(){
        return "EmpyreanArmada";
    }

    public Player getOwner(){
        return this.owner;
    }

    public Territory getTo(){
        return this.to;
    }

}
