package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.server.ClientHandler;

import static it.polimi.ingsw.server.controller.BaseController.setGamePhaseForAllPlayers;
import static java.util.Collections.sort;
import java.util.Comparator;




public class ActionController {

    public static Message place(Message request, ClientHandler clientHandler, GamePhase currentGamePhase) {

        Message response = new Message("string");
        try {
            GamePhase gamePhase = currentGamePhase.isActionPhase() ?
                    (ActionPhase) currentGamePhase :
                    (PlanningPhase) currentGamePhase;
            gamePhase.validatePlaceStudent(clientHandler); // check if action is allowed
            response = processPlace(request, clientHandler); // action
            ActionPhase actionPhase = (ActionPhase) gamePhase; // action phase updated with the new action expected from the same player

            if (actionPhase.alreadyMovedThreeStudents(clientHandler)) {
                actionPhase.setNextActionForCurrentPlayer();
            }
        } catch (GamePhase.WrongTurn e) {
            response.setArgString("Wrong turn");
        } catch (GamePhase.WrongPhaseException e) {
            response.setArgString("Wrong command for this phase");
        } catch (ActionPhase.WrongAction e) {
            response.setArgString(e.getMessage());
        }
        return response;
    }

    public static Message move(Message request, ClientHandler clientHandler, GamePhase currentGamePhase) {
        Message response = new Message("string");

        try {
            GamePhase gamePhase = currentGamePhase.isActionPhase() ?
                    (ActionPhase) currentGamePhase :
                    (PlanningPhase) currentGamePhase;

            gamePhase.validateMoveMotherNature(clientHandler); // check if action is allowed
            response = processMove(request, clientHandler); // action
            ActionPhase actionPhase = (ActionPhase) gamePhase; // action phase updated with the new action expected from the same player
            actionPhase.setNextActionForCurrentPlayer();
        } catch (GamePhase.WrongTurn e) {
            response.setArgString("Wrong turn");
        } catch (GamePhase.WrongPhaseException e) {
            response.setArgString("Wrong command for this phase");
        } catch (ActionPhase.WrongAction e) {
            response.setArgString(e.getMessage());
        }
        return response;
    }

    public static Message choose(Message request, ClientHandler clientHandler, GamePhase currentGamePhase) {
        Message response = new Message("string");

        try {
            GamePhase gamePhase = currentGamePhase.isActionPhase() ?
                    (ActionPhase) currentGamePhase :
                    (PlanningPhase) currentGamePhase;

            gamePhase.validateChooseCloud(clientHandler); // check if action is allowed
            response = processChoose(request, clientHandler); // action

            // TODO: logica scatto prossimo round?

            boolean isLastPlayer =
                    gamePhase.getCurrentPlayers().indexOf(clientHandler) == gamePhase.getCurrentPlayers().size() - 1;

            if (isLastPlayer) {
                sort(currentGamePhase.getCurrentPlayers(),
                        Comparator.comparingInt((ClientHandler a)->a.getGame().getPlayer(a.getUsername()).getFace_up_assistant().getValue()));
                ClientHandler firstPlayer = currentGamePhase.getCurrentPlayers().get(0);
                firstPlayer.isPlayerFirstMove = true;

                GamePhase planningPhase = new PlanningPhase(clientHandler.getGame(), currentGamePhase.getCurrentPlayers());
                setGamePhaseForAllPlayers(currentGamePhase.getCurrentPlayers(),planningPhase);
                planningPhase.getCurrentPlayers().forEach(player -> player.getGame().getPlayer(player.getUsername()).setFace_up_assistant(null));

            } else {
                ActionPhase actionPhase = (ActionPhase) gamePhase; // action phase updated with the new action expected from the next player
                actionPhase.setNextPlayerAndFirstAction(clientHandler);
            }

        } catch (GamePhase.WrongTurn e) {
            response.setArgString("Wrong turn");
        } catch (GamePhase.WrongPhaseException e) {
            response.setArgString("Wrong command for this phase");
        } catch (ActionPhase.WrongAction e) {
            response.setArgString(e.getMessage());
        }
        return response;
    }

    /**
     * @param request message containing the command
     * @param client  client that sent the request to start the game
     * @return a new STRING message containing the
     */
    public static Message processPlace(Message request, ClientHandler client) throws ActionPhase.WrongAction {
        Player currentPlayer = client.getGame().getPlayer(client.getUsername()); // get the player who sent command PLACE
        Message output = new Message("string"); // set up output message

        if (request.getArgNum1() >= currentPlayer.getSchool().getEntranceSize()) { // if the argument refers to a non-existing student
            throw new ActionPhase.WrongAction("Non existing student, please retry");
        } else {
            Student played = currentPlayer.getSchool().getEntrance().get(request.getArgNum1()); // get
            switch (request.getTo_tile()){
                case DINING -> {
                    try { // try moving the student from entrance to the selected dining room
                        client.getGame().moveStudent(currentPlayer.getSchool(), currentPlayer.getSchool().getDining_rooms().get(request.getArgNum2()), played);
                    } catch (Exception ex) {
                        throw new ActionPhase.WrongAction("Can't move the student in the dining room, please retry");
                    }
                }
                case ISLAND -> {
                    if(request.getArgNum2()<=client.getGame().getArchipelago().size())
                        client.getGame().moveStudent(currentPlayer.getSchool(),client.getGame().getArchipelago().get(request.getArgNum2()), played);
                    else {
                        throw new ActionPhase.WrongAction("Not existing island, please retry");
                    }
                }
            }
            output.setArgString("Student placed");
        }
        return output;
    }

    public static Message processMove(Message request, ClientHandler client) throws ActionPhase.WrongAction {
        Message output = new Message("string");
        Game current_game = client.getGame();
        Player current_player = client.getGame().getPlayer(client.getUsername());
        int mother_nature_movements = request.getArgNum1();
        try {
            current_game.moveMotherNature(mother_nature_movements,current_player);
            output.setArgString("Mother nature moved, you have conquered this island!");
        } catch (Game.DistanceMotherNatureException e) {
            throw new ActionPhase.WrongAction("Can't move Mother Nature this far, please retry");
        }
        catch (Exception e ){
            throw new ActionPhase.WrongAction("Mother nature moved, you can't conquer the island");
        }
        return output;
    }

    private static Message processChoose(Message message, ClientHandler client) throws ActionPhase.WrongAction {
        Message output = new Message("string");
        Game game = client.getGame();
        if(game.getClouds().get(message.getArgNum1()).getStudents().size()==0){
            throw new ActionPhase.WrongAction("Can't choose this cloud, it has been already chosen by another player");
        } else {

                game.chooseCloud(game.getClouds().get(message.getArgNum1()), game.getPlayer(client.getUsername()));
                output.setArgString("Students moved to your School Board");

        }
        return output;
    }


}
