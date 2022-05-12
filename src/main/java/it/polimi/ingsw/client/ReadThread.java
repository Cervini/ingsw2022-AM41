package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.Message;

import java.io.IOException;
import java.io.ObjectInputStream;

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
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(msg!=null)
                System.out.println("Server says: " + msg);
        }
    }


}
