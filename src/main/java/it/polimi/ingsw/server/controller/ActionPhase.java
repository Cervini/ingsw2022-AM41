package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.ClientHandler;
import java.util.List;

public class ActionPhase extends GamePhase {

    //private static PlayerAction currentPlayerNextAction;

    public ActionPhase(Game game, List<ClientHandler> players) {
        super(game, players);
        setPlanningPhase(false);
        setActionPhase(true);

        Player firstPlayer = players.get(0).getGame().getPlayer(players.get(0).getUsername());
        game.setCurrentPlayerNextAction( new PlayerAction(firstPlayer, PlayerAction.ActionType.MOVE_STUDENT));
        //currentPlayerNextAction = new PlayerAction(firstPlayer, PlayerAction.ActionType.MOVE_STUDENT);
    }

    @Override
    public void validatePlaceStudent(ClientHandler clientHandler) throws WrongPhaseException, WrongTurn, WrongAction {
        // 1. check turn
        //if (clientHandler.isPlayerFirstMove) throw new WrongTurn();
        validatePlayerTurn(clientHandler);

        // 2. check if 3 students have been already moved
        if (alreadyMovedThreeStudents(clientHandler)) {
            //2.1 if true -> player has to move mother nature
            throw new WrongAction("You already moved all students, move Mother Nature now");
        }


    }

    @Override
    public void validateMoveMotherNature(ClientHandler clientHandler) throws WrongTurn, WrongAction {
        validatePlayerTurn(clientHandler); //check turn
        boolean isExpectedAction = getCurrentGame().getCurrentPlayerNextAction().getAction() == PlayerAction.ActionType.MOVE_MOTHER_NATURE;
        if (!isExpectedAction) {
            if (getCurrentGame().getCurrentPlayerNextAction().getAction() == PlayerAction.ActionType.CHOOSE_CLOUD) {
                throw new WrongAction("As next action you have to choose Cloud");
            } else {
                throw new WrongAction("Before move Mother Nature you have to move three students from your schoolboard");
            }
        }

    }

    @Override
    public void validateChooseCloud(ClientHandler clientHandler) throws WrongPhaseException, WrongTurn, WrongAction{
        validatePlayerTurn(clientHandler);
        boolean isExpectedAction = getCurrentGame().getCurrentPlayerNextAction().getAction() == PlayerAction.ActionType.CHOOSE_CLOUD;
        if (!isExpectedAction) {
            throw new WrongAction("Before choose cloud you have to complete students and mother nature movements");
        }

    }
    @Override
    public void validateUse(ClientHandler clientHandler) throws WrongPhaseException, WrongTurn, WrongAction {
        validatePlayerTurn(clientHandler);
    }


    public void setNextActionForCurrentPlayer() {
        getCurrentGame().getCurrentPlayerNextAction().setNextAction();
    }

    public void setNextPlayerAndFirstAction(ClientHandler clientHandlerOfPreviousPlayer) {
        int previousPlayerIdx = this.getCurrentPlayers().indexOf(clientHandlerOfPreviousPlayer);
        ClientHandler clientHandlerOfNextPlayer = this.getCurrentPlayers().get(previousPlayerIdx + 1);
        Player nextPlayer = clientHandlerOfNextPlayer.getGame().getPlayer(clientHandlerOfNextPlayer.getUsername());
        getCurrentGame().setCurrentPlayerNextAction(new PlayerAction(nextPlayer, PlayerAction.ActionType.MOVE_STUDENT));
    }

    public void validatePlayerTurn(ClientHandler clientHandler) throws WrongTurn {
        Player playerThatTryTheMove = clientHandler.getGame().getPlayer(clientHandler.getUsername());
        if (getCurrentGame().getCurrentPlayerNextAction().getPlayer() != playerThatTryTheMove) {
            throw new WrongTurn();
        }
    }

    /**
     * checks if the player can complete PLACE command
     * @param player who sent the command PLACE
     */
    public boolean alreadyMovedThreeStudents(ClientHandler player){

        int entranceSize = player.getGame().getPlayer(player.getUsername()).getSchool().getEntranceSize();
        switch (player.sameMatchPlayers().size()) {
            case 2, 4 -> { if(entranceSize == 4)  return true ;}
            case 3 -> { if(entranceSize == 5)  return true ;}
        }
        return false;
    }

    public static class WrongAction extends Exception {
        public WrongAction() {}

        public WrongAction(String msg) {
            super(msg);
        }
    }
    public static class EndingGame extends Exception {
        public EndingGame() {}

        public EndingGame(String msg) {
            super(msg);
        }
    }

    /*public static PlayerAction getCurrentPlayerNextAction() {
        return currentPlayerNextAction;
    }*/
}
