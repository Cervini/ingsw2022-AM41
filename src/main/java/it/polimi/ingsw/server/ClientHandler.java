package it.polimi.ingsw.server;

import it.polimi.ingsw.communication.Command;
import it.polimi.ingsw.communication.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.controller.GameResultsController;
import it.polimi.ingsw.server.controller.LoginController;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable{

    private String username = "new client";
    private final Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final List<ClientHandler> clients;
    private Game game = null;

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
                request = (Message) in.readObject(); // read object from input stream and cast it into Message
                while(request.getCommand() != Command.END) { // while the message is not an END type message
                    if(request.getCommand()!=Command.PING){ // if it's not a PING message
                        // parsing of not PING commands
                        //System.out.println(username + " said: " + request); // print the received message
                        Message response = null;// flush output stream
                        if((username.equals("new client"))&&(request.getCommand()!=Command.LOGIN)){

                            response = new Message("string");
                            response.setArgString("Must log in before sending any other command\n" +
                                    "type LOGIN <username>");
                            out.writeObject(response); // send through output stream the msg in String form
                            out.flush();
                        }
                        switch(request.getCommand()){
                            case LOGIN -> response = new LoginController().processLogin(request, this);
                            case END -> response = new LoginController().processLogout(request, this);
                            case START -> response = new GameController().start(request, this);
                            case PLACE -> response = new GameController().place(request, this);
                            case MOVE -> response = new GameController().move(request, this);
                            case GAMESTATUS -> response = new GameResultsController().getStatus(request, this);
                            case NULL -> response = new Message("NULL");
                        }



                        out.writeObject(response); // send through output stream the msg in String form
                        out.flush(); // flush output stream
                        // end of not PING commands

                    } else {
                        Message pongResponse = new Message("PONG");
                        out.writeObject(pongResponse); // send through output stream the msg in String form
                        out.flush(); // flush output stream
                    }
//                    try {
//                        msg = (Message) in.readObject(); // try reading another Message object from input stream
//                    } catch (ClassNotFoundException e) {
//                        System.out.println("Unknown object in input stream");
//                    }
                }
            }
        } catch (SocketException e) {

            try {
                clientSocket.close();
                System.out.println("Client disconnected, socket closed");
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

    public List<ClientHandler> sameMatchPlayers(){
        return clients.stream().filter(ClientHandler -> getGame()==game).collect(Collectors.toList());
    }


}
