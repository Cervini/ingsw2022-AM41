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

    /**
     * checks whether the game phase and turn order are correct in order to process this command. If so, sets next action for the current player
     * @param request command processed
     * @param clientHandler client who sent the command
     * @param currentGamePhase
     * @return next action to perform
     */
    public static Message place(Message request, ClientHandler clientHandler, GamePhase currentGamePhase) {
        Message response = new Message("string");
        if (clientHandler.getGame() == null){ //notifies client that the game has not started yet
            response.setArgString("Game not started yet");
            return response;
        }
        try {
            GamePhase gamePhase = currentGamePhase.isActionPhase() ? //checks current game phase and casts it if necessary
                    (ActionPhase) currentGamePhase :
                    (PlanningPhase) currentGamePhase;
            gamePhase.validatePlaceStudent(clientHandler); // check if action is allowed (in terms of phase and turns)
            response = processPlace(request, clientHandler); // processes action by modifying the model
            ActionPhase actionPhase = (ActionPhase) gamePhase; // action phase updated with the new action expected from the same player
            if (actionPhase.alreadyMovedThreeStudents(clientHandler)) { //checks if clienthandler has already moved three students
                response.setArgString("Now you have to move mother nature, type MOVE [x] (type 'HELP' if you need \n  more info)"); //notifies clienthandler about next action
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

    /**
     * checks whether the game phase and turn order are correct in order to process this command. If so, sets next action for the current player
     * If there are game ending conditions (number of towers ended or 3 groups of island left) declares a winner/winner team
     * @param request command processed
     * @param clientHandler client who sent the command
     * @param currentGamePhase
     *@return next action to perform
     */
    public static Message move(Message request, ClientHandler clientHandler, GamePhase currentGamePhase) {
        Message response = new Message("string");// set up output message
        if (clientHandler.getGame() == null){
            response.setArgString("Game not started yet");//notifies client that the game has not started yet
            return response;
        }
        try {
            GamePhase gamePhase = currentGamePhase.isActionPhase() ? //checks current game phase and casts it if necessary
                    (ActionPhase) currentGamePhase :
                    (PlanningPhase) currentGamePhase;
            gamePhase.validateMoveMotherNature(clientHandler); // check if action is allowed
            response = processMove(request, clientHandler); // processes action by modifying the model
            ActionPhase actionPhase = (ActionPhase) gamePhase; // action phase updated with the new action expected from the same player
            actionPhase.setNextActionForCurrentPlayer();
            clientHandler.updateStatus();
            response.setArgString("You can now choose a cloud, type CHOOSE [x] (type 'HELP' if you need more info) ");
            clientHandler.setAlreadyUpdated(true);

            Player relevantPlayer =  clientHandler.getGame().getPlayer(clientHandler.getUsername());
            if( clientHandler.getGame().getBag().size() > 0 && relevantPlayer.getAssistants().size()!=0){ //game ends at this point only if player places its last tower or only three groups of islands are left
            TowerColour winner = clientHandler.getGame().endGame();
            if (winner != null ){
                Player winnerPlayer = clientHandler.getGame().getPlayers().stream().filter(p->p.getTeam().equals(winner)).findFirst().get(); //find winner colour team
                List<ClientHandler> players = clientHandler.sameMatchPlayers();
                players
                        .forEach(p->p.setGame(null)); //sets game to null for all players
                players.remove(clientHandler);

                if(clientHandler.sameMatchPlayers().size()<4) { //if there 4 players it declares a winner team (since they play in teams)
                players
                        .forEach(p-> alert(p,"The winner is player: " + winnerPlayer.getPlayer_id()+"! You can now start a new game"));
                response.setArgString("The winner is player: " + winnerPlayer.getPlayer_id()+"! You can now start a new game");
                 } if(clientHandler.sameMatchPlayers().size()==4) {
                    players
                            .forEach(p-> alert(p,"The winner team is: " + winnerPlayer.getTeam().name() +" team! You can now start a new game"));
                    response.setArgString("The winner team is: " + winnerPlayer.getTeam().toString() +"! You can now start a new game");
                }
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

    /**
     * checks whether the game phase and turn order are correct in order to process this command. If so, sets next action for the current player
     * in the event that there are no students left or all assistants have been played it declares a winner/winner team
     * @param request command processed
     * @param clientHandler client who sent the command
     * @param currentGamePhase current game phase
     *@return next action to perform
     */

    public static Message choose(Message request, ClientHandler clientHandler, GamePhase currentGamePhase) {
        Message response = new Message("string");// set up output message
        if (clientHandler.getGame() == null){
            response.setArgString("Game not started yet"); //notifies client that the game has not started yet
            return response;
        }
        try {
            GamePhase gamePhase = currentGamePhase.isActionPhase() ? //checks current game phase and casts it if necessary
                    (ActionPhase) currentGamePhase :
                    (PlanningPhase) currentGamePhase;
            try {
                gamePhase.validateChooseCloud(clientHandler); // check if action is allowed
                response = processChoose(request, clientHandler); // processes action by modifying the model
            }
            catch (ActionPhase.EndingGame e) { //exception thrown by processChoose, sets last round true since there are no more students available
                clientHandler.getGame().setLastRound(true);
            } boolean isLastPlayer =
                            gamePhase.getCurrentPlayers().indexOf(clientHandler) == gamePhase.getCurrentPlayers().size() - 1; // checks if all players have already played this phase
            if (isLastPlayer) { //if the client who sent the command is the last one who has to perform an action during this round
                if (clientHandler.getGame().isLastRound()) { //last round is true only if players have played all assistants or there are no available students
                    response = endGame(clientHandler.getGame(), clientHandler);
                    List<ClientHandler> players = clientHandler.sameMatchPlayers();
                    players
                            .forEach(p->p.setGame(null)); //sets game to null for all players
                    players.remove(clientHandler);
                    Message finalResponse = response;
                    players
                            .forEach(p->  alert(p, finalResponse.getArgString())); //notifies all players about the winner/winner team
                    return response;
                } else { //game goes on since there are no winning conditions
                    response = changePhase(currentGamePhase,clientHandler);
                }
            } else { //round has not ended yet
                ActionPhase actionPhase = (ActionPhase) gamePhase; // action phase updated with the new action expected from the next player
                actionPhase.setNextPlayerAndFirstAction(clientHandler); //sets next player's first action (place student)
                response = updateTurns(clientHandler); //updates all players about turns
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
     * once command has been validated, it is processed by calling model method
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
            case ISLAND -> {// try moving the student from entrance to the selected island
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
     * once command has been validated, it is processed by calling model method
     * @param request message containing the command
     * @param client  client that sent the request to start the game
     * @return a new STRING message containing the action result
     */
    public static Message processMove(Message request, ClientHandler client) throws ActionPhase.WrongAction {
        Message output = new Message("string");// set up output message
        Game currentGame = client.getGame();
        Player currentPlayer = client.getGame().getPlayer(client.getUsername());
        int motherNatureMovements = request.getArgNum1();
        TowerColour playerTeam = currentPlayer.getTeam();
        try {//try moving mother nature as far as player demands
            currentGame.moveMotherNature(motherNatureMovements,currentPlayer);
            if (currentGame.getArchipelago().get(request.getArgNum1()).getTower() == playerTeam){
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
     * once command has been validated, it is processed while checking if there is a game ending condition
     * @param request message containing the command
     * @param client  client that sent the request to start the game
     * @return a new STRING message containing the action result
     */
    private static Message processChoose(Message request, ClientHandler client) throws ActionPhase.WrongAction, ActionPhase.EndingGame {
        Message output = new Message("string");// set up output message
        Game game = client.getGame();
        boolean isLastRound =  client.getGame().getBag().size() == 0;
        if(request.getArgNum1() >= game.getClouds().size()){ //checks the number of clouds available and notifies client
            throw new ActionPhase.WrongAction("This cloud doesn't exist, type a number between 0 and "+(game.getClouds().size()-1));
        }
        if( isLastRound ){ //isLastRound is set true when there are no students available
            throw new ActionPhase.EndingGame("there are no students available");
        }
        if( game.getClouds().get(request.getArgNum1()).getStudents().size() == 0 ){ //checks if the chosen cloud has not been previously chosen by another player
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
     * once the round has ended characters are set to default
     * @param players involved in the same match
     */
    private static void setCharacters(List<Player> players){
        players
                .forEach(player -> player.setPlayedCharacterNumber(-1));
    }

    /**
     * finds the winner and notifies other players
     * @param current_game current game
     * @param client last client who sent a command
     * @return message containing winner or winner team
     */
    private static Message endGame(Game current_game, ClientHandler client) {
        Message output= new Message("string");// set up output message
        TowerColour winnerColour = client.getGame().endGame(); //calls model method which returns a winner
        Player winner = current_game.getPlayers().stream().filter(p -> p.getTeam().equals(winnerColour)).findFirst().get();
        if(client.sameMatchPlayers().size()<4) {
        output.setArgString("The winner is player: " + winner.getPlayer_id()+"! You can now start a new game");
        } if(client.sameMatchPlayers().size()==4) {
            output.setArgString("The winner team is: " + winner.getTeam().name() +" team! You can now start a new game");

        }
        return output;
    }

    /**
     *computes all turns and notifies other players
     * @param clientHandler in order to retrieve same match players
     * @return message containing updated turns
     */
    public static Message updateTurns(ClientHandler clientHandler){
        Message output = new Message("string");// set up output message
        int previousPlayerIdx = clientHandler.getCurrentGamePhase().getTurnOrder().indexOf(clientHandler.getUsername()); //gets index of previous player in turn order list
        String clientHandlerOfNextPlayer = clientHandler.getCurrentGamePhase().getTurnOrder().get(previousPlayerIdx+1);//gets index of next player in turn order list
        clientHandler.updateStatus();
        output.setArgString("Next player is: "+clientHandlerOfNextPlayer);//sets server response
        for (ClientHandler handler : clientHandler.sameMatchPlayers()) {
            handler.setAlreadyUpdated(true);
            if (clientHandler.getGame().isLastRound() && !handler.equals(clientHandler)) { //notifies all clients that this is the last round
                if(clientHandler.getGame().getBag().size() == 0){ //first ending game condition (no students available)
                    alert(handler, "Next player is: "+clientHandlerOfNextPlayer+". This is the last round since there are no students left.");
                    output.setArgString("Next player is: "+clientHandlerOfNextPlayer+". This is the last round since there are no students left.");
                } else { //second game ending condition (all assistants played)
                    alert(handler, "Next player is: "+clientHandlerOfNextPlayer+". This is the last round");
                    output.setArgString("Next player is: "+clientHandlerOfNextPlayer+". This is the last round");
                }
            } else if (!handler.equals(clientHandler)) { //there are no ending conditions
                    alert(handler, "Next player is: "+clientHandlerOfNextPlayer);
            }
            if(handler.getUsername().equals(clientHandlerOfNextPlayer)) handler.setAlreadyUpdated(false);
        }

        return output;
    }

    /**
     *sorts turns by assistants previously played, sets first player, sets same game phase for all players and sets to default face up assistants, clouds and characters
     * @param currentGamePhase current game phase
     * @param clientHandler last client who sent a command
     * @return message containing update turns
     */
    public static Message changePhase(GamePhase currentGamePhase,ClientHandler clientHandler){
        Message output = new Message("string");// set up output message
        sort(currentGamePhase.getCurrentPlayers(),
                Comparator.comparingInt((ClientHandler a) -> a.getGame().getPlayer(a.getUsername()).getFace_up_assistant().getValue())); //players sorted by previously played assistant value
        List<String> turnOrder = currentGamePhase.getCurrentPlayers().stream().map(ClientHandler::getUsername).toList();
        //converts list of strings to a string
        String turns = turnOrder.stream().map(Object::toString).collect(Collectors.joining(","));
        clientHandler.updateStatus();
        List<ClientHandler> players = clientHandler.sameMatchPlayers();
        players.remove(clientHandler);
        players
                .forEach( p-> alert(p,"A new round has begun! Turns order (based on previous assistant card value):\n " + turns + " .You can now play an assistant, type PLAY [x] (type 'HELP' if you need more info)"));
        clientHandler.setAlreadyUpdated(true);
        output.setArgString("A new round has begun! Turns order (based on previous assistant card value):\n " + turns + " .You can now play an assistant, type PLAY [x] (type 'HELP' if you need more info)");
        ClientHandler oldFirstPlayer = currentGamePhase.getCurrentPlayers()
                .stream()
                .filter(p -> (p.isPlayerFirstMove))
                .findAny().orElse(null);
        oldFirstPlayer.setPlayerFirstMove(false); //sets old first player's first move false
        ClientHandler firstPlayer = currentGamePhase.getCurrentPlayers().get(0); //picks new first player
        firstPlayer.isPlayerFirstMove = true; //sets isPlayerFirstMove true

        GamePhase planningPhase = new PlanningPhase(clientHandler.getGame(), currentGamePhase.getCurrentPlayers()); // new game phase
        setGamePhaseForAllPlayers(currentGamePhase.getCurrentPlayers(), planningPhase);//sets same game phase for all players
        planningPhase.getCurrentPlayers()
                .forEach(player -> player.getGame().getPlayer(player.getUsername()).setFace_up_assistant(null)); //sets face up assistants to default
        firstPlayer.getGame().startTurn(); //clouds are re-filled
        setCharacters(clientHandler.getGame().getPlayers()); //characters set to default

        clientHandler.sameMatchPlayers()
                .forEach(p->p.getCurrentGamePhase().setTurnOrder(turnOrder)); //sets for all players same turns order
        return output;
    }



}
