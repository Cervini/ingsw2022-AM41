package it.polimi.ingsw.server;

import java.io.IOException;

public class ServerSocket {

    public static java.net.ServerSocket openServerSocket(int portNumber) {
        java.net.ServerSocket serverSocket = null;
        try {
            serverSocket = new java.net.ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return serverSocket;
    }
}
