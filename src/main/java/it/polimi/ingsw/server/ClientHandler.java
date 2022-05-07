package it.polimi.ingsw.server;

import it.polimi.ingsw.communication.Command;
import it.polimi.ingsw.communication.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable{


    private final Socket clientSocket;
    private ObjectInputStream in;
    private PrintWriter out;

    CommandParser cmdParser = new CommandParser();

    public ClientHandler (Socket clientSocket) {

        this.clientSocket= clientSocket;
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new PrintWriter(clientSocket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connection completed!");

    }

    @Override
    public void run() {
            Message msg;
            try {

                while(true) {

                    msg = (Message) in.readObject(); // read object from input stream and cast it into Message

                    while(msg.getCommand() != Command.END) { // while the message is not an END type message
                        System.out.println("received: " + msg); // print the received message
                        out.println(msg); // send through output stream the msg in String form
                        out.flush(); // flush output stream
                        //cmdParser.processCmd(s);
                        try {
                            msg = (Message) in.readObject(); // try reading another Message object from input stream
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
            }
        }  catch (SocketException e){
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        System.out.println("Client disconnected, socket closed");


    }
}
