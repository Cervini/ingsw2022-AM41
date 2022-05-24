package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.server.ClientHandler;

public class ActionController {

    public static Message place(Message request, ClientHandler clientHandler, GamePhase currentGamePhase) {

        Message response = new Message("string");

        try {
            GamePhase gamePhase = currentGamePhase.isActionPhase() ?
                    (ActionPhase) currentGamePhase :
                    (PlanningPhase) currentGamePhase;

            // validazione se la mossa consentita
            gamePhase.validatePlaceStudent(clientHandler);
            // mossa
            response = processPlace(request, clientHandler);

            // aggiornamento fase del gioco, con la prossima action attesa dello stesso giocatore
            ActionPhase actionPhase = (ActionPhase) gamePhase;

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

            // validazione se la mossa consentita
            gamePhase.validateMoveMotherNature(clientHandler);
            // mossa
            response = processMove(request, clientHandler);
            // aggiornamento fase del gioco, con la prossima action attesa dello stesso giocatore
            ActionPhase actionPhase = (ActionPhase) gamePhase;
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

            // validazione se la mossa consentita
            gamePhase.validateChooseCloud(clientHandler);
            // mossa
            response = processChoose(request, clientHandler);

            // TODO: logica scatto prossimo round?

            boolean isLastPlayer =
                    gamePhase.getCurrentPlayers().indexOf(clientHandler) == gamePhase.getCurrentPlayers().size() - 1;

            if (isLastPlayer) {
                // TODO: implementare lo scatto del prossimo round
            } else {
                // aggiornamento fase del gioco, con la prima action del prossimo giocatore
                ActionPhase actionPhase = (ActionPhase) gamePhase;
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
            currentPlayer.getSchool().removeStudent(played); //removes student from entrance
            switch (request.getTo_tile()){
                case DINING -> {
                    try {
                        client.getGame().moveStudent(currentPlayer.getSchool(), currentPlayer.getSchool().getDining_rooms().get(request.getArgNum2()), played);
                        currentPlayer.getSchool().getDining_rooms().get(request.getArgNum2()).putStudent(played);
                    } catch (Exception ex) {
                        output.setArgString("Can't move the student, please retry");
                    }
                }
                case ISLAND -> client.getGame().getArchipelago().get(request.getArgNum2()).putStudent(played);
            }
            output.setArgString("Student placed");
        }
        return output;
    }

    public static Message processMove(Message request, ClientHandler client) {
        Message output = new Message("string");
        Game current_game = client.getGame();
        Player current_player = client.getGame().getPlayer(client.getUsername());
        int mother_nature_movements = request.getArgNum1();
        try {
            current_game.moveMotherNature(mother_nature_movements,current_player);
        } catch (Exception e) {
            output.setArgString("Can't move Mother Nature this far, please retry");
        }
        return output;
    }

    private static Message processChoose(Message message, ClientHandler client){
        Message output = new Message("string");
        Game game = client.getGame();
        if(game.getClouds().get(message.getArgNum1()).getStudents().size()==0){
            output.setArgString("Can't choose this cloud, it has been already chosen by another player");
            return output;
        } else {
            try {
                game.chooseCloud(game.getClouds().get(message.getArgNum1()), game.getPlayer(client.getUsername()));
                output.setArgString("Students moved to your School Board");
            } catch (Exception e) {
                output.setArgString("Impossible move, try another");
            }
        }
        return output;
    }


}
