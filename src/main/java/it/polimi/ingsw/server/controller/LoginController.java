package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.Command;
import it.polimi.ingsw.communication.Message;
import it.polimi.ingsw.server.ClientHandler;

public class LoginController {
    /**
     * @param message message containing the command
     * @param client client that received the message
     * @return a new STRING message containing the result of the LOGIN command
     */
    public Message processLogin(Message message, ClientHandler client){

        Message output = new Message("string");

        if(client.getUsername().equals("new client")){
            boolean unique = true;
            for(ClientHandler client1: client.getClients()){
                if (client1.getUsername().equals(message.getArgString())) {
                    unique = false;
                    break;
                }
            }
            if(unique){
                client.setUsername(message.getArgString());
                output.setArgString("Username set");
                client.getClients().add(client);

            } else {
                output.setArgString("Username already in use, try another");
            }

            output.setStandard(true);


        }
        else {
            output.setArgString("Username already set");
            output.setStandard(true);
        }
        return output;
    }

    public Message processLogout(Message request, ClientHandler clientHandler) {
        // TODO: implement
        return null;
    }
}
