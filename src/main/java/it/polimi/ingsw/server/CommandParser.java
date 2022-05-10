package it.polimi.ingsw.server;
import com.google.gson.Gson;
import it.polimi.ingsw.communication.Command;
import it.polimi.ingsw.communication.Message;

public class CommandParser {

    public Message processCmd(Message message, ClientHandler client){

        Message output = new Message("string");
        switch(message.getCommand()){
            case LOGIN -> { return processLogin(message, client);}
        }
        return output;
    }

    /**
     * @param client whose state is checked
     * @return true if the client is logged in, false if not
     */
    public Boolean isLoggedIn(ClientHandler client){
        return !client.getUsername().equals("new client");
    }

    /**
     * @param message message containing the command
     * @param client client that received the message
     * @return a new STRING message containing the result of the LOGIN command
     */
    public Message processLogin(Message message, ClientHandler client){
        Message output = new Message("string");
        if(message.getCommand()== Command.LOGIN){
            if(client.getUsername().equals("new client")){
                client.setUsername(message.getArgString());
                output.setArgString("Username set");
                output.setStandard(true);
            }
            else {
                output.setArgString("Username already set");
                output.setStandard(true);
            }
        } else {
            output.setArgString("Unexpected command format, please retry");
            output.setStandard(true);
        }
        return output;
    }


}
