package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.Message;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.ClientHandler;
import java.io.IOException;
import java.util.List;

public class GameController {

    /**
     * @param client client that sent the request to start the game
     * @return a new STRING message containing the result of the START command
     */
    public static Message start(ClientHandler client) {
        Message output = new Message("string");
        if(client.getGame()!=null){
            output.setArgString("Already playing!");
            return output;
        }
        int available = availableClients(client.getClients());//count available clients
        if(available>=4){ // if there are at least 4 available players
            Game game = new Game(4);
            client.setGame(game);
            game.getPlayers().get(0).setPlayer_id(client.getUsername()); //set the player who sent START command as a player
            output.setArgString("Game of " + 4 + " started");
            setAsPlaying(4, game, client.getClients());
        } else if (available<2) {
            output.setArgString("Not enough players, wait some time then retry.");
        } else {
            Game game = new Game(available);
            client.setGame(game);
            game.getPlayers().get(0).setPlayer_id(client.getUsername()); //set the player who sent START command as a player
            output.setArgString("Game of " + available + " started");
            setAsPlaying(available, game, client.getClients());
        }
        return output;
    }

    /**
     * @return number of Clients not participating in a game
     */
    private static int availableClients(List<ClientHandler> clients){
        return (int) clients.stream().filter(ClientHandler::isAvailable).count();
    }


    /**
     * @param numberOfPlayers
     * @param game
     * @param clients
     * Set the first 'NumberOfPlayers' available clients as playing the 'game' instance
     */
    private static void setAsPlaying(int numberOfPlayers, Game game, List<ClientHandler> clients){
        int count = 1;
        for(ClientHandler handler: clients){
            if(handler.isAvailable()){
                handler.setGame(game);
                game.getPlayers().get(count).setPlayer_id(handler.getUsername());
                alert(handler, "Game of " + numberOfPlayers + " started");
                count++;
                if(count>=numberOfPlayers)
                    break;
            }
        }
    }

    /**
     * sends a STRING message to client
     * @param client recipient of the message
     * @param message string displayed
     */
    private static void alert(ClientHandler client, String message){
        Message alert = new Message("string");
        alert.setArgString(message);
        try {
            client.getOut().writeObject(alert);
            client.getOut().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Message processPlay(Message message, ClientHandler client){
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
    private static boolean uniqueAssistant(ClientHandler client, Assistant assistant){
        for(ClientHandler player: client.sameMatchPlayers()){
            if(client.getGame().getPlayer(player.getUsername()).getFace_up_assistant()==assistant)
                return false;
        }
        return true;
    }

    public static Message processChoose(Message message, ClientHandler client){
        Message output = new Message("string");
        Game game = client.getGame();
        if(game.getClouds().get(message.getArgNum1()).getStudents().size()==0){
            output.setArgString("Can't choose this cloud, it has been already chosen by another player");
            return output;
        } else {
            try {
                game.chooseCloud(game.getClouds().get(message.getArgNum1()), game.getPlayer(client.getUsername()));
                output.setArgString("Students move to your School Board");
            } catch (Exception e) {
                output.setArgString("Impossible move, try another");
            }
        }
        return output;
    }




}
