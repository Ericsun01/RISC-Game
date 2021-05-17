package edu.duke.ece651.risc.shared;

import java.util.*;

public class Utility {
   /**
   * A helper function for the dijkstra algo. Find the territory with the minimum size to pass under the current territory set.
   * @param dist stores distance from the starting territory for every territory in the map
   * @param processed stores the status of whether has processed for every territory in the map
   * @return the optimal territory
   */
  public static Territory minDistance(HashMap<Territory, Integer> dist, HashMap<Territory, Boolean> processed ){
    int min = Integer.MAX_VALUE;
    Territory minDist = null;

    for (Territory t : processed.keySet()) {
      if (processed.get(t) == false && dist.get(t)<= min) {
        min = dist.get(t);
        minDist = t;
      }
    }
    return minDist;
  }

  /**
   * Use Dijkstra Algorithm to calculate the minimum size to pass from one territory to the other
   * @param map the map of the game play on
   * @param from the start territory
   * @param to the destination
   * @return the min size to pass through
   */
  public static int find_min_size(Map map, Territory from, Territory to){
    Iterator<Territory> it = map.getTerritories();
    HashSet<Territory> territories = new HashSet<>();
    while(it.hasNext()){
      territories.add(it.next());
    }
    
    HashMap<Territory, Integer> dist = new HashMap<>();
    HashMap<Territory, Boolean> processed = new HashMap<>();
    for (Territory t : territories) {
        // dist[i] = Integer.MAX_VALUE;
        dist.put(t, Integer.MAX_VALUE);
        processed.put(t, false);
    }

      // Distance of source vertex from itself is always 0
      dist.put(from, 0);

      // Find shortest path for all vertices
      for(int count = 0; count < territories.size(); count++){
          Territory nearestTerritory = minDistance(dist, processed);
          processed.put(nearestTerritory, true); 
          for(Territory t : territories){
            if(!processed.get(t) && nearestTerritory.hasNeighbor(t) && dist.get(nearestTerritory) != Integer.MAX_VALUE && dist.get(nearestTerritory) + nearestTerritory.getSize() < dist.get(t)){
              dist.put(t,  dist.get(nearestTerritory) + nearestTerritory.getSize());
            }
          }
        
      }

      //printAll(dist);
      return dist.get(to);
  }
  
//  /** Utility print function for testing */
//  public static void printAll(HashMap<Territory, Integer> dist){
//    for(Territory t : dist.keySet()){
//      System.out.println(t.getName() + "'s size is " + t.getSize() + " and need " + dist.get(t) + " to travel");
//    }
//  }

}
