package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.messages.Message;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {

    private static final String server_ip = "127.0.0.1";
    private static final int server_port = 1234;

    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static Thread readThread;
    private static boolean serverWasOffline = true;

    public static void main (String[] args) throws IOException {
        PingThread ping = new PingThread(server_ip, server_port);
        ping.start();



        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        try {
            String writtenString;
            while ((writtenString = stdIn.readLine()) != null) {
                if (ping.isServerReachable()) {
                    if (serverWasOffline) {
                        // serve solo quando avviamo il client ma il server non e' ancora raggiungibile
                        createClientServerSocket();
                    }

                    Message request = new Message(writtenString); // parse the string into message
                    if(request.isStandard()){
                        out.writeObject(request); // send Message object through output stream
                        out.flush(); // flush output stream
                    }
                } else {
                    System.out.println("Server isn't reachable, please try again later.");
                    serverWasOffline = true;
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + server_ip);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + server_ip);
            System.exit(1);
        }
    }

    private static void createClientServerSocket() throws IOException {
        socket = new Socket(server_ip, server_port); // instance server socket
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