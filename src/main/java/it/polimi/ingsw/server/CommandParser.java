package it.polimi.ingsw.server;
import com.google.gson.Gson;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.communication.Command;
import it.polimi.ingsw.communication.Message;
import it.polimi.ingsw.model.Game;

import java.io.IOException;
import java.util.List;

public class CommandParser {

    public Message processCmd(Message message, ClientHandler client){

        Message output = new Message("string");
        switch(message.getCommand()){
            case LOGIN -> { return processLogin(message, client);}
            case START -> { return processStart(message, client);}
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
                client.getClients().add(client);
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

    /**
     * @param message message containing the command
     * @param client client that received the message
     * @return a new STRING message containing the result of the START command
     */
    public Message processStart(Message message, ClientHandler client){
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

    private int availableClients(List<ClientHandler> clients){
        return (int) clients.stream().filter(ClientHandler::isAvailable).count();
    }

    private void setAsPlaying(int numberOfPlayers, Game game, List<ClientHandler> clients){
        int count = 0;
        for(ClientHandler handler: clients){
            if(handler.isAvailable()){
                handler.setGame(game);
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
}
