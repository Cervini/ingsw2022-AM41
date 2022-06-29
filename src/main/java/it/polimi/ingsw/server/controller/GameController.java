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

    /** collects available players and puts them in the same match, sets first player randomly chosen and notifies all players
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
            Collections.swap(client.getClients(),0,client.getClients().indexOf(first_player));//swaps first player and player placed in first position of clients list
            first_player.isPlayerFirstMove = true;//sets first player's first move true
            List<ClientHandler> sameMatchPlayers = first_player.sameMatchPlayers();
            GamePhase gamePhase = new PlanningPhase(game, sameMatchPlayers);
            setGamePhaseForAllPlayers(sameMatchPlayers, gamePhase); //all players have the same GamePhase attribute
            client.setGame(game);
            game.getPlayers().get(0).setPlayer_id(client.getUsername()); //set the client who sent START command as a player
            output.setArgString("Game of " + available + " started");
            setAsPlaying(message.getArgNum1(), game, client.getClients()); // set the other available client as players
            //puts each player username in a list
            List <String> turnOrder = client.sameMatchPlayers().stream().map(ClientHandler::getUsername).toList();//create a list with turns order
            //converts list of strings to a string
            String turns = turnOrder.stream().map(Object::toString).collect(Collectors.joining(","));//converts the previous list in a string (which is sent to the players)
            //sends to client turns order
            client.updateStatus();
            List<ClientHandler> players = client.sameMatchPlayers();
            players.forEach(p->p.getCurrentGamePhase().setTurnOrder(turnOrder));//sets same turns order for all players
            players.forEach(p-> p.setGameAlreadyStarted(true));
            players.remove(client);
            players.forEach(p-> alert(p, "Planning phase has started! Turns order: "+ turns +". You can now play an assistant\n  card, type PLAY [x] (type 'HELP' if you need more info)"));//notifies all players about turns order
            output.setArgString("Planning phase has started! Turns order: "+ turns +". You can now play an assistant\n  card, type PLAY [x] (type 'HELP' if you need more info)");
            client.setAlreadyUpdated(true);
            //client.sameMatchPlayers().forEach(p->p.getGame().getPlayer(p.getUsername()).giveCoins(4));
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
     * Set the first 'NumberOfPlayers' available clients as playing the 'game' instance
     * @param numberOfPlayers involved in this match
     * @param game game
     * @param clients
     */
    private static void setAsPlaying(int numberOfPlayers, Game game, List<ClientHandler> clients) {
        int count = 1;
        for (ClientHandler handler : clients) {
            if (handler.isAvailable()) {//clients whose game is still null
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
            System.out.println("A user has logged out");
        }
    }

    /**
     *sends information about a character
     * @param request client request
     * @param clientHandler who sent the command effect
     * @return server response
     */
    public static Message info(Message request, ClientHandler clientHandler) {
        Message output = new Message("string");
        clientHandler.setAlreadyUpdated(true);
        if( request.getArgNum1() > 2 ) {
            output.setArgString("Non existing character, please retry");
            return output;
        }
        if (clientHandler.getGame() == null) { // if the player is already participating in a game
            output.setArgString("Not playing yet!");
            return output;
        } else {
            // set as string argument the character description
            output.setArgString(clientHandler.getGame().getSelectedCharacters().get(request.getArgNum1()).getDescription());
        }
        return output;
    }

    /**
     *handles character usage
     * @param request client request
     * @param client client who sent the command
     * @param clients players involved in the same match
     * @return message containing the action result
     */
    public static Message character(Message request, ClientHandler client, List clients) {
        Message response = new Message("string");// set up output message
        int index = request.getArgNum1();//gets index of chosen character (displayed)
        Player player = client.getGame().getPlayer(client.getUsername());
        Game game = client.getGame();
        try {
            validateTurn(client);
            validateCharacter(index, player);//validates if this character exists
            Character chosenCharacter = client.getGame().getSelectedCharacters().get(index); //gets chosen Character
            int characterIndex = chosenCharacter.getCharacterNumber();//gets corresponding index of chosen character
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
        } catch (Exception e) {
            player.setPlayedCharacterNumber(-1);
            response.setArgString(e.getMessage());
        }

        return response;
    }

    /**
     *validates whether a character
     * @param index character index
     * @param player player who sent the command
     * @throws alreadyPlayedACharacterException thrown in case of the player has already played a character during a round
     * @throws NonExistentCharacterException  thrown in case of the player has played a non existent charcter
     */
    private static void validateCharacter(int index, Player player) throws alreadyPlayedACharacterException, NonExistentCharacterException {
        if( index < 0 || index > 2) throw new NonExistentCharacterException(); //only three characters are randomly chosen during a match
        if (player.getPlayedCharacterNumber() != -1) throw new alreadyPlayedACharacterException();//checks if the player has already played a character

    }

    /**
     *validates whether a player can play a character
     * @param client who sent the command
     * @throws GamePhase.WrongTurn thrown in case of wrong turn
     */
    private static void validateTurn(ClientHandler client) throws GamePhase.WrongTurn {
        //in case of planning phase
        Player player = client.getGame().getPlayer(client.getUsername());
        boolean existPlayerBeforeMeThatHaveToPlay = false; //checks if there are no players before you
        for (String id : client.getCurrentGamePhase().getTurnOrder()) {
            if (id == player.getPlayer_id()) {
                break;
            }
            boolean playerPlayed = client.getGame().getPlayer(id).getFace_up_assistant() != null; //there is a player before me who has to play
            if (!playerPlayed) {
                existPlayerBeforeMeThatHaveToPlay = true;
                break;
            }
        }
        if (existPlayerBeforeMeThatHaveToPlay) { // there are players before me
            throw new PlanningPhase.WrongTurn("You have to wait your turn to play this character");
        } else if (client.getCurrentGamePhase().isActionPhase()) {
            //in case of action phase
            if(client.getGame().getCurrentPlayerNextAction().getPlayer() != player) //checks if current player corresponds to the player who sent the command
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
