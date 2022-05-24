package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.server.ClientHandler;

import java.util.List;
import java.util.Objects;

public class ActionPhase extends GamePhase {
    private PlayerAction currentPlayerNextAction;

    public ActionPhase(Game game, List<ClientHandler> players) {
        super(game, players);
        setPlanningPhase(false);
        setActionPhase(true);

        Player firstPlayer = players.get(0).getGame().getPlayer(players.get(0).getUsername());
        this.currentPlayerNextAction = new PlayerAction(firstPlayer, PlayerAction.ActionType.MOVE_STUDENT);
    }

    @Override
    public void validatePlaceStudent(ClientHandler clientHandler) throws WrongPhaseException, WrongTurn, WrongAction {

        //Il giocatore deve: spostare 3 studenti -> muovere madre natura -> scegliere la nuvola
        //spostare tre studenti = entrance school board con 3 studenti in meno

        // 1. controllo che è il suo turno

        //if (clientHandler.isPlayerFirstMove) throw new WrongTurn();
        validatePlayerTurn(clientHandler);

        // 2. controllo se ha già mosso tre studenti
        if (alreadyMovedThreeStudents(clientHandler)) {
            throw new WrongAction("You already moved all students, move Mother Nature now");
        }

        // 2.1 se ne ha già mossi 3 eccezione= devi muovere madre natura
        //2.2 se ne ha mossi meno di tre processPlace
    }

    @Override
    public void validateMoveMotherNature(ClientHandler clientHandler) throws WrongPhaseException, WrongTurn, WrongAction {
        validatePlayerTurn(clientHandler);

        boolean isExpectedAction = currentPlayerNextAction.getAction() == PlayerAction.ActionType.MOVE_MOTHER_NATURE;
        if (!isExpectedAction) {
            if (currentPlayerNextAction.getAction() == PlayerAction.ActionType.CHOOSE_CLOUD) {
                throw new WrongAction("As next action you have to choose Cloud");
            } else {
                throw new WrongAction("Before move Mother Nature you should complete students movement");
            }
        }

        // TODO: Can't move Mother Nature this far, please retry
    }

    @Override
    public void validateChooseCloud(ClientHandler clientHandler) throws WrongPhaseException, WrongTurn, WrongAction {
        validatePlayerTurn(clientHandler);

        boolean isExpectedAction = currentPlayerNextAction.getAction() == PlayerAction.ActionType.CHOOSE_CLOUD;
        if (!isExpectedAction) {
            throw new WrongAction("Before choose cloud you have to complete students and mother nature movements");
        }
    }

    public void setNextActionForCurrentPlayer() {
        currentPlayerNextAction.setNextAction();
    }

    public void setNextPlayerAndFirstAction(ClientHandler clientHandlerOfPreviousPlayer) {
        int previousPlayerIdx = this.getCurrentPlayers().indexOf(clientHandlerOfPreviousPlayer);
        ClientHandler clientHandlerOfNextPlayer = this.getCurrentPlayers().get(previousPlayerIdx + 1);
        Player nextPlayer = clientHandlerOfNextPlayer.getGame().getPlayer(clientHandlerOfNextPlayer.getUsername());
        currentPlayerNextAction = new PlayerAction(nextPlayer, PlayerAction.ActionType.MOVE_STUDENT);
    }

    private void validatePlayerTurn(ClientHandler clientHandler) throws WrongTurn {
        Player playerThatTryTheMove = clientHandler.getGame().getPlayer(clientHandler.getUsername());
        if (currentPlayerNextAction.getPlayer() != playerThatTryTheMove) {
            throw new WrongTurn();
        }
    }

    public boolean alreadyMovedThreeStudents(ClientHandler player){
        int entranceSize = 0;
        Player currentPlayer = player.getGame().getPlayer(player.getUsername());
        for (Student s: currentPlayer.getSchool().getEntrance()){
            if (s != null ) entranceSize++;
        }

        switch (player.sameMatchPlayers().size()) {

            case 2 -> { if(entranceSize == 4)  return true ;}

            case 3 -> { if(entranceSize == 6)  return true ;}

        }

        return false;
    }

    public static class WrongAction extends Exception {
        public WrongAction() {}

        public WrongAction(String msg) {
            super(msg);
        }
    }

}
