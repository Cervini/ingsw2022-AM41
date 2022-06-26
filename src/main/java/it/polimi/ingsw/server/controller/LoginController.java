package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.util.List;

import static it.polimi.ingsw.server.controller.GameController.alert;

public class LoginController {

    /**
     * @param message message containing the command
     * @param client  client that received the message
     * @return a new STRING message containing the result of the LOGIN command
     */
    public static Message processLogin(Message message, ClientHandler client) {
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


    public static Message processLogout(ClientHandler client) {
        Message output = new Message("string");

        client.setUsername("new client");
        client.getClients().remove(client);
        output.setArgString("Logout successful");

        if(!client.isAvailable()){
            for(ClientHandler player: client.sameMatchPlayers()){
                player.setGame(null);
                Message alert = new Message("string");
                alert.setArgString("Game ended because one player left");
                try {
                    player.getOut().writeObject(alert);
                    player.getOut().flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
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
