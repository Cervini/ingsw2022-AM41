package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.Ping;

import java.io.IOException;
import java.net.Socket;

public class PingThread implements Runnable{
    private Thread pingThread;
    private String threadName = "PingThread";
    private String ipAddress;
    private int portNumber;
    Ping ping = new Ping();


    public void ipAddressSet (String ipAddress){
        this.ipAddress = ipAddress;
    }
    public void setPortNumber(int portNumber){
        this.portNumber = portNumber;
    }

    public void run(){
        boolean isReachable = true;
        System.out.println("Running " + threadName);
        try{
            while (isReachable){
                isReachable = ping.ping2(ipAddress, portNumber);
                Thread.sleep(5000);
                System.out.println("Server is reachable");
            }
        }catch (InterruptedException e){
            System.out.println("Thread " + threadName + " interrupted");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server connection lost");
    }

    public void start(){
        System.out.println("Starting " + threadName);
        if(pingThread == null){
            pingThread = new Thread(this, threadName);
            pingThread.start();
        }
    }
}
