package it.polimi.ingsw.server;

import it.polimi.ingsw.communication.Message;
import it.polimi.ingsw.communication.Type;

import java.io.*;
import java.net.Socket;

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

        while(true) {

            Message msg;
            String s = "";
            try {
                try {
                    msg = (Message) in.readObject();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                while(msg.getType() != Type.END) {
                    System.out.println("recieved: " + msg);
                    // print in output stream the string s
                    out.println(s);
                    out.flush();
                    //cmdParser.processCmd(s);
                    try {
                        msg = (Message) in.readObject();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

}
