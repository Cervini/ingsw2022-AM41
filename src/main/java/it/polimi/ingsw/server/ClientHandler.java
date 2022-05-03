package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.Professor;
import it.polimi.ingsw.model.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.System.out;

public class ClientHandler implements Runnable{


    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    CommandParser cmdParser = new CommandParser();

    public ClientHandler (Socket clientSocket) {

        this.clientSocket= clientSocket;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        while (true) {

            String s = "";
            try {
                    while ((s = in.readLine()) != null) {

                        out.println("this message is sent to the client");
                        System.out.println("this prints what client has written:  " + s);
                        //cmdParser.processCmd(s);

                    }

            } catch(IOException e) {
                    e.printStackTrace();
            }
        }
    }

}
