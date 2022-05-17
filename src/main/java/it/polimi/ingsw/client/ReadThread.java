package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.Command;
import it.polimi.ingsw.communication.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.UnknownHostException;

public class ReadThread implements Runnable{

    private ObjectInputStream in;

    public ReadThread(ObjectInputStream in){
        this.in = in;
    }

    @Override
    public void run() {
        while(true){
            Message msg;
            try {
                msg = (Message) in.readObject();
                if(msg.getCommand() == Command.PONG){

                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(msg!=null)
                System.out.println("Server says: " + msg);
        }
    }


}
