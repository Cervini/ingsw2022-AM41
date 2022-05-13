package it.polimi.ingsw.communication;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Ping {

    private static final int timeout = 5000;

    public boolean ping(String ipAddress){
        InetAddress inet;
        try{
            inet = InetAddress.getByName(ipAddress);
            return inet.isReachable(timeout);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean ping2(String server_ip, int server_port) throws IOException {
        try (Socket socket = new Socket(server_ip, server_port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            Message pingRequest = new Message("PING");
            out.writeObject(pingRequest);
            out.flush();

            Message pingResponse = (Message) in.readObject();
//           System.out.println("Server says: " + pingResponse);

            return pingResponse.getCommand().equals(Command.PONG);
        } catch (IOException | ClassNotFoundException e){
            return false;
        }
    }
}
