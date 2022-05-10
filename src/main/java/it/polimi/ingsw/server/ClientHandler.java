package it.polimi.ingsw.server;

import it.polimi.ingsw.communication.Command;
import it.polimi.ingsw.communication.Message;
import it.polimi.ingsw.model.Game;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable{

    private String username = "new client";
    private final Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Game game = null;

    CommandParser cmdParser = new CommandParser();

    public ClientHandler (Socket clientSocket) {
        this.clientSocket= clientSocket;
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connection completed!");
    }

    @Override
    public void run() {
        Message msg;
        try {
            while(true) {
                msg = (Message) in.readObject(); // read object from input stream and cast it into Message
                while(msg.getCommand() != Command.END) { // while the message is not an END type message
                    if(msg.getCommand()!=Command.PING){ // if it's not a PING message
                        // parsing of not PING commands
                        System.out.println(username + " said: " + msg); // print the received message
                        Message response;// flush output stream
                        if((username.equals("new client"))&&(msg.getCommand()!=Command.LOGIN)){
                            response = new Message("string");
                            response.setArgString("Must log in before sending any other command\n" +
                                    "type LOGIN <username>");
                        } else {
                            response = cmdParser.processCmd(msg, this);
                        }
                        out.writeObject(response); // send through output stream the msg in String form
                        out.flush(); // flush output stream
                        // end of not PING commands
                    } else {
                        // TODO ponging
                    }
                    try {
                        msg = (Message) in.readObject(); // try reading another Message object from input stream
                    } catch (ClassNotFoundException e) {
                        System.out.println("Unknown object in input stream");
                    }
                }
            }
        } catch (SocketException e){
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
}
