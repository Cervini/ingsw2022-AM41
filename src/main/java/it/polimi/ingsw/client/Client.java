package it.polimi.ingsw.client;
import it.polimi.ingsw.communication.Message;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private static final String server_ip = "127.0.0.1";
    private static final int server_port = 1234;

    public static void main (String[] args) throws IOException {

        try (
                Socket socket = new Socket(server_ip, server_port); // instance server socket
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); // prepare output stream
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // prepare input stream from server
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)) // prepare input stream from terminal

        ) {
            String writtenString;
            while((writtenString = stdIn.readLine()) != null){
                Message msg = new Message(writtenString); // parse the string into message
                out.writeObject(msg); // send Message object through output stream
                out.flush(); // flush output stream
                System.out.println("Server says: " + in.readLine());
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + server_ip);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + server_ip);
            System.exit(1);
        }
    }
}
