package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.Message;
import it.polimi.ingsw.communication.ToTile;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.util.List;

public class GameController {
    
    public Message start(Message request, ClientHandler client) {
        Message output = new Message("string");
        int available = availableClients(client.getClients());//count available clients
        if(available>=4){ // if there are at least 4 available players
            Game game = new Game(4);
            setAsPlaying(4, game, client.getClients());
        } else if (available<2) {
            output.setArgString("Not enough players, wait some time then retry.");
        } else {
            Game game = new Game(available);
            setAsPlaying(available, game, client.getClients());
        }
        return output;
    }

    /**
     * @return number of Clients not participating in a game
     */
    private int availableClients(List<ClientHandler> clients){
        return (int) clients.stream().filter(ClientHandler::isAvailable).count();
    }


    /**
     * @param numberOfPlayers
     * @param game
     * @param clients
     * Set the first 'NumberOfPlayers' available clients as playing the 'game' instance
     */
    private void setAsPlaying(int numberOfPlayers, Game game, List<ClientHandler> clients){
        int count = 0;
        for(ClientHandler handler: clients){
            if(handler.isAvailable()){
                handler.setGame(game);
                game.getPlayers().get(count).setPlayer_id(handler.getUsername());
                Message alert = new Message("string");
                alert.setArgString("Game of " + numberOfPlayers + " started");
                try {
                    handler.getOut().writeObject(alert);
                    handler.getOut().flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                count++;
                if(count>=numberOfPlayers)
                    break;
            }
        }
    }
    private Message processPlay(Message message, ClientHandler client){
        Message output = new Message("string");
        if(message.getArgNum1()>=client.getGame().getPlayer(client.getUsername()).getAssistants().size()){
            output.setArgString("Non existing Assistant, retry");
        } else {
            Assistant played = client.getGame().getPlayer(client.getUsername()).getAssistants().get(message.getArgNum1());
            if(client.getGame().getPlayer(client.getUsername()).getAssistants().size()==1) {
                try {
                    client.getGame().getPlayer(client.getUsername()).playAssistant(played);
                    output.setArgString("Assistant played");
                } catch (Exception e) {
                    output.setArgString("Can't play this Assistant, retry");
                }
            } else {
                if(uniqueAssistant(client, played)){
                    try {
                        client.getGame().getPlayer(client.getUsername()).playAssistant(played);
                        output.setArgString("Assistant played");
                    } catch (Exception e) {
                        output.setArgString("Can't play this Assistant, retry");
                    }
                } else {
                    output.setArgString("Another player has already played this Assistant, try another");
                }
            }
        }
        return output;
    }

    /**
     * @return true if no other player of the same game has already played the same Assistant
     */
    private boolean uniqueAssistant(ClientHandler client, Assistant assistant){
        for(ClientHandler player: client.sameMatchPlayers()){
            if(client.getGame().getPlayer(player.getUsername()).getFace_up_assistant()==assistant)
                return false;
        }
        return true;
    }




}
