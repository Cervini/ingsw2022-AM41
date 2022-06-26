package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.communication.messages.ToTile;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.ClientHandler;

import static it.polimi.ingsw.server.controller.BaseController.setGamePhaseForAllPlayers;
import static it.polimi.ingsw.server.controller.GameController.alert;
import static java.util.Collections.sort;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class ActionController  {

    private static boolean isLastRound = false;
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
                response.setArgString("Now you have to move mother nature, type MOVE [x] (type 'HELP' if you need \n  more info)");
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
            clientHandler.updateStatus();
            response.setArgString("You can now choose a cloud, type CHOOSE [x] (type 'HELP' if you need more info) ");
            clientHandler.setAlreadyUpdated(true);

            Player relevantPlayer =  clientHandler.getGame().getPlayer(clientHandler.getUsername());
            if( clientHandler.getGame().getBag().size() > 0 && relevantPlayer.getAssistants().size()!=0){ //game ends at this point only if player places his last tower or only three groups of islands are left
            TowerColour winner = clientHandler.getGame().endGame();
            if (winner != null ){
                Player winnerPlayer = clientHandler.getGame().getPlayers().stream().filter(p->p.getTeam().equals(winner)).findFirst().get();
                List<ClientHandler> players = clientHandler.sameMatchPlayers();
                players.forEach(p->p.setGame(null));
                players.remove(clientHandler);
                players.forEach(p-> alert(p,"The winner is player: " + winnerPlayer.getPlayer_id()+"! You can now start a new game"));
                response.setArgString("The winner is player: " + winnerPlayer.getPlayer_id()+"! You can now start a new game");
            }
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
            try {
                gamePhase.validateChooseCloud(clientHandler); // check if action is allowed
                response = processChoose(request, clientHandler); // action
            }
            catch (ActionPhase.endingGame e) { //exception thrown by processChoose, sets last round true since there are no more students available
                setIsLastRound(true);
            } boolean isLastPlayer =
                            gamePhase.getCurrentPlayers().indexOf(clientHandler) == gamePhase.getCurrentPlayers().size() - 1; // checks if all players have already played this phase
            if (isLastPlayer) {
                if (isLastRound) { //players have played all assistants or there are no available students
                    response = endGame(clientHandler.getGame(), clientHandler);
                    List<ClientHandler> players = clientHandler.sameMatchPlayers();
                    players.forEach(p->p.setGame(null));
                    players.remove(clientHandler);
                    Message finalResponse = response;
                    players.forEach(p->  alert(p, finalResponse.getArgString()));
                    return response;
                } else { //game goes on since there are no winning conditions
                    response = changePhase(currentGamePhase,clientHandler);
                }
            } else {
                ActionPhase actionPhase = (ActionPhase) gamePhase; // action phase updated with the new action expected from the next player
                actionPhase.setNextPlayerAndFirstAction(clientHandler);
                response = updateTurns(clientHandler);
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
                output.setArgString("Mother nature moved, you have conquered this island!");
            } else {
                output.setArgString("Mother nature moved but you can't conquer this island");
            }
        } catch (Game.DistanceMotherNatureException e) {
            throw new ActionPhase.WrongAction("Can't move Mother Nature this far, please retry");
        }

        return output;
    }

    /**
     * @param request message containing the command
     * @param client  client that sent the request to start the game
     * @return a new STRING message containing the action result
     */
    private static Message processChoose(Message request, ClientHandler client) throws ActionPhase.WrongAction, ActionPhase.endingGame {
        Message output = new Message("string");
        Game game = client.getGame();
        boolean isLastRound =  client.getGame().getBag().size() == 0;
        if(request.getArgNum1() >= game.getClouds().size()){
            throw new ActionPhase.WrongAction("This cloud doesn't exist, type a number between 0 and "+(game.getClouds().size()-1));
        }
        if(isLastRound){
            throw new ActionPhase.endingGame("there are no students available");
        }
        if( game.getClouds().get(request.getArgNum1()).getStudents().size() == 0 ){
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

    private static void setCharacters(List<Player> players){
        players.forEach(player -> player.setPlayedCharacterNumber(-1));
    }
    private static Message endGame(Game current_game, ClientHandler client) {
        Message output= new Message("string");
        TowerColour winnerColour = client.getGame().endGame();
        Player winner = current_game.getPlayers().stream().filter(p -> p.getTeam().equals(winnerColour)).findFirst().get();
        output.setArgString("The winner is player: "+ winner.getPlayer_id()+"! You can now start a new game");
        return output;
    }

    public static Message updateTurns(ClientHandler clientHandler){
        Message output = new Message("string");
        int previousPlayerIdx = clientHandler.getCurrentGamePhase().getTurnOrder().indexOf(clientHandler.getUsername());
        String clientHandlerOfNextPlayer = clientHandler.getCurrentGamePhase().getTurnOrder().get(previousPlayerIdx+1);
        clientHandler.updateStatus();
        output.setArgString("Next player is: "+clientHandlerOfNextPlayer);
        for (ClientHandler handler : clientHandler.sameMatchPlayers()) {
            handler.setAlreadyUpdated(true);
            if (isIsLastRound() && !handler.equals(clientHandler)) {
                    alert(handler, "Next player is: "+clientHandlerOfNextPlayer+". This is the last round");
                    output.setArgString("Next player is: "+clientHandlerOfNextPlayer+". This is the last round");
            } else if (!handler.equals(clientHandler)) {
                    alert(handler, "Next player is: "+clientHandlerOfNextPlayer);
            }

        }
        return output;
    }

    public static Message changePhase(GamePhase currentGamePhase,ClientHandler clientHandler){
        Message output = new Message("string");
        sort(currentGamePhase.getCurrentPlayers(),
                Comparator.comparingInt((ClientHandler a) -> a.getGame().getPlayer(a.getUsername()).getFace_up_assistant().getValue())); //players sorted by previous assistant value
        List<String> turnOrder = currentGamePhase.getCurrentPlayers().stream().map(ClientHandler::getUsername).toList();
        //converts list of strings to a string
        String turns = turnOrder.stream().map(Object::toString).collect(Collectors.joining(","));
        clientHandler.updateStatus();
        List<ClientHandler> players = clientHandler.sameMatchPlayers();
        players.remove(clientHandler);
        players.forEach( p-> alert(p,"A new round has begun! Turns order (based on previous assistant card value):\n " + turns + " .You can now play an assistant, type PLAY [x] (type 'HELP' if you need more info)"));
        clientHandler.setAlreadyUpdated(true);
        output.setArgString("A new round has begun! Turns order (based on previous assistant card value):\n " + turns + " .You can now play an assistant, type PLAY [x] (type 'HELP' if you need more info)");
        ClientHandler oldFirstPlayer = currentGamePhase.getCurrentPlayers()
                .stream()
                .filter(p -> (p.isPlayerFirstMove))
                .findAny().orElse(null);
        oldFirstPlayer.setPlayerFirstMove(false);
        ClientHandler firstPlayer = currentGamePhase.getCurrentPlayers().get(0);
        firstPlayer.isPlayerFirstMove = true;

        GamePhase planningPhase = new PlanningPhase(clientHandler.getGame(), currentGamePhase.getCurrentPlayers()); // new game phase
        setGamePhaseForAllPlayers(currentGamePhase.getCurrentPlayers(), planningPhase);
        planningPhase.getCurrentPlayers().forEach(player -> player.getGame().getPlayer(player.getUsername()).setFace_up_assistant(null));
        firstPlayer.getGame().startTurn(); //clouds filled
        setCharacters(clientHandler.getGame().getPlayers());

        clientHandler.sameMatchPlayers().forEach(p->p.getCurrentGamePhase().setTurnOrder(turnOrder));
        return output;
    }
    public static boolean isIsLastRound() {
        return isLastRound;
    }

    public static void setIsLastRound(boolean isLastRound) {
        ActionController.isLastRound = isLastRound;
    }


}
