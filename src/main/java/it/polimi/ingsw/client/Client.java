package it.polimi.ingsw.client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.BufferOverflowException;

public class Client {

    private static final String server_ip = "127.0.0.1";
    private static final int server_port = 1234;

    public static void main (String[] args) throws IOException {

        Socket socket = new Socket(server_ip,server_port);

        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);

        while(true) {

            String command = in.readLine();
            out.println(command);
            String serverResponse = input.readLine();
            System.out.println("Server says: " + serverResponse);

        }

    }
}
