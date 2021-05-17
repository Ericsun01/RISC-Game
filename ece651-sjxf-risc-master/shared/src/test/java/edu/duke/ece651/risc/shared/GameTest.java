package edu.duke.ece651.risc.shared;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameTest {
    @Test
    public void test_addPlayer_getPlayer() {
        Game game = new Game();
        Player player1 = new Player("A", 0, 10, 10, 10);
        Player player2 = new Player("B", 1, 10, 10, 10);
        Player player3 = new Player("C", 2, 10, 10, 10);

        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        List<Player> list = new ArrayList<>();
        list.add(player1);
        list.add(player2);
        list.add(player3);

        int i=0;
        Iterator<Player> iter = game.getPlayer();
        while (iter.hasNext()) {
            assertEquals(list.get(i++),iter.next());
        }

    }

    private Map createMap(Player p1, Player p2) {
        Map map = new Map();

        Territory t1 = new Territory(1, p1,"t1",  0, 10, 10, 10);
        Territory t2 = new Territory(1, p1,"t2", 0, 11, 11, 11);
        Territory t3 = new Territory(1, p1,"t3",  0, 12, 12, 12);
        Territory t4 = new Territory(2, p1,"t4",  0, 13, 13, 13);
        Territory t5 = new Territory(2, p1,"t5",  0, 14, 14, 14);
        Territory t6 = new Territory(2, p2,"t6",  0, 15, 15, 15);

        t1.addNeighbor(t2);
        t2.addNeighbor(t3);
        t3.addNeighbor(t4);
        t4.addNeighbor(t5);
        t5.addNeighbor(t6);
        t6.addNeighbor(t1);
        t2.addNeighbor(t5);

        map.addTerritory(t1);
        map.addTerritory(t2);
        map.addTerritory(t3);
        map.addTerritory(t4);
        map.addTerritory(t5);
        map.addTerritory(t6);

        return map;
    }

    @Test
    public void gameIsOver() {
        Player p1 = new Player("Bob", 0, 10, 10, 10);
        Player p2 = new Player("Sue", 1, 10, 0, 0);
        Map map1 = createMap(p1, p2);
        Map map2 = createMap(p1, p1);
        Game game1 = new Game(map1);
        Game game2 = new Game(map2);

        assertFalse(game1.gameIsOver());
        assertTrue(game2.gameIsOver());
    }

    @Test
    public void Test_addOneUnitByTurn() {
        V2MapFactory factory = new V2MapFactory();
        Player p1 = new Player("Bob", 1, 10, 100, 100);
        Player p2 = new Player("Eve", 2, 10, 100, 100);

        Map map = factory.build2PlayerMap(p1, p2);
        Game game = new Game(map);
        game.addOneUnitByTurn();
        Iterator<Territory> iter = map.getTerritories();
        while (iter.hasNext()) {
            Territory ter = iter.next();
            assertEquals(1,ter.getNumUnits());
        }

    }

    @Test
    public void Test_addResourceByTurn() {

        Player p1 = new Player("Bob", 1, 10, 100, 100);
        Player p2 = new Player("Eve", 2, 10, 100, 100);

        Map map = new Map();
        Territory t1 = new Territory(p1, "Mordor", 5, 5, 10, 10);
        Territory t2 = new Territory(p2, "Gondor", 3, 7, 15, 5);
        map.addTerritory(t1);
        map.addTerritory(t2);
        Game game = new Game(map);
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addResourceByTurn();

        assertEquals(110, p1.getFoodResources());
        assertEquals(110, p1.getTechResources());
        assertEquals(115, p2.getFoodResources());
        assertEquals(105, p2.getTechResources());

    }

    @Test
    public void Test_doCloakAction() {
        Player p1 = new Player("Bob", 1, 10, 100, 100);
        Player p2 = new Player("Eve", 2, 10, 100, 100);

        Map map = new Map();
        Territory t1 = new Territory(p1, "Mordor", 5, 5, 10, 10);
        Territory t2 = new Territory(p2, "Gondor", 3, 7, 15, 5);
        map.addTerritory(t1);
        map.addTerritory(t2);
        Game game = new Game(map);

        CloakAction ca1 = new CloakAction(p1, t1);
        CloakAction ca2 = new CloakAction(p2, t2);
        List<CloakAction> list = new ArrayList<>();
        list.add(ca1);
        list.add(ca2);

        game.doTimedAction(list, game.getCloakTable(), 3);
        assertTrue(t1.getCloaked());
        assertTrue(t2.getCloaked());
    }

    @Test
    public void Test_updateCloakResult() {
        Player p1 = new Player("Bob", 1, 10, 100, 100);
        Player p2 = new Player("Eve", 2, 10, 100, 100);

        Map map = new Map();
        Territory t1 = new Territory(p1, "Mordor", 5, 5, 10, 10);
        Territory t2 = new Territory(p2, "Gondor", 3, 7, 15, 5);
        map.addTerritory(t1);
        map.addTerritory(t2);
        Game game = new Game(map);

        CloakAction ca1 = new CloakAction(p1, t1);
        CloakAction ca2 = new CloakAction(p2, t2);
        List<CloakAction> list = new ArrayList<>();
        list.add(ca1);
        game.doTimedAction(list, game.getCloakTable(), 3);
        game.updateCloakResult();
        HashMap<CloakAction, Integer> cloakTable1 = game.getCloakTable();
        assertEquals(cloakTable1.get(ca1), 2);
        list.clear();

        list.add(ca2);
        game.doTimedAction(list, game.getCloakTable(), 3);
        game.updateCloakResult();
        HashMap<CloakAction, Integer> cloakTable2 = game.getCloakTable();
        assertEquals(cloakTable2.get(ca1), 1);
        assertEquals(cloakTable2.get(ca2), 2);

        game.updateCloakResult();
        game.updateCloakResult();
        HashMap<CloakAction, Integer> cloakTable3 = game.getCloakTable();
        assertEquals(cloakTable3.size(), 0);
    }

    @Test
    public void test_doCloakUpgradeActions() {
        Player p1 = new Player("Bob", 1, 10, 100, 100);
        Player p2 = new Player("Eve", 2, 10, 100, 100);

        Map map = new Map();
        Territory t1 = new Territory(p1, "Mordor", 5, 5, 10, 10);
        Territory t2 = new Territory(p2, "Gondor", 3, 7, 15, 5);
        map.addTerritory(t1);
        map.addTerritory(t2);
        Game game = new Game(map);

        p1.setMaxTechLevel(3);
        p2.setMaxTechLevel(3);
        CloakUpgradeAction cua1 = new CloakUpgradeAction(p1);
        CloakUpgradeAction cua2 = new CloakUpgradeAction(p2);
        List<CloakUpgradeAction> list = new ArrayList<>();
        list.add(cua1);
        list.add(cua2);
        game.doActions(list);

        assertTrue(p1.getCanCloak());
        assertTrue(p2.getCanCloak());
    }

  @Test
    public void Test_doTachyonicBombAction() {
        Player p1 = new Player("Bob", 1, 10, 300, 300);
        Player p2 = new Player("Eve", 2, 10, 300, 300);

        Map map = new Map();
        Territory t1 = new Territory(p1, "Mordor", 5, 5, 10, 10);
        Territory t2 = new Territory(p2, "Gondor", 3, 7, 15, 5);
        map.addTerritory(t1);
        map.addTerritory(t2);
        Game game = new Game(map);

        TachyonicBombAction ta1 = new TachyonicBombAction(p1, t1);
        //CloakAction ca2 = new CloakAction(p2, t2);
        List<TachyonicBombAction> list = new ArrayList<>();
        list.add(ta1);
        //list.add(ca2);

        game.doTimedAction(list, game.getBombTable(), 2);
        assertTrue(t1.isBomb());
        //assertTrue(t2.getCloaked());
    }
  
  @Test
  public void Test_updateTachyBombResult() {
    Player p1 = new Player("Bob", 1, 10, 3000, 3000);
    Player p2 = new Player("Eve", 2, 10, 3000, 1030);
    
    Map map = new Map();
    Territory t1 = new Territory(p1, "Mordor", 5, 5, 10, 10);
    Territory t2 = new Territory(p2, "Gondor", 3, 7, 15, 5);
    map.addTerritory(t1);
    map.addTerritory(t2);
    Game game = new Game(map);
    
    //CloakAction ca1 = new CloakAction(p1, t1);
    //CloakAction ca2 = new CloakAction(p2, t2);
    TachyonicBombAction ta1 = new TachyonicBombAction(p1, t1);
    TachyonicBombAction ta2 = new TachyonicBombAction(p2, t2);
    List<TachyonicBombAction> list = new ArrayList<>();
    list.add(ta1);
    game.doTimedAction(list, game.getBombTable(), 2);
    game.updateTachyonicBombResult();
    HashMap<TachyonicBombAction, Integer> bombTable1 = game.getBombTable();
    assertEquals(bombTable1.get(ta1), 1);
    list.clear();
    
    list.add(ta2);
    game.doTimedAction(list, game.getBombTable(), 2);
    game.updateTachyonicBombResult();
    HashMap<TachyonicBombAction, Integer> bombTable2 = game.getBombTable();
    assertEquals(bombTable2.size(),1);
    assertEquals(bombTable2.get(ta2), 1);
    
    game.updateTachyonicBombResult();
    //game.updateCloakResult();
    HashMap<TachyonicBombAction, Integer> bombTable3 = game.getBombTable();
    assertEquals(bombTable3.size(), 0);
  }

  @Test
  public void test_Login() throws InterruptedException {
        V2MapFactory mf = new V2MapFactory();
        Player p1 = new Player("Bob");
        Player p2 = new Player("Sue");
        Map m = mf.build2PlayerMap(p1, p2);
        Game game = new Game(m);
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(new Player("ai", 3, 0, 0, 0, true));

        p1.getLoginMessage().setInformation("Bob", "123");
        p2.getLoginMessage().setInformation("Sue","456");

        p1.getLoginMessage().setAvailable(true);
        p2.getLoginMessage().setAvailable(true);

        game.recordLoginInfo();

        assertEquals(game.getLoginInfo().size(), 2);

        p1.getLoginMessage().setAvailable(true);
        p2.getLoginMessage().setAvailable(true);

        game.recordLoginInfo();

  }

    @Test
    public void test_getWinner() {
        ArrayList<Territory> territories = new ArrayList<>();
        territories.add(new Territory(new Player("Bob"), "t1", 0, 0, 0 ,0));
        Iterator<Territory> iter = territories.iterator();
        Iterator<Territory> iter2 = territories.iterator();
        Map mockMap = mock(Map.class);
        when(mockMap.getTerritories()).thenReturn(iter);

        Game g = new Game(mockMap);

        Player p = g.getWinner();
        verify(mockMap).getTerritories();

        assertEquals(p.getName(), "Bob");
        g.getBombTable();
        g.getCloakTable();

        when(mockMap.getTerritories()).thenReturn(iter2);
        assertTrue(g.gameIsOver());
    }

    @Test
    public void test_gameIsOver() {
        ArrayList<Territory> territories = new ArrayList<>();
        Player p = new Player("Bob");
        territories.add(new Territory(p, "t1", 0,0,0,0));
        territories.add(new Territory(p, "t2", 0,0,0,0));
        territories.add(new Territory(new Player("Sue"), "t2",0,0,0,0));
        Iterator<Territory> iter = territories.iterator();
        Map mockMap = mock(Map.class);
        when(mockMap.getTerritories()).thenReturn(iter);

        Game g = new Game(mockMap);

        assertFalse(g.gameIsOver());
    }

    @Test
    public void test_findComputerPlayer() {
        Game g = new Game();
        Player cp1 = new Player("cp1");
        g.addComputerPlayer(new ComputerPlayer(new Map(), g, cp1, 1));
        g.addPlayer(new Player("bob"));

        assertNotNull(g.findComputerPlayerByPlayer(cp1));
        assertNull(g.findComputerPlayerByPlayer(new Player("bob")));

        Iterator<Player> iter = g.getPlayer();
        Map m = g.getMap();
        g.computerPlayerInitialize();
    }

    @Test
    public void test_setTerritoryOwner() {
        Player p1 = new Player("Bob");
        Player p2 = new Player("Sue");
        V2MapFactory mf = new V2MapFactory();
        Map m = mf.build2PlayerMap(p1, p2);

        Game g = new Game(m);
        g.setTerritoryOwner(1, p1);
        g.setTerritoryOwner(2, p2);
        assertEquals(g.getMap().getTerritoryByName("t1").getOwner(), p1);
        assertEquals(g.getMap().getTerritoryByName("t4").getOwner(), p2);

    }

    @Test
    public void test_waitForRoundEnd() throws InterruptedException {
        Player p = mock(Player.class);
        RoundActions ra = mock(RoundActions.class);
        when(p.getRoundActions()).thenReturn(ra);
        when(ra.getInfoUpdated()).thenReturn(true);

        Game g = new Game();
        g.waitForRoundEnd();
    }

    @Test
    public void test_doPlacementRound() throws InterruptedException {
        Player p1 = new Player("Bob");
        Player cp1 = new Player("cp1", 3, 0, 0, 0, true);
        V2MapFactory mf = new V2MapFactory();
        Map m = mf.build2PlayerMap(p1, cp1);
        ArrayList<Action> p1Actions = new ArrayList<>();
        p1Actions.add(new PlacementAction(p1, m.getTerritoryByName("t1"), 10));
        p1.getRoundActions().setActions(p1Actions);
        p1.getRoundActions().setInfoUpdated(true);

        Game g = new Game(m);
        g.addPlayer(p1);
        g.addPlayer(cp1);
        g.addComputerPlayer(new ComputerPlayer(m, g, cp1, 2));
        g.doPlacementRound();
    }

    @Test
    public void test_gamePlay() throws InterruptedException {
        Player cp1 = new Player("cp1",1,0,0,0, true);
        Player cp2 = new Player("cp2", 2, 0, 0, 0, true);
        Player cp3 = new Player("cp3", 3, 0,0, 0, true);

        V2MapFactory mf = new V2MapFactory();
        Map m = mf.build3PlayerMap(cp1, cp2, cp3);

        Game g = new Game(m);
        ComputerPlayer comp1 = new ComputerPlayer(m,g, cp1,1);
        ComputerPlayer comp2 = new ComputerPlayer(m, g, cp2, 2);
        ComputerPlayer comp3 = new ComputerPlayer(m, g, cp3, 3);
        g.addComputerPlayer(comp1);
        g.addComputerPlayer(comp2);
        g.addComputerPlayer(comp3);
        g.addPlayer(cp1);
        g.addPlayer(cp2);
        g.addPlayer(cp3);
        g.gamePlay();
    }
}












