package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.messages.Message;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
    private  String SERVER_IP ;
    private  int SERVER_PORT;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Thread readThread;
    protected static boolean serverWasOffline = true;
    private String host;


    public Client (String[] args) {
        if (args == null || args.length <2 ){
            throw new IllegalArgumentException("To start client you have to set ip and port number");
        }
        SERVER_IP = args[0];
        SERVER_PORT = Integer.parseInt(args[1]);
        PingThread ping = new PingThread(SERVER_IP, SERVER_PORT);
        ping.start();
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
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