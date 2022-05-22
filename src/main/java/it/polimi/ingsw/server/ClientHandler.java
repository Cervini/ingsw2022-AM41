package it.polimi.ingsw.server;

import it.polimi.ingsw.communication.GamePack;
import it.polimi.ingsw.communication.messages.Command;
import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.controller.*;
import it.polimi.ingsw.server.controller.PhaseController;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;


public class ClientHandler implements Runnable{

    private String username = "new client";
    private final Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final List<ClientHandler> clients;
    private Game game = null;
    public boolean isPlayerFirstMove =false;

    private GamePhase currentGamePhase;

    CommandParser cmdParser = new CommandParser();

    public ClientHandler (Socket clientSocket, List<ClientHandler> clients) {
        this.clients = clients;
        this.clientSocket= clientSocket;
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Connection completed!");
    }

    @Override
    public void run() {
        Message request;
        try {
            while(true) {
                request = (Message) in.readObject();// read object from input stream and cast it into Message
                while(request.getCommand() != Command.END) { // while the message is not an END type message
                    // flush output stream
                    if(request.getCommand()!=Command.PING){ // if it's not a PING message
                        // parsing of not PING commands
                        //System.out.println(username + " said: " + request); // print the received message
                        Message response = null;// flush output stream
                        // send through output stream the msg in String form
                        if((username.equals("new client"))&&(request.getCommand()!=Command.LOGIN)){
                            response = new Message("string");
                            response.setArgString("Must log in before sending any other command\n" +
                                    "type LOGIN <username>");
                        } else {

                            switch(request.getCommand()){
                                case LOGIN -> response = LoginController.processLogin(request, this);
                                case LOGOUT -> response = LoginController.processLogout(this);
                                case START -> response = GameController.start(request, this, clients);


                                case PLAY -> response = PhaseController.play(request, this, currentGamePhase);


                                case PLACE -> response = PhaseController.play(request, this,currentGamePhase);
                                case MOVE -> response = PhaseController.play(request, this,currentGamePhase);
                                case CHOOSE -> response = PhaseController.play(request, this,currentGamePhase);


                                case STATUS -> response = new GameResultsController().getStatus(request, this);
                                case NULL -> response = new Message("NULL");
                            }
                            // end of not PING commands
                        }
                        out.writeObject(response); // send through output stream the msg in String form
                        out.flush();
                        if(game!=null){
                            updateStatus();
                        }
                    } else {
                        Message pongResponse = new Message("PONG");
                        out.writeObject(pongResponse);
                        out.flush();// send through output stream the msg in String form
                    }
                    try {// if this reading is removed the server keeps sending the last message to the clients, flooding the network
                        request = (Message) in.readObject(); // try reading another Message object from input stream
                    } catch (ClassNotFoundException e) {
                        System.out.println("Unknown object in input stream");
                    }
                    catch (EOFException e){
                        //System.out.println("Unknown object in input stream");
                    }
                }
            }
        } catch (SocketException e) {
            try {
                clientSocket.close();
                LoginController.processLogout(this);
                clients.remove(this);
                //System.out.println("Client disconnected, socket closed");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Message response = new Message("game");
    //response.setGame(game);

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

    public List<ClientHandler> sameMatchPlayers(){
        List<ClientHandler> same = new LinkedList<>();
        for(ClientHandler client: clients){
            if(client.getGame()==this.game){
                same.add(client);
            }
        }
        return same;
    }

    public void updateStatus(){
        for(ClientHandler clientHandler: sameMatchPlayers()){
            //Send Game status if the client is currently in a game
            Message status = new Message("status");
            GamePack gamePack = new GamePack(game, clientHandler);
            status.setStatus(gamePack);
            status.setStandard(true);
            try {
                clientHandler.getOut().writeObject(status);
                clientHandler.getOut().flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public boolean getPlayerFirstMove() {
        return isPlayerFirstMove;
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
}
