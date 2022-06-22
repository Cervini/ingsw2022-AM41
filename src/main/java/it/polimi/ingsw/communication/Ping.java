package it.polimi.ingsw.communication;

import it.polimi.ingsw.communication.messages.Command;
import it.polimi.ingsw.communication.messages.Message;

import java.io.*;
import java.net.Socket;

public class Ping {

    private static final int timeout = 5000;

    public boolean ping(String server_ip, int server_port) throws IOException {
        try (Socket socket = new Socket(server_ip, server_port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); // prepare output stream
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            Message pingRequest = new Message("PING");
            out.writeObject(pingRequest);
            out.flush();
            Message pingResponse = (Message) in.readObject();
            in.close();
            out.close();
            socket.close();
            return pingResponse.getCommand().equals(Command.PONG);
        } catch (IOException | ClassNotFoundException e){
            //System.out.println("Client exception: " + e);
            return false;
        }
    }
}
