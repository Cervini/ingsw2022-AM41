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

            // validazione se la mossa consentita
            gamePhase.validatePlayAssistant(clientHandler);
            // mossa
            response = processPlay(request, clientHandler);

            // TODO: logica controllo cambiamento fase del gioco

            boolean canStartActionPhase = allPlayersHavePlayedAnAssistant(clientHandler);

            if (canStartActionPhase) {
                List<ClientHandler> sameMatchPlayers = clientHandler.sameMatchPlayers();

                //ordino in base al valore dell'assistente

                sort(sameMatchPlayers,
                        Comparator.comparingInt((ClientHandler a) -> a.getGame().getPlayer(a.getUsername()).getFace_up_assistant().getValue()));
                GamePhase actionPhase = new ActionPhase(clientHandler.getGame(), sameMatchPlayers);
                setGamePhaseForAllPlayers(sameMatchPlayers, actionPhase);

                //il giocatore random non ha più il primo turno
//                ClientHandler oldFirstTurn = (ClientHandler) sameMatchPlayers.stream().filter(ClientHandler::getPlayerFirstMove);
//                oldFirstTurn.setPlayerFirstMove(false);
//                sameMatchPlayers.get(0).setPlayerFirstMove(true);
            }
        } catch (GamePhase.WrongPhaseException e) {
            response.setArgString("Wrong command for this phase");
        } catch (GamePhase.WrongTurn e) {
            response.setArgString("It is not your turn");
        }

        return response;
    }

    /**
     * @param message message containing the command
     * @param client  client that sent the request to start the game
     * @return a new STRING message containing the result of the PLAY command
     */
    private static Message processPlay(Message message, ClientHandler client) {
        Message output = new Message("string");
        int index = message.getArgNum1();
        try {
            Assistant played = client.getGame().getPlayer(client.getUsername()).getAssistants().get(index);
            if (client.getGame().getPlayer(client.getUsername()).getAssistants().size() == 1) {
                playAssistant(client, index, output); // if the player has only one assistant
            } else {
                if (uniqueAssistant(client, played)) {
                    playAssistant(client, index, output);


                } else {
                    if (!checkAllUnique(client)) {
                        playAssistant(client, index, output);
                    } else {
                        output.setArgString("Another player has already played this Assistant, try another");
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
        for (ClientHandler player : client.sameMatchPlayers()) {
            Assistant other = client.getGame().getPlayer(player.getUsername()).getFace_up_assistant();
            if ((other == null) || (other.equals(assistant))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param client player whose assistant are checked
     * @return false if the player has no unique Assistants
     */
    private static boolean checkAllUnique(ClientHandler client) {
        Game game = client.getGame();
        boolean check = false;
        for (Assistant assistant : game.getPlayer(client.getUsername()).getAssistants()) {
            if (uniqueAssistant(client, assistant))
                check = true;
        }
        return check;
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

}

