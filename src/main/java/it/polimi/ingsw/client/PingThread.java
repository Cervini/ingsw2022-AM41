package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.Ping;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static it.polimi.ingsw.client.Client.serverWasOffline;


public class PingThread implements Runnable{
    private Thread pingThread;
    private final String threadName = "PingThread";
    private String ipAddress;
    private int portNumber;
    private boolean isServerReachable;
    Ping ping = new Ping();

    private ObjectOutputStream out;

    private ObjectInputStream in;


    public PingThread(String ipAddress, int portNumber){
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }



    public void ipAddressSet (String ipAddress){
        this.ipAddress = ipAddress;
    }
    public void setPortNumber(int portNumber){
        this.portNumber = portNumber;
    }

    public void run(){
        isServerReachable = true;
        //System.out.println("Running " + threadName);
        try{
            while (true){
                isServerReachable = ping.ping(ipAddress, portNumber);
                Thread.sleep(2000);
                 //System.out.println("Server is reachable "+isServerReachable );
                if (!isServerReachable) {
                    System.out.println("Server is not available, please wait..");
                    serverWasOffline = true;
                }
            }
        }catch (InterruptedException e){
            System.out.println("Thread " + threadName + " interrupted");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server connection lost");
    }

    public void start(){
        // System.out.println("Starting " + threadName);
        if(pingThread == null){
            pingThread = new Thread(this, threadName);
            pingThread.start();
        }
    }
    public boolean isServerReachable() {
        return isServerReachable;
    }
}