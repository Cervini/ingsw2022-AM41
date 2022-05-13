package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.Message;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import static java.lang.System.out;

public class Client{

    private static final String server_ip = "127.0.0.1";
    private static final int server_port = 1234;

    public static void main (String[] args) throws IOException {
        PingThread ping = new PingThread(server_ip, server_port);
        ping.start();

        try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            String writtenString;

            while ((writtenString = stdIn.readLine()) != null) {
                if (ping.isServerReachable()) {

                    try (
                            Socket socket = new Socket(server_ip, server_port); // instance server socket
                            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); // prepare output stream
                            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) // prepare input stream from terminal
                    {
                        //Thread readThread = new Thread(new ReadThread(in));
                        //readThread.start();
                        Message request = new Message(writtenString); // parse the string into message
                        out.writeObject(request); // send Message object through output stream
                        out.flush(); // flush output stream

                        Message response = (Message) in.readObject();
                        System.out.println("Server says: " + response);

                    } catch (UnknownHostException e) {
                        System.err.println("Don't know about host " + server_ip);
                        System.exit(1);
                    } catch (IOException e) {
                        System.err.println("Couldn't get I/O for the connection to " + server_ip);
                        System.exit(1);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Server isn't reachable, please try again later.");
                }
            }

        } catch (IOException ex) {
            System.err.println("Couldn't get I/O for the connection to " + server_ip);
            System.exit(1);
        }
    }
}