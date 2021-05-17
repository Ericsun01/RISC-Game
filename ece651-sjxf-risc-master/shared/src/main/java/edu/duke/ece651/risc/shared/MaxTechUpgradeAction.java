package edu.duke.ece651.risc.shared;

import java.io.Serializable;

public class MaxTechUpgradeAction extends UpgradeAction implements Serializable{

  /**
   * constructor for MaxTechUpgradeAction
   * @param owner is the owner of the action
   * @param toLevel is the level to upgrade to
   */
  public MaxTechUpgradeAction(Player owner, int toLevel){
    super(owner, toLevel);
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
    MaxTechUpgradeCost m = new MaxTechUpgradeCost();
    owner.dequeueTechResource(m.getCostByLevel(toLevel));
    owner.updateMaxTechByOneLevel();
    owner.setCanUpgradeMaxTech(true);
  }

  /**
   * checks that the action is valid
   * @return error message if invalid
   */
  @Override
  public String check() {
    MaxTechUpgradeCost m = new MaxTechUpgradeCost();
    MaxTechUpgradeActionChecker checker = new MaxTechCanUpgradeChecker(new MaxTechEnoughTechResourcesChecker(new NextUpgradeLevelChecker(new MaxUpgradeLevelChecker(null))));
    String answer = checker.checkInteraction(this);
    if(answer==null){
      owner.subtractTechResource(m.getCostByLevel(toLevel));
      owner.queueTechResource(m.getCostByLevel(toLevel));
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
    return "MaxTechUpgradeAction";
  }



  
  
}










