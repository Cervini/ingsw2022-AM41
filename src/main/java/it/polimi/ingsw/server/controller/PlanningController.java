package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.ClientHandler;

import java.util.Comparator;
import java.util.List;

import static java.util.Collections.sort;


public class PlanningController extends BaseController {

    public static Message play(Message request, ClientHandler clientHandler, GamePhase currentGamePhase) {

        Message response = new Message("string");

        try {

            GamePhase gamePhase = currentGamePhase.isActionPhase() ?
                    (ActionPhase) currentGamePhase :
                    (PlanningPhase) currentGamePhase;
            gamePhase.validatePlayAssistant(clientHandler); // check if move is allowed
            response = processPlay(request, clientHandler); // process move


            boolean canStartActionPhase = allPlayersHavePlayedAnAssistant(clientHandler);

            if (canStartActionPhase) {
                List<ClientHandler> sameMatchPlayers = clientHandler.sameMatchPlayers();

                //ordino in base al valore dell'assistente

                sort(sameMatchPlayers,
                        Comparator.comparingInt((ClientHandler a) -> a.getGame().getPlayer(a.getUsername()).getFace_up_assistant().getValue()));
                GamePhase actionPhase = new ActionPhase(clientHandler.getGame(), sameMatchPlayers);
                setGamePhaseForAllPlayers(sameMatchPlayers, actionPhase);

            }

        } catch (GamePhase.WrongPhaseException e) {
            response.setArgString("Wrong command for this phase");
        } catch (GamePhase.WrongTurn e) {
            response.setArgString("It is not your turn");
        } catch (GamePhase.GameEndedException e) {
            response.setArgString("Game already ended");
        }

        return response;
    }

    /**
     * @param message message containing the command
     * @param client  client that sent the request to start the game
     * @return a new STRING message containing the result of the PLAY command
     */
    private static Message processPlay(Message message, ClientHandler client) {
        Message output = new Message("string"); // prepare the output message
        int index = message.getArgNum1(); // get the argument from the client's message
        try {
            Assistant played = client.getGame().getPlayer(client.getUsername()).getAssistants().get(index); // get the Assistant that the player wants to play
            if (client.getGame().getPlayer(client.getUsername()).getAssistants().size() == 1) {
                playAssistant(client, index, output); // if the player has only one assistant play it (no need to make checks)
            } else {
                if (uniqueAssistant(client, played)) { // if the assistant is unique between the already played
                    playAssistant(client, index, output); // play the assistant
                } else {
                    if (!checkAllUnique(client)) { // if it's not unique check if any of the other assistants is playable
                        playAssistant(client, index, output); // if not still play the assistant
                    } else {
                        output.setArgString("Another player has already played this Assistant, try another"); // layer can play one other assistant without trouble
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            output.setArgString("This Assistant doesn't exist.");
        }
        return output;
    }

    /**
     * @return true if no other player of the same game has already played the same Assistant
     */
    private static boolean uniqueAssistant(ClientHandler client, Assistant assistant) {
        for (ClientHandler player : client.sameMatchPlayers()) { // for each of the players of the game
            Assistant other = client.getGame().getPlayer(player.getUsername()).getSchool().getFace_up_assistant(); // get the last played assistant
            if(other == null)
                break;
            if (other.equals(assistant)){ // if the played assistant is equal to the one tha player wants to play
                return false; // the assistant is not unique
            }
        }
        return true; // the assistant is unique
    }

    /**
     * @param client player whose assistant are checked
     * @return false if the player has no unique Assistants
     */
    private static boolean checkAllUnique(ClientHandler client) {
        Game game = client.getGame(); // get the game from the client
        for (Assistant assistant : game.getPlayer(client.getUsername()).getAssistants()) { // for all the assistant of the player
            if (uniqueAssistant(client, assistant)) // if any of the assistant is unique
                return true; // the player has a playable assistant
        }
        return false; // none of the player assistants is playable
    }

    /**
     * play Assistant
     */
    private static void playAssistant(ClientHandler client, int index, Message output) {
        try {
            client.getGame().getPlayer(client.getUsername()).playAssistant(index);
            output.setArgString("Assistant played");
        } catch (Exception e) {
            output.setArgString("Can't play this Assistant.");
        }
    }

    private static boolean allPlayersHavePlayedAnAssistant(ClientHandler client) {
        for (ClientHandler c : client.sameMatchPlayers()) {
            if (c.getGame().getPlayer(c.getUsername()).getFace_up_assistant() == null) return false;
        }
        return true;
    }


    protected static void setGamePhaseForAllPlayers(List<ClientHandler> sameMatchPlayers, GamePhase gamePhase) {
        sameMatchPlayers.stream()
                .forEach(player -> player.setCurrentGamePhase(gamePhase));
    }

}

