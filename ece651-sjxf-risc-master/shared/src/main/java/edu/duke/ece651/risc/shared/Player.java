package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

/**
 * Holds information about a particular player
 */
public class Player implements Serializable {
    private boolean isAI;
    private String name;
    private String password;
    private int playerNumber;
    private int unitsToPlace;  // checking
    private int queuedUnitsToPlace;  // execution
    private int queuedFoodResources;
    private int foodResources;
    private int techResources;
    private RoundActions roundActions;
    private LoginMessage loginMessage;
    private int maxTechLevel;
    private boolean canUpgradeMaxTech;
    private boolean canCloak;
    private int queuedTechResource;

    /**
     * Constructor for Player
     * @param name is the player's name
     * @param playerNumber is the player's number
     * @param unitsToPlace is the num units to place
     * @param foodResources is the player's food resources
     * @param techResources is the player's tech resources
     */
    public Player(String name, int playerNumber, int unitsToPlace, int foodResources, int techResources) {
        this.isAI = false;
        this.name = name;
        this.password = null;
        this.playerNumber = playerNumber;
        this.unitsToPlace = unitsToPlace;
        this.queuedUnitsToPlace = unitsToPlace;
        this.foodResources = foodResources;
        this.techResources = techResources;
        this.roundActions = new RoundActions();
        this.loginMessage = new LoginMessage();
        this.queuedFoodResources = 0;
        this.maxTechLevel = 1;
        this.canUpgradeMaxTech = true;
        this.canCloak = false;
        this.queuedTechResource = 0;
    }

    public Player(String name) {
        this(name, 0, 0, 0, 0);
    }

    public Player(String name, int foodResources) {
        this(name, 0, 0, foodResources, 0);
    }

    public Player(String name, int foodResources, int techResources){
        this(name, 0, 0, foodResources, techResources);
    }

    /**
     * Constructor that adds info regarding AI players
     * @param name is the name
     * @param playerNumber is the playernumber
     * @param unitsToPlace is the num units to place
     * @param foodResources is the food
     * @param techResources is the tech
     * @param isAI is whether the player is an AI
     */
    public Player(String name, int playerNumber, int unitsToPlace, int foodResources, int techResources, boolean isAI) {
        this(name, playerNumber, unitsToPlace, foodResources, techResources);
        this.isAI = isAI;
    }

    public boolean getIsAI() {
        return isAI;
    }

    public void decrementUnitsToPlace() {
        unitsToPlace--;
    }

    public void setUnitsToPlace(int numUnits) {
        unitsToPlace = numUnits;
    }

    public void addUnitsToPlace(int numUnits) {
        unitsToPlace += numUnits;
    }

    public void subtractUnitsToPlace(int numUnits) {unitsToPlace -= numUnits;}

    public void setQueuedUnitsToPlace(int numUnits) {queuedUnitsToPlace = numUnits;}

    public void addQueuedUnitsToPlace(int numUnits) {queuedUnitsToPlace += numUnits;}

    public void subtractQueuedUnitsToPlace(int numUnits) {queuedUnitsToPlace -= numUnits;}

    public void queueFoodresources(int numFoodresources) {queuedFoodResources += numFoodresources;}

    public void dequeueFoodresources(int numFoodresources) {queuedFoodResources -= numFoodresources;}

    public int getQueuedUnitsToPlace() {return queuedUnitsToPlace;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public void setPlayerNumber(int playerNumber) {this.playerNumber = playerNumber;}

    public int getPlayerNumber() {
        return playerNumber;
    }

    public int getUnitsToPlace() {
        return unitsToPlace;
    }

    public int getTechResources() {
        return techResources;
    }

    public synchronized RoundActions getRoundActions() {
        return this.roundActions;
    }

    public LoginMessage getLoginMessage() { return this.loginMessage; }

    public int getFoodResources() {
        return foodResources;
    }

    public void setFoodResources(int foodResources) {
        this.foodResources = foodResources;
    }

    public void subtractFoodResources(int foodResources){
        this.foodResources -= foodResources;
    }

    public int getQueuedFoodResources() {
        return queuedFoodResources;
    }

	public void setQueuedFoodResources(int queuedFoodResources) {
		this.queuedFoodResources = queuedFoodResources;
	}

	public int getMaxTechLevel() {
		return maxTechLevel;
	}

	public void setMaxTechLevel(int maxTechLevel) {
		this.maxTechLevel = maxTechLevel;
	}

	public void setTechResources(int techResources) {
		this.techResources = techResources;
	}

	public boolean isCanUpgradeMaxTech() {
		return canUpgradeMaxTech;
	}

	public void setCanUpgradeMaxTech(boolean canUpgradeMaxTech) {
		this.canUpgradeMaxTech = canUpgradeMaxTech;
	}

	public void setCanCloak(boolean canCloak) {
        this.canCloak = canCloak;
    }

    public boolean getCanCloak() {
        return this.canCloak;
    }

	public int getQueuedTechResource() {
		return queuedTechResource;
	}

	public void setQueuedTechResource(int queuedTechResource) {
		this.queuedTechResource = queuedTechResource;
	}

    public void subtractTechResource(int num){
    this.techResources -= num;
    }

    public void addTechResource(int num){
    this.techResources += num;
    }

    public void queueTechResource(int num){
    this.queuedTechResource += num;
    }

    public void dequeueTechResource(int num){
    this.queuedTechResource -= num;
    }

    public void updateMaxTechByOneLevel(){
    this.maxTechLevel++;
  }

    public void addResource(int addFood, int addTech) {
        this.foodResources += addFood;
        this.techResources += addTech;
    }
}











