package it.polimi.ingsw.client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    private static final String server_ip = "127.0.0.1";
    private static final int server_port = 1234;


    public static void main (String[] args) throws IOException {

        try (
                Socket socket = new Socket(server_ip, server_port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            String writtenString;
            while((writtenString = stdIn.readLine()) != null){
                out.println(writtenString);
                System.out.println("Server says: " + in.readLine());
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + server_ip);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + server_ip);
            System.exit(1);
        }
    }
}
