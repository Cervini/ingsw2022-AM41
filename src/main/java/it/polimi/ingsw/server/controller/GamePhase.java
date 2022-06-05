package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.ClientHandler;

import java.util.List;

public class GamePhase {

    private final Game current_game;
    private Boolean planning_phase = false;
    private Boolean action_phase = false;
    private final List<ClientHandler> current_players;
    private boolean gameEnded = false;

    public GamePhase(Game game, List<ClientHandler> players) {
        current_game = game;
        current_players = players;
    }

    public void validatePlayAssistant(ClientHandler player) throws PlanningPhase.WrongPhaseException, PlanningPhase.WrongTurn, GameEndedException {
        if (gameEnded){
            throw new GameEndedException("Game already ended");
        }
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

    private void validateCorrectActionGamePhase(String msg) throws WrongPhaseException, WrongTurn, GameEndedException {
        if (gameEnded){
            throw new GameEndedException("Game already ended");
        }
        if (this instanceof PlanningPhase) {
            throw new WrongPhaseException();
        }
        throw new WrongTurn(msg);
    }

    public Boolean isPlanningPhase() {
        return planning_phase;
    }

    public Boolean isActionPhase() {
        return action_phase;
    }

    public void setPlanningPhase(Boolean isPlanningPhase) {
        this.planning_phase = isPlanningPhase;
    }

    public void setActionPhase(Boolean isActionPhase) {
        this.action_phase = isActionPhase;
    }

    public List<ClientHandler> getCurrentPlayers() {
        return current_players;
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

    public Game getCurrent_game() {
        return current_game;
    }
}
