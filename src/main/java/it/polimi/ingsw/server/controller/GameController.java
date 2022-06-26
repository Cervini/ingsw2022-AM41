package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.server.ClientHandler;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


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

            Random randomIndex = new Random();
            ClientHandler first_player = client.sameMatchPlayers().get(randomIndex.nextInt(client.sameMatchPlayers().size()));//during the first round the first player is randomly chosen
            Collections.swap(client.getClients(),0,client.getClients().indexOf(first_player));
            first_player.isPlayerFirstMove = true;
            List<ClientHandler> sameMatchPlayers = first_player.sameMatchPlayers();
            GamePhase gamePhase = new PlanningPhase(game, sameMatchPlayers);
            setGamePhaseForAllPlayers(sameMatchPlayers, gamePhase); //all players have the same GamePhase attribute
            client.setGame(game);
            game.getPlayers().get(0).setPlayer_id(client.getUsername()); //set the client who sent START command as a player
            output.setArgString("Game of " + available + " started");
            setAsPlaying(message.getArgNum1(), game, client.getClients()); // set the other available client as players
            //puts each player username in a list
            List <String> turnOrder = client.sameMatchPlayers().stream().map(ClientHandler::getUsername).toList();
            //converts list of strings to a string
            String turns = turnOrder.stream().map(Object::toString).collect(Collectors.joining(","));
            //sends to client turns order
            client.updateStatus();
            for (ClientHandler handler : first_player.sameMatchPlayers()) {
                handler.getCurrentGamePhase().setTurnOrder(turnOrder);
                handler.setGameAlreadyStarted(true);
                if(!handler.equals(client)){
                alert(handler, "Planning phase has started! Turns order: "+ turns +". You can now play an assistant\n  card, type PLAY [x] (type 'HELP' if you need more info)");}

            }
            output.setArgString("Planning phase has started! Turns order: "+ turns +". You can now play an assistant\n  card, type PLAY [x] (type 'HELP' if you need more info)");
            client.setAlreadyUpdated(true);
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
                //alert(handler, "Game of " + numberOfPlayers + " started");
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


    public static Message info(Message request, ClientHandler clientHandler) {
        Message output = new Message("string");
        if (clientHandler.getGame() == null) { // if the player is already participating in a game
            output.setArgString("Not playing yet!");
            return output;
        } else {
            // set as string argument the character description
            output.setArgString(clientHandler.getGame().getSelectedCharacters().get(request.getArgNum1()).getDescription());
        }
        return output;
    }


    public static Message character(Message request, ClientHandler client, List clients) {
        Message response = new Message("string");
        int index = request.getArgNum1();
        Player player = client.getGame().getPlayer(client.getUsername());
        Game game = client.getGame();

        try {
            validateTurn(client);
            validateCharacter(index, player);
            Character chosenCharacter = client.getGame().getSelectedCharacters().get(index); //get chosen Character
            int characterIndex = chosenCharacter.getCharacterNumber();
            switch (characterIndex){
                case 0 -> response = CharacterController.processChar0(player, request,game, chosenCharacter);
                case 1 -> response = CharacterController.processChar1(player, request,game,chosenCharacter);
                case 2 -> response = CharacterController.processChar2(player, request,game,chosenCharacter);
                case 3 -> response = CharacterController.processChar3(player, request,game,chosenCharacter);
                case 4 -> response = CharacterController.processChar4(player, request,game,chosenCharacter);
                case 5 -> response = CharacterController.processChar5(player, request,game,chosenCharacter);
                case 6 -> response = CharacterController.processChar6(player, request,game,chosenCharacter);
                case 7 -> response = CharacterController.processChar7(player, request,game,chosenCharacter);
                case 8 -> response = CharacterController.processChar8(player, request,game,chosenCharacter);
                case 9 -> response = CharacterController.processChar9(player, request,game,chosenCharacter);
                case 10 -> response = CharacterController.processChar10(player, request,game,chosenCharacter);
                case 11 -> response = CharacterController.processChar11(player, request,game,chosenCharacter);
            }
        } catch (alreadyPlayedACharacterException e) {
            response.setArgString("You have already played a character during this round, wait for the next round");
        } catch (NonExistentCharacterException ex) {
            response.setArgString("There is no such character");
        }catch (Exception notEnoughCoins) {
            response.setArgString(notEnoughCoins.getMessage());
        }

        return response;
    }

    private static void validateCharacter(int index, Player player) throws alreadyPlayedACharacterException, NonExistentCharacterException {
        if( index < 0 || index > 2) throw new NonExistentCharacterException();
        if (player.getPlayedCharacterNumber() != -1) throw new alreadyPlayedACharacterException();

    }
    private static void validateTurn(ClientHandler client) throws GamePhase.WrongTurn {

        Player player = client.getGame().getPlayer(client.getUsername());
        boolean existPlayerBeforeMeThatHaveToPlay = false; //checks if there are no players before you
        for (ClientHandler pl : client.sameMatchPlayers()) {
            if (pl.getUsername() == player.getPlayer_id()) {
                break;
            }
            boolean playerPlayed = pl.getGame().getPlayer(pl.getUsername()).getFace_up_assistant() != null;
            if (!playerPlayed) {
                existPlayerBeforeMeThatHaveToPlay = true;
                break;
            }
        }
        if (existPlayerBeforeMeThatHaveToPlay) { // there are players before me
            throw new PlanningPhase.WrongTurn("You have to wait your turn to play this character");
        } else if(client.getCurrentGamePhase().isActionPhase()) {
            if(ActionPhase.getCurrentPlayerNextAction().getPlayer() != player)
                throw new PlanningPhase.WrongTurn("You have to wait your turn to play this character");
            }

    }
    public static class alreadyPlayedACharacterException extends Exception {
        public alreadyPlayedACharacterException() {}
    }
    public static class NonExistentCharacterException extends Exception {
        public NonExistentCharacterException() {}
    }

}
