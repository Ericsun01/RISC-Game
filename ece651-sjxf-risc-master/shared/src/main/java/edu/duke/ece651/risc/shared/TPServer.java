package edu.duke.ece651.risc.shared;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *The class allocates threadPool's thread to handle I/O with client
 */
public class TPServer extends Thread{
    private int serverPort;
    private ServerSocket serverSocket;
    private HashMap<Socket, Integer> operationSocketsID;
    public ExecutorService threadPool;
    private int numOfhuman;
    private int numOfAI;
    private int numPerGame;
    private Map map;
    private Game game;

    /**
     *Constructor TPServer
     *@param port port number that listens to client
     *@param numPerGame is the number of players for this game
     *@param game is the game used for playing
     */
    public TPServer(int port, int numPerGame, Game game) {
        this.serverPort = port;
        this.serverSocket = null;
        this.operationSocketsID = new HashMap<>();
        this.threadPool = Executors.newFixedThreadPool(5);
        this.numPerGame = numPerGame;
        this.map = game.getMap();
        this.game = game;
    }

    /**
     *Constructor TPServer
     *@param port port number that listens to client
     *@param numOfhuman is the number of human players for this game
     *@param numOfAI is the number of computer players for this game
     *@param game is the game used for playing
     */
    public TPServer(int port, int numOfhuman, int numOfAI, Game game) {
        this.serverPort = port;
        this.serverSocket = null;
        this.operationSocketsID = new HashMap<>();
        this.threadPool = Executors.newFixedThreadPool(5);
        this.numOfhuman = numOfhuman;
        this.numOfAI = numOfAI;
        this.numPerGame = numOfAI+numOfhuman;
        this.map = game.getMap();
        this.game = game;
    }


    /**
     *the functions that runs the entire logic of TPServer, also overrides Thread.run()
     */
    public void run() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);

            if (numOfAI <= 0) {
                PVPGame();
            } else {
                PVEGame();
            }
            // gather the players from serverRunnable!
            this.threadPool.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * If all players are human, play PVP
     * generate corresponding player objects, will be injected into game object as well as each serverRunnable
     * allocates threadPool thread to run each serverRunnable
     * Then runs the game logic
     */
    public void PVPGame() throws IOException, InterruptedException{
        System.out.println("TPServer: we are playing PVP Game");
        List<Player> playerList = new ArrayList<>();

        for (int playerIndex = 0; playerIndex < numPerGame; playerIndex++) {
            Socket opeartionSocket = this.serverSocket.accept();
            operationSocketsID.put(opeartionSocket, playerIndex+1);
            Player player = new Player("Player", 0, 10, 100, 100);
            playerList.add(player);
            game.addPlayer(player);
        }

        // Assign threads in threadPool to handle I/O with client side player
        int playerIndex = 0;
        for (java.util.Map.Entry<Socket, Integer> entry: operationSocketsID.entrySet()) {
            ServerRunnable sr = new ServerRunnable(entry.getKey(), entry.getValue(), map, playerList.get(playerIndex++), game);
            this.threadPool.execute(sr);
        }

        game.recordLoginInfo();

        for (HashMap.Entry<String, String> entry: game.getLoginInfo().entrySet()) {
            System.out.println("Player's username is "+entry.getKey()+", the password is "+entry.getValue());
        }

        System.out.println("TPServer: now, do Placement Actions");
        game.doPlacementRound();
        game.gamePlay();
    }

    /**
     * If there are AI, play PVE
     * generate corresponding player objects, will be injected into game object as well as each serverRunnable
     * For AI, generates and execute functions of computerPlayer at server side directly
     * allocates threadPool thread to run each serverRunnable
     * Then runs the game logic
     */
    public void PVEGame() throws IOException, InterruptedException {
        List<Player> playerList = new ArrayList<>();

        for (int playerIndex = 0; playerIndex < numOfhuman; playerIndex++) {
            Socket opeartionSocket = this.serverSocket.accept();
            operationSocketsID.put(opeartionSocket, playerIndex+1);
            Player player = new Player("Player", 0, 10, 100, 100);
            playerList.add(player);
            game.addPlayer(player);
        }

        List<String> nameOfAIs = new ArrayList<>();
        for (int i=1; i<=numOfAI; i++) {
            String name = "Mark "+i;
            nameOfAIs.add(name);
        }

        int AINameIndex = 0;
        int AIGroupID = numOfhuman+1;
        while (AIGroupID <= numPerGame) {
            Player player = new Player(nameOfAIs.get(AINameIndex++), AIGroupID, 10, 100, 100, true);
            game.addPlayer(player);
            ComputerPlayer computerPlayer = new ComputerPlayer(map, game, player, AIGroupID++);
            game.addComputerPlayer(computerPlayer);
        }

        // Assign threads in threadPool to handle I/O with client side player
        int playerIndex = 0;
        for (java.util.Map.Entry<Socket, Integer> entry: operationSocketsID.entrySet()) {
            ServerRunnable sr = new ServerRunnable(entry.getKey(), entry.getValue(), map, playerList.get(playerIndex++), game);
            this.threadPool.execute(sr);
        }

        game.recordLoginInfo();
        game.computerPlayerInitialize();

        for (HashMap.Entry<String, String> entry: game.getLoginInfo().entrySet()) {
            System.out.println("Player's username is "+entry.getKey()+", the password is "+entry.getValue());
        }

        game.doPlacementRound();
        game.gamePlay();
    }

}

