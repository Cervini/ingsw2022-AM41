package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.Server;

import java.io.ObjectStreamException;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author ludov
 * @Date 23/06/2022
 */

public class Application {
    public static void main(String[] args) {
        if (args == null || args.length < 2) throw new IllegalArgumentException("You have to specify application mode (client or server) and all mandatory parameters");
        String mode = args[0];
        if(!mode.equals("server") && !mode.equals("client")) throw new IllegalArgumentException("Wrong application mode");
        switch(mode){
            case("server")-> new Server(Arrays.copyOfRange(args,1,args.length));
            case("client")-> new Client(Arrays.copyOfRange(args,1,args.length));
        }
    }
}
