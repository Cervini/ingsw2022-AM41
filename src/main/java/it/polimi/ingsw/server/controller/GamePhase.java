package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.ClientHandler;

import java.util.List;

public class GamePhase {

    private final Game currentGame;
    private Boolean planningPhase = false;
    private Boolean actionPhase = false;
    private List<ClientHandler> currentPlayers;
    private boolean gameEnded = false;
    private List<String> turnOrder= null;

    public GamePhase(Game game, List<ClientHandler> players) {
        currentGame = game;
        currentPlayers = players;
    }

    public void validatePlayAssistant(ClientHandler player) throws PlanningPhase.WrongPhaseException, PlanningPhase.WrongTurn, GameEndedException {
        if (gameEnded){
            throw new GameEndedException("Game already ended");
        }
        // TODO: validare che il player che sta facendo la mossa non e' unico presente nel gioco in questo momento
        if (this instanceof ActionPhase) {
            throw new PlanningPhase.WrongPhaseException();
        }
        //throw new PlanningPhase.WrongPhaseException("Method playAssistant cannot be called from GamePhase parent class");
    }

    public void validatePlaceStudent(ClientHandler player) throws WrongPhaseException, WrongTurn, ActionPhase.WrongAction, GameEndedException {
        validateCorrectActionGamePhase("Method validatePlaceStudent cannot be called from GamePhase parent class");
    }

    public void validateMoveMotherNature(ClientHandler player) throws WrongPhaseException, WrongTurn, ActionPhase.WrongAction, GameEndedException {
        validateCorrectActionGamePhase("Method validateMoveMotherNature cannot be called from GamePhase parent class");
    }

    public void validateChooseCloud(ClientHandler player) throws WrongPhaseException, WrongTurn, ActionPhase.WrongAction, GameEndedException {
        validateCorrectActionGamePhase("Method validateChooseCloud cannot be called from GamePhase parent class");
    }

    public void validateUse(ClientHandler player) throws WrongPhaseException, WrongTurn, ActionPhase.WrongAction, GameEndedException {
        validateCorrectActionGamePhase("Method validateMoveMotherNature cannot be called from GamePhase parent class");
    }

    private void validateCorrectActionGamePhase(String msg) throws WrongPhaseException, WrongTurn, GameEndedException {
        if (gameEnded){
            throw new GameEndedException("Game already ended");
        }
        // TODO: validare che il player che sta facendo la mossa non e' unico presente nel gioco in questo momento
        if (this instanceof PlanningPhase) {
            throw new WrongPhaseException();
        }
        throw new WrongTurn(msg);
    }

    public Boolean isPlanningPhase() {
        return planningPhase;
    }

    public Boolean isActionPhase() {
        return actionPhase;
    }

    public void setPlanningPhase(Boolean isPlanningPhase) {
        this.planningPhase = isPlanningPhase;
    }

    public void setActionPhase(Boolean isActionPhase) {
        this.actionPhase = isActionPhase;
    }

    public List<ClientHandler> getCurrentPlayers() {
        return currentPlayers;
    }
    public void setCurrentPlayers(List<ClientHandler> current_players) {
        this.currentPlayers = current_players ;
    }

    public static class WrongPhaseException extends Exception {

        public WrongPhaseException() {}
        public WrongPhaseException(String msg) {
            super(msg);
        }
    }

    public static class WrongTurn extends Exception {
        public WrongTurn() {}
        public WrongTurn(String msg) {
            super(msg);
        }
    }
    public static class GameEndedException extends Exception {
        public GameEndedException() {}
        public GameEndedException(String msg) {
            super(msg);
        }
    }

    protected static void setGamePhaseForAllPlayers(List<ClientHandler> sameMatchPlayers, GamePhase gamePhase) {
        sameMatchPlayers.stream()
                .forEach(player -> player.setCurrentGamePhase(gamePhase));
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setTurnOrder(List<String> turnOrder) {
        this.turnOrder = turnOrder;
    }

    public List<String> getTurnOrder() {
        return turnOrder;
    }
}
