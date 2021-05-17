package edu.duke.ece651.risc.shared;

import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *The class handles I/O with client as well as Game thread (server's main thread)
 */
public class ServerRunnable implements Runnable {
    private Socket operationSocket;
    private volatile Player player;
    private volatile Map map;
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private volatile Game game;
    private int groupID;

    /**
     *Constructor ServerRunnable
     *@param operationSocket is the socket that connects to a client
     *@param groupID is the groupID for territories allocation
     *@param player is the player object that stores status for this user
     *@param map is the map object being used in server
     *@param game is the game object that process game logic
     */
    public ServerRunnable(Socket operationSocket, int groupID, Map map, Player player, Game game) {
        this.operationSocket = operationSocket;
        this.player = player;
        this.map = map;
        this.os = null;
        this.is = null;
        this.game = game;
        this.groupID = groupID;
    }

    /**
     *the functions that runs the entire logic of ServerRunnable
     */
    public void run() {
        try {
            setPlayerInfo();

            executePlacement();

            actionPhase();

            operationSocket.close();
            System.out.println("ServerRunnable Exit!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot convert to ClientMessage type");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *Initialize serverRunnable, object input/output stream, receive username/password from client side
     *Send the group ID, map, player to client
     */
    public void setPlayerInfo() throws IOException, ClassNotFoundException, InterruptedException {
        this.os = new ObjectOutputStream(operationSocket.getOutputStream());
        this.is = new ObjectInputStream(operationSocket.getInputStream());

        String[] playerInfo = (String[])is.readObject();
        player.setName(playerInfo[0]);
        player.setPassword(playerInfo[1]);
        player.setPlayerNumber(groupID);
        this.player.getLoginMessage().setInformation(player.getName(), player.getPassword());
        //System.out.println("Server Side: Login information added into LoginMessage, waiting for recorded by Game");
        Thread.sleep(500);

        os.writeObject(this.groupID);
        os.reset();
        // For now, change the territories' owner by groupID
        game.setTerritoryOwner(this.groupID, this.player);
        //System.out.println("Server Side: Player " + groupID +" "+ this.player.getName() + " with password: "+ player.getPassword()+" reach ServerRunnable!");

        RoundResult roundResult = new RoundResult(this.map, this.player);
        os.writeObject(roundResult);
        os.reset();
        //System.out.println("Server Side: map has been sent to client -- "+player.getName());
    }

    /**
     * Recv the placement actions from client side
     * Check all actions and set them inside the shared buffer between serverRunnable thread as well as main Thread
     * Wait for main thread to execute, then send the result back to client
     */
    public void executePlacement() throws IOException, ClassNotFoundException, InterruptedException {
        List<Action> pm = (List<Action>)is.readObject();
        List<Action>  placementsList = new ArrayList<>();
        for (Action a: pm) {
            if (a instanceof PlacementAction) {
                PlacementAction pa = (PlacementAction) a;
                PlacementAction placementAction = new PlacementAction(player, map.getTerritoryByName(pa.getWhere().getName()), pa.getNumUnits());
                placementAction.check();
                placementsList.add(placementAction);
            }
        }
        this.player.getRoundActions().setActions(placementsList);
        this.player.getRoundActions().setInfoUpdated(false);
        //System.out.println("Server Side: Placement actions added into RoundActions");

        while (!game.getRoundIsOver()) {
            //System.out.println("Server Side: placements waiting for execution by Game");
            Thread.sleep(1000);
        }

        String viewRep = null;
        do {
            os.writeObject(map);
            os.reset();
        } while ((viewRep = (String)is.readObject()) == null);
        this.player.getRoundActions().setInfoUpdated(true);
        System.out.println("Server Side: "+viewRep);
    }


    /**
     * check and merge some of the actions(such as attack)
     * Deep copy the action object server receives, then returns a new list of actions
     */
    public List<Action> checkAndMergeGameActions(List<Action> actionList) {
        List<Action> gameActions = new ArrayList<>();
        HashMap<String, ArrayList<AttackAction>> commonTargetAttacks = new HashMap<>();

        for (Action a: actionList) {
            if (a == null) {
                System.out.println("Server : Player " + player.getName() + " has an null action! ");
                continue;
            }
            System.out.println("Server : Player " + player.getName() + " has an action: " + a.getType());
            if (a instanceof UnitUpgradeAction) {
                UnitUpgradeAction uua = (UnitUpgradeAction) a;
                UnitUpgradeAction unitUpgradeAction = new UnitUpgradeAction(player,
                        map.getTerritoryByName(uua.getTerritory().getName()),
                        uua.getFromLevel(), uua.getToLevel(), uua.getNumUnits());

                //Check AND EXECUTE
                unitUpgradeAction.check();
                unitUpgradeAction.execute();
            } else if (a instanceof SpyUpgradeAction) {
                SpyUpgradeAction sua = (SpyUpgradeAction)a;
                SpyUpgradeAction spyUpgradeAction = new SpyUpgradeAction(player, map.getTerritoryByName(sua.getTerritory().getName()));

                spyUpgradeAction.check();
                spyUpgradeAction.execute();
            } else if (a instanceof MoveAction) {
                MoveAction ma = (MoveAction) a;
                MoveAction moveAction = new MoveAction(player, map.getTerritoryByName(ma.getTo().getName()),
                        map.getTerritoryByName(ma.getFrom().getName()), ma.getUnitsList(), map);

                String result = moveAction.check();
                if (result != null) {
                    System.out.println("Server : Player "+player.getName()+", MoveAction has error message: "+result);
                    continue;
                }
                gameActions.add(moveAction);
            } else if (a instanceof SpyMoveAction) {
                SpyMoveAction sma = (SpyMoveAction) a;
                ArrayList<Spy> spies = new ArrayList<>();
                for (int i=0; i<sma.getSpiesNum();i++) {
                    spies.add(new Spy(player));
                }
                SpyMoveAction spyMoveAction = new SpyMoveAction(player, map.getTerritoryByName(sma.getTo().getName()),
                        map.getTerritoryByName(sma.getFrom().getName()), spies, map);

                String result = spyMoveAction.check();
                if (result != null) {
                    System.out.println("Server : Player "+player.getName()+", SpyMoveAction has error message: "+result);
                    continue;
                }
                gameActions.add(spyMoveAction);
            } else if (a instanceof EmpyreanArmadaAction) {
                EmpyreanArmadaAction eaa = (EmpyreanArmadaAction) a;
                EmpyreanArmadaAction empyreanArmadaAction = new EmpyreanArmadaAction(player, map.getTerritoryByName(eaa.getTo().getName()));

                empyreanArmadaAction.check();
                gameActions.add(empyreanArmadaAction);
            } else if (a instanceof PsionicStormAction) {
                PsionicStormAction psi = (PsionicStormAction)a;
                PsionicStormAction psionicStormAction = new PsionicStormAction(player, map.getTerritoryByName(psi.getTo().getName()));

                psionicStormAction.check();
                gameActions.add(psionicStormAction);
            } else if (a instanceof AttackAction) {
                AttackAction at = (AttackAction) a;
                AttackAction attackAction = new AttackAction(player, map.getTerritoryByName(at.getTo().getName()),
                        map.getTerritoryByName(at.getFrom().getName()), at.getUnitsList());

                String result = attackAction.check();
                if (result != null) {
                    System.out.println("Server : Player "+player.getName()+", AttackAction has error message: "+result);
                    continue;
                }
                commonTargetAttacks.putIfAbsent(attackAction.getTo().getName(), new ArrayList<AttackAction>());
                commonTargetAttacks.get(attackAction.getTo().getName()).add(attackAction);
            } else if (a instanceof MaxTechUpgradeAction) {
                MaxTechUpgradeAction mtua = (MaxTechUpgradeAction) a;
                MaxTechUpgradeAction maxTechUpgradeAction = new MaxTechUpgradeAction(player, mtua.getToLevel());

                maxTechUpgradeAction.check();
                gameActions.add(maxTechUpgradeAction);
            } else if (a instanceof CloakUpgradeAction) {
                CloakUpgradeAction cloakUpgradeAction = new CloakUpgradeAction(player);

                cloakUpgradeAction.check();
                gameActions.add(cloakUpgradeAction);
            } else if (a instanceof CloakAction) {
                CloakAction ca = (CloakAction) a;
                CloakAction cloakAction = new CloakAction(player, map.getTerritoryByName(ca.getCloakArea().getName()));

                cloakAction.check();
                gameActions.add(cloakAction);
            } else if (a instanceof TachyonicBombAction) {
                TachyonicBombAction tba = (TachyonicBombAction) a;
                TachyonicBombAction tachyonicBombAction = new TachyonicBombAction(player, map.getTerritoryByName(tba.getTo().getName()));

                String result = tachyonicBombAction.check();
                if (result != null) {
                    System.out.println("Server : Player "+player.getName()+", TachyonicBombAction has error message: "+result);
                    continue;
                }
                gameActions.add(tachyonicBombAction);
            }
        }
        //merge attacks to same territory
        for (ArrayList<AttackAction> attackActions: commonTargetAttacks.values()) {
            if (attackActions.size() < 2) {
                gameActions.add(attackActions.get(0));
                continue;
            }
            for (int i = 1; i < attackActions.size(); i++) {
                attackActions.get(0).mergeAction(attackActions.get(i));
            }
            gameActions.add(attackActions.get(0));
        }
        return gameActions;
    }


    /**
     * Recv the round actions from client side
     * Check all actions and set them inside the shared buffers between serverRunnable thread as well as main Thread
     * Wait for main thread to execute, then send the result back to client
     * Check does game over in this round
     */
    public void actionPhase() throws IOException, ClassNotFoundException, InterruptedException {
        while (true) {
            List<Action> actionList = (List<Action>)is.readObject();

            List<Action> gameActions = checkAndMergeGameActions(actionList);

            this.player.getRoundActions().setActions(gameActions);
            this.player.getRoundActions().setInfoUpdated(false);

            System.out.println("Server: Waiting for game to execute");
            while (!game.getRoundIsOver()) {
                System.out.println("Server: actions waiting for execution by Game");
                Thread.sleep(1000);
            }

            System.out.println("Server: game finish executing!");
            String viewRep = null;

            RoundResult roundResult = new RoundResult(map, player);

            System.out.println("Server: Ready to send to client!");
            do {
                os.writeObject(roundResult);
                os.reset();
                System.out.println("Server: finish sending to client!");
            } while ((viewRep = (String)is.readObject()) == null);
            this.player.getRoundActions().setInfoUpdated(true);
            System.out.println("Server Side: "+viewRep);

            System.out.println("Server : "+player.getName()+" food resource is "+player.getFoodResources()+", your tech resource is "+player.getTechResources());

            if (game.getGameOver()) {
                String endGame = "gameOver";
                os.writeObject(endGame);
                os.reset();
                break;
            } else {
                String continuing = "countinue";
                os.writeObject(continuing);
                os.reset();
            }
        }
    }
}

