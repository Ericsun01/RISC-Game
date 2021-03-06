package edu.duke.ece651.risc.shared;

public interface AbstractMapFactory {
  /**
   *Build maps for various numbers of players
   */
  public Map build2PlayerMap(Player p1, Player p2);
  public Map build3PlayerMap(Player p1, Player p2, Player p3);
  public Map build4PlayerMap(Player p1, Player p2, Player p3, Player p4);
  public Map build5PlayerMap(Player p1, Player p2, Player p3, Player p4, Player p5);

}










