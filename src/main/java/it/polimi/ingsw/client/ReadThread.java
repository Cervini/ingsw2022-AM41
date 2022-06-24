package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.messages.Command;
import it.polimi.ingsw.communication.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ReadThread implements Runnable{

    private final ObjectInputStream in; // stream from where the thread reads the objects

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
                if(msg.getCommand() == Command.STATUS){ // if incoming message is a STATUS message
                    msg.getStatus().printPack(); // print the packed game
                }
            } catch (IOException | ClassNotFoundException e) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    //ex.printStackTrace();
                }
            }
            if((msg!=null)&&(msg.getCommand()!=Command.STATUS)){
                // if the message is not null
                System.out.println(msg); // print the content of the message
            }
        }
    }
}
