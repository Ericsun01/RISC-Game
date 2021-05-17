package edu.duke.ece651.risc.shared;

import java.io.Serializable;

public class SpyUpgradeAction extends UpgradeAction implements Serializable {

  private Territory territory;

  /**
   * constructor for SpyUpgradeAction
   * @param owner is the owner for the action
   * @param territory is the territory with the spy
   */
  public SpyUpgradeAction(Player owner, Territory territory){
    super(owner, 0);
    this.territory = territory;
  }

  /**
   * executes the action
   * @param map
   */
  @Override
  public void execute(Map map) {
    execute();
  }
  
  public void execute() {
    owner.dequeueTechResource(20);
    territory.dequeueSpy(owner);
    territory.addSpy(owner);
  }

  /**
   * checks that the action is valid
   * @return error message if invalid
   */
  @Override
  public String check() {
    String ans = null;
    
    if (owner.getTechResources() < 20) {
      ans = "You don't have enough tech resource, this upgrade will cost 20";
      return ans;
    }
    Unit originalUnit = getViableUnit();
    if (originalUnit == null) {
      ans = "No viable units to convert to spy";
      return ans;
    }

    owner.subtractTechResource(20);
    owner.queueTechResource(20);
    territory.subtractUnit(originalUnit.getLevel());
    territory.queueSpy(owner);
    return ans;
  }
  
  @Override
  public String check(Map map) {
    return check();
  }
  
  @Override
  public String getType() {
    // TODO Auto-generated method stub
    return "SpyUpgradeAction";
  }

  public Territory getTerritory() {
    return this.territory;
  }

  /**
   * finds the lowest level viable unit for promotion
   * @return the lowest level viable unit for promotion
   */
  private Unit getViableUnit() {
    for (Unit u: territory.getUnits()) {
      if (u.getLevel() > 0) {
        return u;
      }
    }
    return null;
  }
}
