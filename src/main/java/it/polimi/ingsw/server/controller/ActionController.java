package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.communication.messages.ToTile;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.ClientHandler;

import static it.polimi.ingsw.server.controller.BaseController.setGamePhaseForAllPlayers;
import static java.util.Collections.sort;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public class ActionController  {

    public static Message place(Message request, ClientHandler clientHandler, GamePhase currentGamePhase) {
        Message response = new Message("string");
        if (clientHandler.getGame() == null){
            response.setArgString("Game not started yet");
            return response;
        }
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
        catch (GamePhase.GameEndedException e) {
            response.setArgString("Game already ended");
        }
        return response;
    }

    public static Message move(Message request, ClientHandler clientHandler, GamePhase currentGamePhase) {
        Message response = new Message("string");
        if (clientHandler.getGame() == null){
            response.setArgString("Game not started yet");
            return response;
        }
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
        catch (GamePhase.GameEndedException e) {
            response.setArgString("Game already ended");
        }
        return response;
    }

    public static Message choose(Message request, ClientHandler clientHandler, GamePhase currentGamePhase) {
        Message response = new Message("string");
        if (clientHandler.getGame() == null){
            response.setArgString("Game not started yet");
            return response;
        }

        try {
            GamePhase gamePhase = currentGamePhase.isActionPhase() ?
                    (ActionPhase) currentGamePhase :
                    (PlanningPhase) currentGamePhase;
            gamePhase.validateChooseCloud(clientHandler); // check if action is allowed
            response = processChoose(request, clientHandler); // action
            //changeActivePlayer(gamePhase); // change the order of currentPlayers
            boolean isLastPlayer =
                    gamePhase.getCurrentPlayers().indexOf(clientHandler) == gamePhase.getCurrentPlayers().size() - 1; // checks if all players have already played this phase
            if (isLastPlayer) {

                sort(currentGamePhase.getCurrentPlayers(),
                        Comparator.comparingInt((ClientHandler a)->a.getGame().getPlayer(a.getUsername()).getFace_up_assistant().getValue())); //players sorted by previous assistant value

                ClientHandler oldFirstPlayer = currentGamePhase.getCurrentPlayers()
                                                                .stream()
                                                                .filter(p-> (p.isPlayerFirstMove))
                                                                .findAny().orElse(null);
                oldFirstPlayer.setPlayerFirstMove(false);
                ClientHandler firstPlayer = currentGamePhase.getCurrentPlayers().get(0);
                firstPlayer.isPlayerFirstMove = true;

                GamePhase planningPhase = new PlanningPhase(clientHandler.getGame(), currentGamePhase.getCurrentPlayers()); // new game phase
                setGamePhaseForAllPlayers(currentGamePhase.getCurrentPlayers(),planningPhase);
                planningPhase.getCurrentPlayers().forEach(player -> player.getGame().getPlayer(player.getUsername()).setFace_up_assistant(null));
                firstPlayer.getGame().startTurn(); //clouds filled
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
        } catch (GamePhase.GameEndedException e) {
            response.setArgString("Game already ended");
        }
        return response;
    }

    /**
     * change the active player
     */
    private static void changeActivePlayer(GamePhase gamePhase) {
        for (int i=0; i<gamePhase.getCurrentPlayers().size(); i++){
            if(gamePhase.getCurrentPlayers().get(i).getUsername().equals(gamePhase.getCurrent_game().getActivePlayer().getPlayer_id())){
                String name = gamePhase.getCurrentPlayers().get(i+1).getUsername();
                Player player = gamePhase.getCurrent_game().getPlayer(name);
                gamePhase.getCurrent_game().setActivePlayer(player);
            }
        }
    }

    /**
     * @param request message containing the command
     * @param client  client that sent the request to start the game
     * @return a new STRING message containing the action result
     */
    public static Message processPlace(Message request, ClientHandler client) throws ActionPhase.WrongAction {
        Player currentPlayer = client.getGame().getPlayer(client.getUsername()); // get the player who sent command PLACE
        Message output = new Message("string"); // set up output message
        placeArgumentCheck(request, client); // if the arguments are not ok exceptions are thrown

        Student played = currentPlayer.getSchool().getEntrance().get(request.getArgNum1()); // get the placed student
        switch (request.getTo_tile()){
            case DINING -> {
                try { // try moving the student from entrance to the selected dining room
                    Colour colour = played.getColour();
                    client.getGame().moveStudent(currentPlayer.getSchool(), currentPlayer.getSchool().getDining_room(colour), played);
                } catch (Exception ex) {
                    throw new ActionPhase.WrongAction("Can't move the student in the dining room, please retry");
                }
            }
            case ISLAND -> {
                if(request.getSingleArgNum2()<=client.getGame().getArchipelago().size())
                    client.getGame().moveStudent(currentPlayer.getSchool(),client.getGame().getArchipelago().get(request.getSingleArgNum2()), played);
                else {
                    throw new ActionPhase.WrongAction("Not existing island, please retry");
                }
            }
        }
        output.setArgString("Student placed");

        return output;
    }
    /**
     * processes the command and checks if there are valid conditions to end the game
     * @param request message containing the command
     * @param client  client that sent the request to start the game
     * @return a new STRING message containing the action result
     */
    public static Message processMove(Message request, ClientHandler client) throws ActionPhase.WrongAction {
        Message output = new Message("string");
        Game current_game = client.getGame();
        Player current_player = client.getGame().getPlayer(client.getUsername());
        GamePhase currentGamePhase = client.getCurrentGamePhase();
        int mother_nature_movements = request.getArgNum1();
        TowerColour playerTeam = current_player.getTeam();
        try {
            current_game.moveMotherNature(mother_nature_movements,current_player);
            if (current_game.getArchipelago().get(request.getArgNum1()).getTower() == playerTeam){
                // TODO: even when island is conquered this string is not printed
                output.setArgString("Mother nature moved, you have conquered this island!");
            } else {
                output.setArgString("Mother nature moved but you can't conquer this island");
            }

        } catch (Game.DistanceMotherNatureException e) {
            throw new ActionPhase.WrongAction("Can't move Mother Nature this far, please retry");
        }

        boolean isLastPlayer =
                client.getCurrentGamePhase().getCurrentPlayers().indexOf(client) == client.getCurrentGamePhase().getCurrentPlayers().size() - 1;
        //game ends if all towers have been placed by one player
        Message firstEndingCondition = towersEnded(current_game, current_game.getPlayers(),currentGamePhase);
        //game ends if only three groups of islands are left
        Message secondEndingCondition = minNumberOfIslands(current_game, current_game.getPlayers(), currentGamePhase);
            if (firstEndingCondition != null ) {
                    output = firstEndingCondition;
            }
           else if ( secondEndingCondition != null){
                output = secondEndingCondition;
            }
           else if( isLastPlayer && current_game.getBag().size() == 0 ) { //game ends if no students are left
                Player winner = current_game.getConclusionChecks().checkWinner(current_game.getPlayers(),current_game.getArchipelago());
                if (winner != null ){
                    output.setArgString("The winner is: " + winner.getPlayer_id());
                }
            }

           else if( isLastPlayer && current_player.getAssistants().size() == 0 ) { //game ends if no assistants are left
                Player winner = current_game.getConclusionChecks().checkWinner(current_game.getPlayers(),current_game.getArchipelago());
                if (winner != null ){
                    output.setArgString("The winner is: " + winner.getPlayer_id());
                }
            }

        return output;
    }

    /**
     * @param request message containing the command
     * @param client  client that sent the request to start the game
     * @return a new STRING message containing the action result
     */
    private static Message processChoose(Message request, ClientHandler client) throws ActionPhase.WrongAction {
        Message output = new Message("string");
        Game game = client.getGame();
        if(game.getClouds().get(request.getArgNum1()).getStudents().size()==0){
            throw new ActionPhase.WrongAction("Can't choose this cloud, it has been already chosen by another player");
        } else {
                game.chooseCloud(game.getClouds().get(request.getArgNum1()), game.getPlayer(client.getUsername()));
                output.setArgString("Students moved to your School Board");

        }
        return output;
    }

    /**
     * @param request command to check
     * @param client client who sent the command
     * @return true if the arguments are ok
     */
    private static void placeArgumentCheck(Message request, ClientHandler client) throws ActionPhase.WrongAction {
        // check if students exists
        if (request.getArgNum1() >= client.getGame().getPlayer(client.getUsername()).getSchool().getEntranceSize()) {
            throw new ActionPhase.WrongAction("Non existing student, please retry");
        }
        if(request.getTo_tile() == ToTile.ISLAND){
            // check if island exists
            if(request.getSingleArgNum2() >= client.getGame().getArchipelago().size())
                throw new ActionPhase.WrongAction("Not existing island, please retry");
        }
    }

    /**
     * @param game reference to the current game
     * @param players reference to the players
     * @return a new STRING message containing the winner (winner is set null if there is no winner)
     */
    private static Message towersEnded(Game game, LinkedList<Player> players, GamePhase currentGamePhase) {
        TowerColour winnerTeam = game.getConclusionChecks().endBecauseAvailableTowersFinished(players, null);
        String winner = null;
        Message winningTeam = null;
        if (winnerTeam != null) { //checks if there is a winner

            for (Player p : players) {
                if (p.getTeam() == winnerTeam)
                    winner = p.getPlayer_id();
            }
            currentGamePhase.setGameEnded(true); //gameEnded attribute is set false
            winningTeam = new Message("The winner is: " + winner);
            return winningTeam;
        }
        return null;
    }
    /**
     * @param game reference to the current game
     * @param players reference to the players
     * @param currentGamePhase reference to the the current gamePhase
     * @return a new STRING message containing the winner (winner is set null if there is no winner)
     */

    private static Message  minNumberOfIslands(Game game, List<Player> players, GamePhase currentGamePhase){
        TowerColour winnerTeam = game.getConclusionChecks().endBecauseOfArchipelagoSize(3,game.getArchipelago(),players);
        String winner = null;
        Message winningTeam = null;
         if(winnerTeam != null) { //checks if there is a winner
             for (Player p : players) {
                 if (p.getTeam() == winnerTeam)
                     winner = p.getPlayer_id();
             }
             currentGamePhase.setGameEnded(true); //gameEnded attribute is set false
             winningTeam = new Message("The winner is: " + winner);
             return winningTeam;
        }
        return null;
    }


}
