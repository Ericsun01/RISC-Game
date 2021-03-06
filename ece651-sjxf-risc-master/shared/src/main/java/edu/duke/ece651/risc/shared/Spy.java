package edu.duke.ece651.risc.shared;

public class Spy extends Unit {
  private Player owner;

  /**
     *Constructor for a new Spy
     *@param owner is the owner of this Spy
     */
  public Spy(Player owner){
    super(0);
    this.owner = owner;
    //this.level = 0;
  }

  public Player getOwner(){
    return owner;
  }

  public void setOwner(Player owner){
    this.owner = owner;
  }
  
}
