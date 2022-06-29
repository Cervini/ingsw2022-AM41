package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.ClientHandler;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.server.controller.ActionController.updateTurns;
import static it.polimi.ingsw.server.controller.GameController.alert;
import static java.util.Collections.sort;

public class PlanningController extends BaseController {

    /**first it validates gamePhase, then it processes the request.
     * If it is the last player of planning phase  it sorts turns, sets game phase for all players and notifies them
     * @param request contains client's command
     * @param clientHandler contains client reference
     * @param currentGamePhase contains client's GamePhase attribute
     * @return message containing the action result
     */
    public static Message play(Message request, ClientHandler clientHandler, GamePhase currentGamePhase) {

        Message response = new Message("string");

        if (clientHandler.getGame() == null){
            response.setArgString("Game not started yet");
            return response;
        }

        try {
            GamePhase gamePhase = currentGamePhase.isActionPhase() ?
                    (ActionPhase) currentGamePhase :
                    (PlanningPhase) currentGamePhase;
            gamePhase.validatePlayAssistant(clientHandler); // check if move is allowed
            response = processPlay(request, clientHandler); // processes move by modifying the model

            boolean canStartActionPhase = allPlayersHavePlayedAnAssistant(clientHandler); //if all players have played an assistant action phase can start

            if (!canStartActionPhase ) {//still planning phase
                response = updateTurns(clientHandler);// all players are notified about turns
            }
            if (canStartActionPhase) {
                List<ClientHandler> sameMatchPlayers = clientHandler.sameMatchPlayers();

                sort(sameMatchPlayers,
                        Comparator.comparingInt((ClientHandler a) -> a.getGame().getPlayer(a.getUsername()).getFace_up_assistant().getValue()));
                GamePhase actionPhase = new ActionPhase(clientHandler.getGame(), sameMatchPlayers);//sorts players by the previously chosen assistant values
                setGamePhaseForAllPlayers(sameMatchPlayers, actionPhase);//sets the same game phase for all players
                List <String> turnOrder = sameMatchPlayers.stream().map(ClientHandler::getUsername).toList();//list with usernames of players previously sorted
                String turns = turnOrder.stream().map(Object::toString).collect(Collectors.joining(", ")); //converts list of strings to a string
                clientHandler.updateStatus();
                List<ClientHandler> players = clientHandler.sameMatchPlayers();
                players
                        .forEach(p->p.getCurrentGamePhase().setTurnOrder(turnOrder));//sets same turns order for all players
                players.remove(clientHandler);
                players
                        .forEach(p-> alert(p, "Action Phase started! Turns order: "+ turns +". First of all move three \n  students from your entrance to an island or to the dining room \n " +
                        "type 'PLACE [x] [DINING/ISLAND] [*/y]' (type 'HELP' if you need more info)"));//notifies all players about turns order
                clientHandler.setAlreadyUpdated(true);
                response.setArgString("Action Phase started! Turns order: "+ turns+". First of all move three \n  students from your entrance to an island or to the dining room \n " +
                        "type 'PLACE [x] [DINING/ISLAND] [*/y]' (type 'HELP' if you need more info)");
            }

        } catch (GamePhase.WrongPhaseException e) {
            response.setArgString("Wrong command for this phase");
        } catch (GamePhase.WrongTurn e) {
            response.setArgString("It is not your turn");
        } catch (GamePhase.GameEndedException e) {
            response.setArgString("Game already ended");
        } catch (Exception e){
            response.setArgString(e.getMessage());
        }
        return response;
    }


    /**
     * processes command checking whether any other player has played the same assistant
     * If there is only one assistant left it processes the command in any case
     * @param message message containing the command in any case
     * @param client  client that sent the request to start the game
     * @return a new STRING message containing the result of the PLAY command
     */
    private static Message processPlay(Message message, ClientHandler client) throws Exception {
        Message output = new Message("string"); // prepare the output message
        int index = message.getArgNum1(); // get the argument from the client's message

            try {
                Assistant played = client.getGame().getPlayer(client.getUsername()).getAssistants().get(index); // get the Assistant that the player wants to play
                if (client.getGame().getPlayer(client.getUsername()).getAssistants().size() == 1) {
                    playAssistant(client, index, output); // if the player has only one assistant play it (no need to make checks)
                    client.getGame().setLastRound(true); //at the end of this round checkWinner will be called
                } else {
                    if (uniqueAssistant(client, played)) { // if the assistant is unique between the already played
                        playAssistant(client, index, output); // play the assistant
                    } else {
                        if (!checkAllUnique(client)) { // if it's not unique check if any of the other assistants is playable
                            playAssistant(client, index, output); // if not still play the assistant
                        } else {
                            throw new Exception("Another player has already played this Assistant, try another"); // layer can play one other assistant without trouble
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                throw new Exception("This Assistant doesn't exist.");
            }

        return output;
    }

    /**
     * @return true if no other player of the same game has already played the same Assistant
     */
    private static boolean uniqueAssistant(ClientHandler client, Assistant assistant) {
        for (ClientHandler player : client.sameMatchPlayers()) { // for each of the players of the game
            Assistant other = client.getGame().getPlayer(player.getUsername()).getSchool().getFace_up_assistant(); // get the last played assistant
            //if(other == null)
                //break;
            if (other != null && other.equals(assistant)){ // if the played assistant is equal to the one tha player wants to play
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
    private static void playAssistant(ClientHandler client, int index, Message output) throws Exception {
        try {
            client.getGame().getPlayer(client.getUsername()).playAssistant(index);
            output.setArgString("Assistant played");
            if(client.getGame().isLastRound()) output.setArgString("Last assistant played, this is the last round");
        } catch (Exception e) {
            throw new Exception("Can't play this Assistant.");
        }
    }

    /**
     *checks whether all players have played an assistant
     * @param client in order to retrieve players involved in the same mathc
     * @return boolean
     */
    private static boolean allPlayersHavePlayedAnAssistant(ClientHandler client) {
        for (ClientHandler c : client.sameMatchPlayers()) {//for all players involved in the same match
            if (c.getGame().getPlayer(c.getUsername()).getFace_up_assistant() == null) return false;
        }
        return true;
    }

    /**
     *sets same game phase for all players
     * @param sameMatchPlayers involved in the same match
     * @param gamePhase next game phase
     */
    protected static void setGamePhaseForAllPlayers(List<ClientHandler> sameMatchPlayers, GamePhase gamePhase) {
        sameMatchPlayers.stream()
                .forEach(player -> player.setCurrentGamePhase(gamePhase));
    }


}

