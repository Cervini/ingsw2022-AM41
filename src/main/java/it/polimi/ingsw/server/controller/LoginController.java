package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import static it.polimi.ingsw.server.controller.GameController.alert;

public class LoginController {

    /**
     * @param message message containing the command
     * @param client  client that received the message
     * @return a new STRING message containing the result of the LOGIN command
     */
    public static Message processLogin(Message message, ClientHandler client) {
        Message output = new Message("string");// set up output message

        if(client.getUsername().equals("new client")){//each new client's username is set to "new client"
            boolean unique = true;
            for(ClientHandler client1: client.getClients()){
                if (client1.getUsername().equals(message.getArgString())) {//checks whether there is another player who has the same username
                    unique = false;
                    break;
                }
            }
            if(unique){
                client.setUsername(message.getArgString());//sets client's username
                output.setArgString("Username set");//sets server response
                client.getClients().add(client);//add this client to clients list
            } else {
                output.setArgString("Username already in use, try another");
            }

            output.setStandard(true);
        }
        else {
            output.setArgString("Username already set");//client has already set an username
            output.setStandard(true);
        }
        return output;
    }

    /**
     * @param client who has logged out
     * @return a new STRING message containing the result of the LOGOUT command
     */
    public static Message processLogout(ClientHandler client) {
        Message output = new Message("string");// set up output message

        client.setUsername("new client");//client's username set to default
        output.setArgString("Logout successful");//sets server response

        try{
            if(!client.isAvailable()){
                for(ClientHandler player: client.sameMatchPlayers()){//if one player logs out the match ends
                    player.setGame(null);//other players' game is set null
                    Message alert = new Message("string");// set up output message
                    alert.setArgString("Game ended because one player left");//notifies remaining players
                    try {
                        player.getOut().writeObject(alert);
                        player.getOut().flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
            client.getClients().remove(client);//removes client from clients list
        } catch(Exception e){
            System.out.println("All users have logged out");
        }


        return output;
        }


    /*public static void welcomeBack(ClientHandler knownClientHandler, ClientHandler client, Message message){
        client.setUsername(message.getArgString());
        client.setGame(knownClientHandler.getGame());
        client.setCurrentGamePhase(knownClientHandler.getCurrentGamePhase());
        client.getClients().remove(knownClientHandler);
        client.getClients().add(client);
        client.getClients().remove(knownClientHandler);

        List<ClientHandler> otherPlayers = client.sameMatchPlayers();
        otherPlayers.remove(client);
        boolean isPlanningPhase = otherPlayers
                .stream()
                .anyMatch(cl -> cl.getGame().getPlayer(cl.getUsername()).getFace_up_assistant() != null);
        if (!isPlanningPhase) {

        }
        else {

        }

    }*/



}
