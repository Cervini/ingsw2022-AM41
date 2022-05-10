package it.polimi.ingsw.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
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
        try(Socket socket = new Socket(server_ip, server_port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            Message ping = new Message("PING");
            while(true){
                out.writeObject(ping);
                out.flush();
            }
        }catch (IOException e){
            return false;
        }
    }
}
