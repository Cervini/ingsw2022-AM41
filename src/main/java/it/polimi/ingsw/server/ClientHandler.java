package it.polimi.ingsw.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler implements Runnable{


    private Socket clientSocket;

    private BufferedReader in;

    CommandParser cmdParser = new CommandParser();


    public ClientHandler (Socket clientSocket) {

        this.clientSocket= clientSocket;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }


        String s = "";
        try {
            while ((s = in.readLine()) != null) {

                System.out.println(s);
                //cmdParser.processCmd(s);

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }



    }
}
