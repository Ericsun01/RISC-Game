package edu.duke.ece651.risc.shared;

import java.io.Serializable;

public class UnitUpgradeAction extends UpgradeAction implements Serializable {
    private Territory territory;
    private int fromLevel;
    private int numUnits;

    /**
     * Constructor for UnitUpgradeAction
     * @param owner is the owner of the action
     * @param territory is the territory
     * @param fromLevel is the from level
     * @param toLevel is the to level
     * @param numUnits is the number of units
     */
  public UnitUpgradeAction(Player owner, Territory territory, int fromLevel, int toLevel, int numUnits){
    super(owner, toLevel);
    this.territory = territory;
    this.fromLevel = fromLevel;
    this.numUnits = numUnits;
  }

    /**
     * executes the action
     * @param map
     */
  @Override
  public void execute(Map map) {
    execute();
  }

  public void execute(){
    UnitUpgradeCost m = new UnitUpgradeCost();
    for (int i = 0; i < numUnits; i++) {
        Unit u = territory.tryGetUnitByLevel(fromLevel);
        if (u == null) {return;}

//        owner.subtractTechResource((m.getCostByLevel(toLevel) - m.getCostByLevel(fromLevel)));
        owner.dequeueTechResource((m.getCostByLevel(toLevel) - m.getCostByLevel(fromLevel)));
        u.setLevel(toLevel);
    }
    //unit.setLevel(toLevel);
  }

    /**
     * checks that the action is valid
     * @return error message if invalid
     */
    @Override
    public String check() {
        UnitUpgradeCost m = new UnitUpgradeCost();
        UnitUpgradeActionChecker checker = new UnitEnoughTechResourceChecker(new MaxUnitLevelCanUpgradeChecker(new OnlyUplevelChecker(null)));
        String answer = checker.checkInteraction(this);
        if(answer==null) {
            int upgradeCost = (m.getCostByLevel(toLevel) - m.getCostByLevel(fromLevel)) * numUnits;
            owner.subtractTechResource(upgradeCost);
            owner.queueTechResource(upgradeCost);
        }
        return answer;
    }
  
    @Override
    public String check(Map map) {
  return check();
 }

    @Override
    public String getType() {
    // TODO Auto-generated method stub
        return "UnitUpgradeAction";
    }

    public int getNumUnits() {
      return numUnits;
    }

    public int getFromLevel() {
      return fromLevel;
    }

    public Territory getTerritory() {
      return territory;
    }

}






