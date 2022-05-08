package it.polimi.ingsw.server;
import com.google.gson.Gson;
import it.polimi.ingsw.communication.Message;

public class CommandParser {

    Gson gson = new Gson();

    public String processCmd(Message message, ClientHandler client){
        /*
            Map map = gson.fromJson(cmd, Map.class);
            System.out.println(map.get(""));
            Map m = (Map) map.get("");
            int x = (int)m.get("");
            return true;
        */
        String output = "";
        switch(message.getCommand()){
            case LOGIN -> {
                if(client.getUsername().equals("new client")){
                    client.setUsername(message.getArgString());
                    return "username set";
                }
                else
                    return "Username already set";
            }
        }
        return output;
    }
}
