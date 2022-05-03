package it.polimi.ingsw.server;
import com.google.gson.Gson;

public class CommandParser {

    Gson gson = new Gson();

    public Boolean processCmd(String cmd){
        Map map = gson.fromJson(cmd,Map.class);
        return true;
    }
}
