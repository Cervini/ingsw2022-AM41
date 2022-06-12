package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.ClientHandler;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.Collections.swap;

public class GameController extends BaseController {

    /**
     * @param message message containing the command
     * @param client  client that sent the request to start the game
     * @return a new STRING message containing the result of the START command
     */
    public static Message start(Message message, ClientHandler client, List clients) {
        Message output = new Message("string");
        if (client.getGame() != null) { // if the player is already participating in a game
            output.setArgString("Already playing!");
            return output;
        }
        if ((message.getArgNum1() > 4) || (message.getArgNum1() < 2)) { // if the argument of the message is not an accepted value
            output.setArgString("Impossible number of players.");
            return output;
        }
        int available = availableClients(client.getClients());//count available clients
        if (available >= message.getArgNum1()) { // if there are enough available players
            Game game = new Game(message.getArgNum1()); // create the game
            client.setGame(game);
            game.getPlayers().get(0).setPlayer_id(client.getUsername()); //set the client who sent START command as a player
            output.setArgString("Game of " + available + " started");
            setAsPlaying(message.getArgNum1(), game, client.getClients()); // set the other available client as players

            Collections.shuffle(client.sameMatchPlayers());

            ClientHandler first_player = client.sameMatchPlayers().get(0);//during the first round the first player is randomly chosen
            first_player.isPlayerFirstMove = true;

            List<ClientHandler> sameMatchPlayers = first_player.sameMatchPlayers();
            GamePhase gamePhase = new PlanningPhase(game, sameMatchPlayers);
            setGamePhaseForAllPlayers(sameMatchPlayers, gamePhase); //all players have the same GamePhase attribute

            Player firstPlayer = game.getPlayer(first_player.getUsername());
            swap(game.getPlayers(), game.getPlayers().indexOf(firstPlayer), 0); //first_player is now in the first position
            //puts each player username in a list
            List <String> turnOrder = sameMatchPlayers.stream().map(ClientHandler::getUsername).toList();
            //converts list of strings to a string
            String turns = turnOrder.stream().map(Object::toString).collect(Collectors.joining(","));
            //sends to client turns order
            for (ClientHandler handler : first_player.sameMatchPlayers()) {
                alert(handler, "Turns order: "+ turns);
            }
        } else {
            output.setArgString("Not enough players, wait some time then retry.");
        }
        return output;
    }

    /**
     * @return number of Clients not participating in a game
     */
    private static int availableClients(List<ClientHandler> clients) {
        return (int) clients.stream().filter(ClientHandler::isAvailable).count();
    }


    /**
     * @param numberOfPlayers
     * @param game
     * @param clients         Set the first 'NumberOfPlayers' available clients as playing the 'game' instance
     */
    private static void setAsPlaying(int numberOfPlayers, Game game, List<ClientHandler> clients) {
        int count = 1;
        for (ClientHandler handler : clients) {
            if (handler.isAvailable()) {
                handler.setGame(game);
                game.getPlayers().get(count).setPlayer_id(handler.getUsername());
                alert(handler, "Game of " + numberOfPlayers + " started");
                count++;
                if (count >= numberOfPlayers)
                    break;
            }
        }
    }

    /**
     * sends a STRING message to client
     *
     * @param client  recipient of the message
     * @param message string displayed
     */
    public static void alert(ClientHandler client, String message) {
        Message alert = new Message("string");
        alert.setArgString(message);
        try {
            client.getOut().writeObject(alert);
            client.getOut().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //TODO: THIS V
    public static Message info(Message request, ClientHandler clientHandler) {
        Message output = new Message("string");
        if (clientHandler.getGame() == null) { // if the player is already participating in a game
            output.setArgString("Not playing yet!");
            return output;
        } else {
            // set as string argument the character description
            output.setArgString(clientHandler.getGame().getCharacters().get(request.getArgNum1()).getDescription());
        }
        return output;
    }
}
