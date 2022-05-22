package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.messages.Command;
import it.polimi.ingsw.communication.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ReadThread implements Runnable{

    private ObjectInputStream in; // stream from where the thread reads the objects

    /**
     * @param in inherited stream from where the thread reads the objects
     *           (the connection must be built by the father of the thread)
     */
    public ReadThread(ObjectInputStream in){
        this.in = in;
    }

    @Override
    public void run() {
        while(true){ // infinite cycle
            Message msg = null; // instantiate new message
            try {
                msg = (Message) in.readObject(); // try reading a Message object from in
                if(msg.getCommand() == Command.STATUS){
                    msg.getStatus().printPack();
                }
                if(msg.getCommand() == Command.PONG){
                    // TODO reaction to PONG
                }
            } catch (IOException | ClassNotFoundException e) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                //throw new RuntimeException(e);
            }
            if(msg!=null) // if the message is not null
                System.out.println("Server says: " + msg); // print the content of the message
        }
    }



}
