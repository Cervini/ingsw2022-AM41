package it.polimi.ingsw.server;

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
    /**
     * @param args passed by starting Application
     * @return port chosen
     */
    private int getPort(String[] args) {
        int port;
        if (args == null || args.length < 1) throw new IllegalArgumentException("You have to set port number to start server");
        try{
            port = Integer.parseInt(args[0]);
        }catch (Exception e){
            System.out.println("The port value must be a number!");
            System.exit(0);
            port = 0;
        }
        return port;
    }


    /**
     * constructor called by Application
     * @param args passed once Application has started
     */
    public Server(String[] args) {
        String mode = getMode(args);
        if (mode.equals("server")) {
            int port = getPort(args);
            startServer(port);
        }
    }

    private  final ExecutorService pool = Executors.newCachedThreadPool();
    /**
     * accepts new connections and puts clientThread in a pool
     * @param portNumber chosen once Application starts
     */
     void startServer(int portNumber) {
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
        int oldSize = -1;

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
            if(clients.size()!=oldSize){
                System.out.println("Number of connected players: " + clients.size());
                oldSize = clients.size();
            }
        }
        pool.shutdown();
    }
}

