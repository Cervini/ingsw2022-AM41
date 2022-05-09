package it.polimi.ingsw.server;
import com.google.gson.Gson;
import it.polimi.ingsw.communication.Message;

public class CommandParser {

    Gson gson = new Gson();

    public Message processCmd(Message message, ClientHandler client){
        /*
            Map map = gson.fromJson(cmd, Map.class);
            System.out.println(map.get(""));
            Map m = (Map) map.get("");
            int x = (int)m.get("");
            return true;
        */
        Message output = new Message("string");
        switch(message.getCommand()){
            case LOGIN -> {
                if(client.getUsername().equals("new client")){
                    client.setUsername(message.getArgString());
                    output.setArgString("Username set");
                    output.setStandard(true);
                }
                else {
                    output.setArgString("Username already set");
                    output.setStandard(true);
                }
                return output;
            }
        }
        return output;
    }
}
