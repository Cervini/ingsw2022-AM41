package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.Server;
import java.util.Arrays;

/**
 * @author ludov
 * @Date  23/06/2022
 */

public class Application {

    public static void main(String[] args) {
        if (args == null || args.length < 2){
            try{
                System.out.println("""
                        Wrong parameters!
                        In order to start the server please type "server [port number]"
                        \tex. java -jar [file path] server 1234
                        In order to start the client type "client [server ip] [port]"
                        \tex. java -jar [file path] client 127.28.240.1 1234
                        """);
                return;
            }catch (Exception e){
                throw new IllegalArgumentException("You have to specify application mode (client or server) and all mandatory parameters");
            }
        }
        String mode = args[0];
        if(!mode.equals("server") && !mode.equals("client")) throw new IllegalArgumentException("Wrong application mode");
        switch(mode){
            case("server")-> new Server(Arrays.copyOfRange(args,1,args.length));
            case("client")-> new Client(Arrays.copyOfRange(args,1,args.length));
        }
    }
}
