package it.polimi.ingsw.server;

import it.polimi.ingsw.graphics.Main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static java.lang.System.exit;

public class Server {

    static String getMode(String[] args) {
        return "server";
    }

    static int getPort(String[] args) {
        if (args == null || args.length < 1) throw new IllegalArgumentException("You have to set port number to start server");
        return Integer.parseInt(args[0]);
    }

    public static void main(String[] args) {

        String mode = getMode(args);
        if (mode == "server") {
            int port = getPort(args);
            startServer(port);
        }
    }

    private static final ExecutorService pool = Executors.newCachedThreadPool();

    static void startServer(int portNumber) {
        System.out.println("starting server on port " + portNumber);
        ServerSocket serverSocket = null;
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("Server opened at "+ inetAddress.getHostAddress());
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println("cannot start server on port " + portNumber);
            exit(-1);
        }
        List<ClientHandler> clients = new LinkedList<>();
        System.out.println("Accepting..");

        while(true){
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("cannot accept port " + portNumber);
                break;
            }
            ClientHandler clientThread = new ClientHandler(clientSocket, clients);
            pool.execute(clientThread);
            System.out.println("Number of connected players: " + clients.size());
        }
        pool.shutdown();
    }
}

