package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.messages.Message;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private final String SERVER_IP ;
    private final int SERVER_PORT;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Thread readThread;
    protected static boolean serverWasOffline = true;

    /**
     *Constructor of client, starts a pingThread and keeps reading client requests while checking server is not offline
     * @param args parameters passed by starting Application
     */
    public Client (String[] args) {
        if (args == null || args.length <2 ) { //check right number of arguments
            throw new IllegalArgumentException("To start client you have to set ip and port number");
        }
        SERVER_IP = args[0]; //set the first argument as SERVER_IP
        try {
            SERVER_PORT = Integer.parseInt(args[1]); //set the second argument as SERVER_PORT
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Impossible port");
        }
        PingThread ping = new PingThread(SERVER_IP, SERVER_PORT);
        ping.start(); //start ping thread
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); //open input stream
        try {
            String writtenString;
            while ((writtenString = stdIn.readLine()) != null ) {
                if (ping.isServerReachable()) {
                    if (serverWasOffline) {
                        // used when Client starts before Server
                        createClientServerSocket();
                    }
                    Message request = new Message(writtenString); // parse the string into message
                    if (request.isStandard()){
                        assert out != null;
                        out.writeObject(request); // send Message object through output stream
                        out.flush(); // flush output stream
                    }
                } else {
                    System.out.println("Server isn't reachable, please try again later.");
                    serverWasOffline = true;
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + SERVER_IP);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + SERVER_IP);
        }
    }

    /**
     *creates socket with parameters passed by Client constructor and sets serverWasOffline false
     */
    public void createClientServerSocket() throws IOException {

        socket = new Socket(SERVER_IP, SERVER_PORT); // instance server socket
        out = new ObjectOutputStream(socket.getOutputStream()); // prepare output stream
        in = new ObjectInputStream(socket.getInputStream()); // prepare input stream

        if (readThread != null) {
            readThread.interrupt();
        }

        readThread = new Thread(new ReadThread(in)); // create a new ReadThread thread with the input stream obtained during connection
        readThread.start(); // start ReadThread thread
        serverWasOffline = false;
    }
}