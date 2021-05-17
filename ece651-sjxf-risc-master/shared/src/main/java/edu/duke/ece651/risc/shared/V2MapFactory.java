package edu.duke.ece651.risc.shared;

import java.util.List;

public class V2MapFactory implements AbstractMapFactory{


  /**
   *Builds two player map of following shape
   *
   *p1:t1,t2,t3  p2: t4,t5,t6
   *
   *t1-t2-t3        1 2 1                  3 6 3                   100 200 100
   *|  |  |   size:        foodProduction:        techProduction: 
   *t4-t5-t6        1 2 1                  3 6 3                   100 200 100
   */
	@Override
	public Map build2PlayerMap(Player p1, Player p2) {
		Map answer = new Map();
    //initialize territory whose numUnits = 0
    Territory t1 = new Territory(1, p1, "t1", 1, 20, 30);
    Territory t2 = new Territory(1, p1, "t2", 2, 40, 60);
    Territory t3 = new Territory(1, p1, "t3", 1, 20, 30);
    Territory t4 = new Territory(2, p2, "t4", 1, 20, 30);
    Territory t5 = new Territory(2, p2, "t5", 2, 40, 60);
    Territory t6 = new Territory(2, p2, "t6", 1, 20, 30);

    t1.addNeighbor(t2);
    t1.addNeighbor(t4);
    t2.addNeighbor(t3);
    t2.addNeighbor(t5);
    t3.addNeighbor(t6);
    t4.addNeighbor(t5);
    t5.addNeighbor(t6);

    answer.addTerritory(t1);
    answer.addTerritory(t2);
    answer.addTerritory(t3);
    answer.addTerritory(t4);
    answer.addTerritory(t5);
    answer.addTerritory(t6);

    return answer;
	}

  /**
   *Builds three player map of following shape
   *
   *p1:t1,t2,t3  p2: t4,t5,t6   p3:t7,t8,t9
   *
   *t1-t2-t3        1 2 1                  3 6 3                   100 200 100
   *|  |  |   size:        foodProduction:        techProduction: 
   *t4-t5-t6        1 2 1                  3 6 3                   100 200 100
   *|  |  |
   *t7-t8-t9        1 2 1                  3 6 3                   100 200 100
   */
	@Override
	public Map build3PlayerMap(Player p1, Player p2, Player p3) {
		Map answer = build2PlayerMap(p1, p2);

    Territory t7 = new Territory(3, p3, "t7", 1, 20, 30);
    Territory t8 = new Territory(3, p3, "t8", 2, 40, 60);
    Territory t9 = new Territory(3, p3, "t9", 1, 20, 30);

    answer.addTerritory(t7);
    answer.addTerritory(t8);
    answer.addTerritory(t9);

    t7.addNeighbor(answer.getTerritoryByName("t4"));
    t7.addNeighbor(t8);
    
    t8.addNeighbor(answer.getTerritoryByName("t5"));
    t8.addNeighbor(t9);

    t9.addNeighbor(answer.getTerritoryByName("t6"));
    
    return answer;

	}

  /**
   *Builds four player map of following shape
   *
   *p1:t1,t2,t3  p2: t4,t5,t6   p3:t7,t8,t9  p4:t10,t11,t12
   *
   *t1- t2- t3        1 2 1                  3 6 3                   100 200 100
   *|   |   |    size:        foodProduction:       techProduction:
   *t4- t5- t6        1 2 1                  3 6 3                   100 200 100
   *|   |   |
   *t7- t8- t9        1 2 1                  3 6 3                   100 200 100
   *|   |   |
   *t10-t11-t12       1 2 1                  3 6 3                   100 200 100
   */
	@Override
	public Map build4PlayerMap(Player p1, Player p2, Player p3, Player p4) {
    Map answer = build3PlayerMap(p1, p2, p3);

    Territory t10 = new Territory(4, p4, "t10", 1, 20, 60);
    Territory t11 = new Territory(4, p4, "t11", 2, 40, 90);
    Territory t12 = new Territory(4, p4, "t12", 1, 20, 60);

    answer.addTerritory(t10);
    answer.addTerritory(t11);
    answer.addTerritory(t12);

    t10.addNeighbor(answer.getTerritoryByName("t7"));
    t10.addNeighbor(t11);

    t11.addNeighbor(answer.getTerritoryByName("t8"));
    t11.addNeighbor(t12);

    t12.addNeighbor(answer.getTerritoryByName("t9"));
    
    return answer;
	}

  /**
   *Builds five player map of following shape
   *
   *p1:t1,t2,t3  p2: t4,t5,t6   p3:t7,t8,t9  p4:t10,t11,t12
   *
   *t1- t2- t3        1 2 1                  3 6 3                    100 200 100
   *|   |   |    size:        foodProduction:          techProduction:
   *t4- t5- t6        1 2 1                  3 6 3                    100 200 100
   *|   |   |
   *t7- t8- t9        1 2 1                  3 6 3                    100 200 100
   *|   |   |
   *t10-t11-t12       1 2 1                  3 6 3                    100 200 100
   *|   |   |
   *t13-t14-t15       1 2 1                  3 6 3                    100 200 100
   */
	@Override
	public Map build5PlayerMap(Player p1, Player p2, Player p3, Player p4, Player p5) {
      Map answer = build4PlayerMap(p1, p2, p3, p4);

      Territory t13 = new Territory(5, p5, "t13", 1, 20, 60);
      Territory t14 = new Territory(5, p5, "t14", 2, 40, 90);
      Territory t15 = new Territory(5, p5, "t15", 1, 20, 60);

      answer.addTerritory(t13);
      answer.addTerritory(t14);
      answer.addTerritory(t15);

      t13.addNeighbor(answer.getTerritoryByName("t10"));
      t13.addNeighbor(t14);

      t14.addNeighbor(answer.getTerritoryByName("t11"));
      t14.addNeighbor(t15);

      t15.addNeighbor(answer.getTerritoryByName("t12"));

      return answer;
	}

}









