package it.polimi.ingsw.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static it.polimi.ingsw.server.ServerSocket.openServerSocket;
import static java.lang.System.exit;

public class Server {


    static String getMode(String[] args) {
        return "server";
    }

    static int getPort(String[] args) {
        return 1234;
    }


    public static void main(String[] args) {
        String mode = getMode(args);

        if (mode == "server") {
            int port = getPort(args);
            startServer(port);
        }

    }


    private static ExecutorService pool = Executors.newCachedThreadPool();

    static void startServer(int portNumber) {

        System.out.println("starting server on port " + portNumber);

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println("cannot start server on port " + portNumber);
            return;
        }

        System.out.println("Accepting..");

        Socket clientSocket = null;

        System.out.println("Accepted");

        while(true){
            try{
                clientSocket = serverSocket.accept();
                ClientHandler clientThread = new ClientHandler(clientSocket);
                pool.execute(clientThread);
            } catch (IOException e) {
                System.out.println("cannot accept port " + portNumber);
                break;
            }
        }
        pool.shutdown();
    }
}