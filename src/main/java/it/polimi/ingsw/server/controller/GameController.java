package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.Message;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.ClientHandler;
import java.io.IOException;
import java.util.List;

public class GameController {

    /**
     * @param message message containing the command
     * @param client  client that sent the request to start the game
     * @return a new STRING message containing the result of the START command
     */
    public static Message start(Message message, ClientHandler client) {
        Message output = new Message("string");
        if(client.getGame()!=null){ // if the player is already participating in a game
            output.setArgString("Already playing!");
            return output;
        }
        if((message.getArgNum1()>4)||(message.getArgNum1()<2)){ // if the argument of the message is not an accepted value
            output.setArgString("Impossible number of players.");
            return output;
        }
        int available = availableClients(client.getClients());//count available clients
        if(available>= message.getArgNum1()){ // if there are enough available players
            Game game = new Game(message.getArgNum1()); // create the game
            client.setGame(game);
            game.getPlayers().get(0).setPlayer_id(client.getUsername()); //set the client who sent START command as a player
            output.setArgString("Game of " + available + " started");
            setAsPlaying(message.getArgNum1(), game, client.getClients()); // set the other available client as players
        } else {
            output.setArgString("Not enough players, wait some time then retry.");
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

    /**
     * @param message message containing the command
     * @param client  client that sent the request to start the game
     * @return a new STRING message containing the result of the PLAY command
     */
    public static Message processPlay(Message message, ClientHandler client){
        Message output = new Message("string");
        int range = client.getGame().getPlayer(client.getUsername()).getAssistants().size();
        // System.out.println("Size: " + range + " Arg: " + message.getArgNum1());
        // TODO check condition
        if((message.getArgNum1()-1>range)||(message.getArgNum1()-1<0)){
            output.setArgString("Non existing Assistant, retry");
            return output;
        } else {
            Assistant played = client.getGame().getPlayer(client.getUsername()).getAssistants().get(message.getArgNum1()-1);
            if(client.getGame().getPlayer(client.getUsername()).getAssistants().size()==1) {
                playAssistant(client, played, output); // if the player has only one assistant
            } else {
                if(uniqueAssistant(client, played)){
                    playAssistant(client, played, output);
                } else {
                    if(!checkAllUnique(client)){
                        playAssistant(client, played, output);
                    } else {
                        output.setArgString("Another player has already played this Assistant, try another");
                    }
                }
            }
        }
        return output;
    }

    /**
     * play Assistant
     */
    private static void playAssistant(ClientHandler client, Assistant played, Message output){
        try {
            client.getGame().getPlayer(client.getUsername()).playAssistant(played);
            output.setArgString("Assistant played");
        } catch (Exception e) {
            output.setArgString("Can't play this Assistant, retry");
        }
    }

    /**
     * @return true if no other player of the same game has already played the same Assistant
     */
    private static boolean uniqueAssistant(ClientHandler client, Assistant assistant){
        for(ClientHandler player: client.sameMatchPlayers()){
            if(client.getGame().getPlayer(player.getUsername()).getFace_up_assistant()==assistant){
                System.out.println("got one equal");
                return false;
            }
        }
        return true;
    }

    /**
     * @param client player whose assistant are checked
     * @return false if the player has no unique Assistants
     */
    private static boolean checkAllUnique(ClientHandler client){
        Game game = client.getGame();
        boolean check = false;
        for(Assistant assistant: game.getPlayer(client.getUsername()).getAssistants()){
            if(uniqueAssistant(client, assistant))
                check = true;
        }
        return check;
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
