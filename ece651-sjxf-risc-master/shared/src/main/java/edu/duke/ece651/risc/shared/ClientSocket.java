package edu.duke.ece651.risc.shared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

/**
 *The class handles I/O with server as well as GUI thread (client's main thread)
 */
public class ClientSocket extends Thread {
    private Socket clientSocket;
    private String serverName;
    private int port;
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private volatile Map map;
    private volatile Player player;
    private int groupID;
    private LoginMessage loginMessage;
    private ActionMessage actionMessage;
    private MapMessage mapMessage;
    private RoundMessage roundMessage;

    /**
     *Constructor ClientSocket
     *@param serverName is the ip address of server
     *@param port is the port num of server
     *@param player is the player object that stores status for this user
     *@param map is the map object being used in server
     */
    // For main function
    public ClientSocket(String serverName, int port, Player player, Map map) {
        this.clientSocket = null;
        this.serverName = serverName;
        this.port = port;
        this.os = null;
        this.is = null;
        this.map = map;
        this.player = player;
        this.groupID = 0;
    }

    /**
     *Constructor ClientSocket
     *@param serverName is the ip address of server
     *@param port is the port num of server
     *@param player is the player object that stores status for this user
     *@param map is the map object being used in server
     *@param loginMessage is the communication buffer that share between clientSocket thread and GUI thread
     */
    public ClientSocket(String serverName, int port, Player player, Map map, LoginMessage loginMessage) {
        this(serverName, port, player, map);
        this.loginMessage = loginMessage;
    }

    /**
     *Constructor ClientSocket
     *@param serverName is the ip address of server
     *@param port is the port num of server
     *@param player is the player object that stores status for this user
     *@param map is the map object being used in server
     *@param loginMessage is the communication buffer that share between clientSocket thread and GUI thread, transmitting login information
     *@param mapMessage is the communication buffer that share between clientSocket thread and GUI thread, transmitting map
     *@param actionMessage is the communication buffer that share between clientSocket thread and GUI thread, transmitting round actions
     *@param roundMessage  is the communication buffer that share between clientSocket thread and GUI thread, transmitting the result of each round
     */
    public ClientSocket(String serverName, int port, Player player, Map map, LoginMessage loginMessage, MapMessage mapMessage, ActionMessage actionMessage, RoundMessage roundMessage) {
        this(serverName, port, player, map, loginMessage);
        this.mapMessage = mapMessage;
        this.actionMessage = actionMessage;
        this.roundMessage = roundMessage;
    }

    /**
     *the functions that runs the entire logic of clientSocket, also overrides Thread.run()
     */
    public void run() {
        try {
            clientInitilaize();

            placementActionDisplay();

            actionPhase();

            clientSocket.close();
            System.out.println("Client " + player.getName() + " Exit!");
        } catch (ClassNotFoundException e) {
            System.out.println("Unable to transform Message");
        } catch (InterruptedException e) {
            System.out.println("Client: " + player.getName() + ", " + "Waiting fail");
        } catch (ConnectException e) {
            System.out.println("Client: " + player.getName() + ", " + "Server has not been initialized yet");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     *Initialize client socket, object input/output stream, send username/password to server side
     *Update the map, player at client side
     */
    public void clientInitilaize() throws IOException, InterruptedException, ClassNotFoundException {
        Thread.sleep(1000);
        clientSocket = new Socket(serverName, port);

        this.os = new ObjectOutputStream(clientSocket.getOutputStream());
        this.is = new ObjectInputStream(clientSocket.getInputStream());

        String[] info = loginMessage.getInformation();
        os.writeObject(info);
        os.reset();
        player.setName(info[0]);
        player.setPassword(info[1]);

        this.groupID = (Integer)is.readObject();
        player.setPlayerNumber(groupID);
        System.out.println("Client Side: " +player.getName()+"'s group ID is "+player.getPlayerNumber());

        RoundResult roundResult = (RoundResult)is.readObject();
        // TODO: not sure about this
        this.map = roundResult.getMap();
        this.player = roundResult.getPlayer();

        if (map != null) {
            System.out.println("Client Side: " +player.getName()+" receive map from server!");
            mapMessage.setGameMap(map);
            //receInfo[0] = true;
        }
    }

    /**
     *Send the placement actions to server side
     *Update the map client side, update the buffer as well
     */
    public void placementActionDisplay() throws IOException, InterruptedException, ClassNotFoundException {
        List<Action> pm = actionMessage.getActionList();

        os.writeObject(pm);
        os.reset();

        Map newMap = null;
        do {
            System.out.println("Client Side: waiting for map from Server");
        } while ((newMap = (Map)is.readObject())==null);
        String viewRecv = "Client " + player.getName() + " received Map view!";
        os.writeObject(viewRecv);
        os.reset();

        this.map = newMap;
        mapMessage.setGameMap(newMap);
        //receInfo[0] = true;
        //displayView();
    }

    /**
     * Send normal round actions to server side
     * Update the map, player object at client side, send ACK message back to server
     * Update the buffer as well
     * Check if game is over every round (one iteration = one round)
     */
    public void actionPhase() throws IOException, InterruptedException, ClassNotFoundException {
        while (true) {
            os.flush();
            List<Action> actions = actionMessage.getActionList();
            os.writeObject(actions);
            os.reset();

            RoundResult roundResult = null;
            do {
                System.out.println("Client Side: waiting for Round Result from Server");
            } while ((roundResult = (RoundResult) is.readObject())==null);
            String viewRecv = "Client " + player.getName() + " received Round Result for this round!";

            os.writeObject(viewRecv);
            os.reset();

            //updateClientPlayer(roundResult.getPlayer());
            this.map = roundResult.getMap();
            this.player = roundResult.getPlayer();
            roundMessage.setRoundResult(roundResult);
            //mapMessage.setGameMap(map);
            System.out.println("Client: Your food resource is "+player.getFoodResources()+", your tech resource is "+player.getTechResources());
            //displayView();

            String isGameContinue = (String)is.readObject();
            // TODO: inform GUI thread
            if (isGameContinue.equals("gameOver")) {
                String result = endGameMessage();
                System.out.println(result);
                break;
            }
        }
    }

    /**
     * If the game is over, the function will find the winner and return corresponding message
     */
    public String endGameMessage() {
        Iterator<Territory> iter = map.getTerritories();
        Territory ter = iter.next();
        if (ter.getOwner().getName().equals(player.getName())) {
            return "Congratuations ! You win the game";
        } else {
            return "Player "+ter.getOwner().getName()+" wins the game, You lose";
        }
    }
}
