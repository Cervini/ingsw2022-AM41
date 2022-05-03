package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static it.polimi.ingsw.server.ServerSocket.openServerSocket;
import static java.lang.System.exit;

public class Server  {


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

    static boolean startServer(int portNumber) {

        System.out.println("starting server on port " + portNumber);

        ServerSocket serverSocket = openServerSocket(portNumber);

        if (serverSocket == null) {
            System.out.println("cannot start server on port " + portNumber);
            exit(-1);
        }

        while (true) {

            Socket clientSocket = null;

            System.out.println("Accepting..");

            try {
                clientSocket = serverSocket.accept();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("cannot accept port " + portNumber);
                exit(-1);
            }

            System.out.println("Accepted");

            ClientHandler clientThread = new ClientHandler(clientSocket);
            pool.execute(clientThread);


        }


    }
}