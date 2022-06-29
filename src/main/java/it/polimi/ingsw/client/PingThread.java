package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.Ping;
import java.io.IOException;

import static it.polimi.ingsw.client.Client.serverWasOffline;


public class PingThread implements Runnable{
    private Thread pingThread;
    private final String threadName = "PingThread";
    private final String ipAddress;
    private final int portNumber;
    private boolean isServerReachable;
    Ping ping = new Ping();

    /**
     * @param ipAddress passed by Client constructor
     * @param portNumber passed by Client constructor
     */
    public PingThread(String ipAddress, int portNumber){
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }

    public void run(){
        isServerReachable = true;
        try{
            while (true){ //loop
                isServerReachable = ping.ping(ipAddress, portNumber);
                Thread.sleep(1000);
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

    /**
     *starts pingThread
     */
    public void start(){
        if(pingThread == null){
            pingThread = new Thread(this, threadName);
            pingThread.start();
        }
    }
    public boolean isServerReachable() {
        return isServerReachable;
    }
}