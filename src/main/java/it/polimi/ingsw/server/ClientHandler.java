package it.polimi.ingsw.server;

import it.polimi.ingsw.communication.GamePack;
import it.polimi.ingsw.communication.messages.Command;
import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.controller.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

public class ClientHandler implements Runnable{
    private String username = "new client";
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final List<ClientHandler> clients;
    private boolean playerIsOffline = false;
    private Game game = null;
    public boolean isPlayerFirstMove = false;
    private GamePhase currentGamePhase;
    private boolean onePlayerLeft = false;
    private boolean alreadyUpdated = false;
    private static boolean isGameAlreadyStarted = false;

    /**
     *constructor of ClientHandler
     * @param clientSocket socket previously created
     * @param clients connected clients
     */
    public ClientHandler(Socket clientSocket, List<ClientHandler> clients) {
        this.clients = clients;
        initializeClientServerConnection(clientSocket);
        //System.out.println("Connection completed!");
    }

    /**
     *initializes in and out stream
     * @param clientSocket relevant socket
     */
    public void initializeClientServerConnection(Socket clientSocket) {

        this.clientSocket= clientSocket;
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Message request;
        try {
            //while(request.getCommand() != Command.END) { // while the message is not an END type message
            while(true) {
                request = (Message) in.readObject();// read object from input stream and cast it into Message
                if(request.getCommand()!=Command.PING) { // if it's not a PING message
                    // parsing of not PING commands
                    Message response = null;// flush output stream
                    // send through output stream the msg in String form
                    //if ( onePlayerLeft ) { response= new Message("Wait for others to join"); response.setArgString("Wait for others to join");}
                   if ((username.equals("new client"))&&(request.getCommand()!=Command.LOGIN)) { // force login as first command
                        response = new Message("string"); // setup response
                        response.setArgString("Must log in before sending any other command\n" +
                                "type LOGIN <username>");
                    } else {
                        // if user has already logged in
                        switch(request.getCommand()){ // process command
                            case LOGIN -> response = LoginController.processLogin(request, this);
                            case LOGOUT -> response = LoginController.processLogout(this);
                            case START -> response = GameController.start(request, this, clients);
                            case PLAY -> response = PlanningController.play(request, this, currentGamePhase);
                            case MOVE -> response = ActionController.move(request, this, currentGamePhase);
                            case PLACE -> response =  ActionController.place(request, this, currentGamePhase);
                            case EFFECT -> response = GameController.info(request, this);
                            case CHOOSE -> response =  ActionController.choose(request, this, currentGamePhase);
                            case USE -> response = GameController.character(request,this, sameMatchPlayers());
                            case NULL -> response = new Message("NULL");
                        }
                        // end of not PING commands
                    }
                    if(game!=null && !alreadyUpdated){
                        updateStatus();
                    }
                    out.writeObject(response); // send through output stream the msg in String form
                    out.flush();
                    setAlreadyUpdated(false);
                } else {
                    Message pongResponse = new Message("PONG");
                    out.writeObject(pongResponse);
                    out.flush();// send through output stream the msg in String form
                    break;      // break while loop in case of PING
                }

                /*try {// if this reading is removed the server keeps sending the last message to the clients, flooding the network
                    request = (Message) in.readObject(); // try reading another Message object from input stream
                } catch (ClassNotFoundException e) {
                    System.out.println("Unknown object in input stream");
                } catch (EOFException e){
                    //System.out.println("Unknown object in input stream");
                }*/
            }
            //}
        } catch (SocketException e) {
            try {
                clientSocket.close();
                this.setPlayerIsOffline(true);
                LoginController.processLogout(this);
                //clients.remove(this);
                //System.out.println("Client disconnected, socket closed");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    public ObjectInputStream getIn() {
        return in;
    }
    public ObjectOutputStream getOut() {
        return out;
    }
    public List<ClientHandler> getClients() {
        return clients;
    }
    public boolean isAvailable(){
        return game == null;
    }

    /**
     *creates a new list of clientHandler involved in the same match
     * @return list of clientHandler
     */
    public List<ClientHandler> sameMatchPlayers(){
        List<ClientHandler> same = new LinkedList<>();
        for(ClientHandler client: clients){
            if(!client.isPlayerIsOffline() && client.getGame()==this.game){
                same.add(client);
            }
        }
        return same;
    }

    /**
     * packs the game status in a new GamePack then sends the new status to all the playing users
     */
    public void updateStatus(){
        Message status = new Message("status");
        status.setStandard(true);
        GamePack gamePack = new GamePack(game, this);
        try{
            for(ClientHandler clientHandler: sameMatchPlayers()){
                //Send Game status if the client is currently in a game
                gamePack.updateGamePack(game, clientHandler);
                status.setStatus(gamePack);
                try {
                    clientHandler.getOut().writeObject(status);
                    clientHandler.getOut().flush();
                    clientHandler.getOut().reset();
                } catch (IOException e) {
                    System.out.println("A user has logged out");
                }
            }
        }  catch(Exception e){
            System.out.println("users have logged out");
        }
    }

    public void setPlayerFirstMove(boolean playerFirstMove) {
        this.isPlayerFirstMove = playerFirstMove;
    }

    public void setCurrentGamePhase(GamePhase gamePhase) {
        this.currentGamePhase = gamePhase;
    }

    public GamePhase getCurrentGamePhase() {
        return currentGamePhase;
    }

     public boolean isPlayerIsOffline() {
        return playerIsOffline;
    }

      public void setPlayerIsOffline(boolean playerIsOffline) {
        this.playerIsOffline = playerIsOffline;
    }

    public void setAlreadyUpdated(boolean alreadyUpdated) {
        this.alreadyUpdated = alreadyUpdated;
    }

    public void setGameAlreadyStarted(boolean gameAlreadyStarted) {
        isGameAlreadyStarted = gameAlreadyStarted;
    }

    public boolean isGameAlreadyStarted() {
        return isGameAlreadyStarted;
    }
}