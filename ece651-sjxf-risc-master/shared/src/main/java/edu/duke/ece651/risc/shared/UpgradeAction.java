package edu.duke.ece651.risc.shared;

import java.io.Serializable;

public abstract class UpgradeAction implements Action, Serializable {

    protected int toLevel;
    protected Player owner;

    /**
     * abstract class for upgrade actions
     * @param owner the owner of the action
     * @param toLevel the level to upgrade to
     */
  public UpgradeAction(Player owner, int toLevel){
    this.toLevel = toLevel;
    this.owner = owner;
  }

    /**
     * executes the action
     * @param map
     */
	abstract public void execute(Map map);

    /**
     * checks the action
     * @return error message if invalid
     */
	@Override
	abstract public String check();

	@Override
	abstract public String getType();

  abstract public void execute();

  public int getToLevel() {
    return toLevel;
  }

  public Player getOwner() {
    return owner;
  }

}









