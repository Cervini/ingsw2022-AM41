package it.polimi.ingsw.server;
import com.google.gson.Gson;

public class CommandParser {

    Gson gson = new Gson();

    public Boolean processCmd(String cmd){

        Map map = gson.fromJson(cmd, Map.class);
        System.out.println(map.get(""));
        Map m = (Map) map.get("");
        int x = (int)m.get("");
        return true;

    }
}
