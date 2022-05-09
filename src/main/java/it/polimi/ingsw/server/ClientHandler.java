package it.polimi.ingsw.server;

import it.polimi.ingsw.communication.Command;
import it.polimi.ingsw.communication.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable{

    private String username = "new client";
    private final Socket clientSocket;
    private ObjectInputStream in;
    private PrintWriter pongOut;
    private ObjectOutputStream out;

    CommandParser cmdParser = new CommandParser();

    public ClientHandler (Socket clientSocket) {
        this.clientSocket= clientSocket;
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            pongOut = new PrintWriter(clientSocket.getOutputStream(),true);
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
                    Message response = cmdParser.processCmd(msg,this);
                    if(msg.getCommand()!=Command.PING){
                        System.out.println(username + " said: " + msg); // print the received message
                        out.writeObject(response); // send through output stream the msg in String form
                        out.flush(); // flush output stream
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
}
