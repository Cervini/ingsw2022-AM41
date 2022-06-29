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

    /**
     * constructor of GamePhase
     * @param game current game
     * @param players players involved in the same match
     */
    public GamePhase(Game game, List<ClientHandler> players) {
        currentGame = game;
        currentPlayers = players;
    }

    /**
     *validates both turn and phase
     * @param player who sent the command
     * @throws PlanningPhase.WrongPhaseException thrown in case of wrong phase
     * @throws PlanningPhase.WrongTurn thrown in case of wrong turn
     * @throws GameEndedException thrown in case of ended game
     */
    public void validatePlayAssistant(ClientHandler player) throws PlanningPhase.WrongPhaseException, PlanningPhase.WrongTurn, GameEndedException {
        if (gameEnded){
            throw new GameEndedException("Game already ended");
        }
        if (this instanceof ActionPhase) {
            throw new PlanningPhase.WrongPhaseException();
        }
        //throw new PlanningPhase.WrongPhaseException("Method playAssistant cannot be called from GamePhase parent class");
    }

    /**
     *validates turn, phase and action type
     * @param player who sent the command
     * @throws WrongPhaseException thrown in case of wrong phase
     * @throws WrongTurn thrown in case of wrong turn
     * @throws ActionPhase.WrongAction thrown in case of wrong action
     * @throws GameEndedException thrown in case of ended game
     */
    public void validatePlaceStudent(ClientHandler player) throws WrongPhaseException, WrongTurn, ActionPhase.WrongAction, GameEndedException {
        validateCorrectActionGamePhase("Method validatePlaceStudent cannot be called from GamePhase parent class");
    }

    /**
     *validates turn, phase and action type
     * @param player who sent the command
     * @throws WrongPhaseException thrown in case of wrong phase
     * @throws WrongTurn thrown in case of wrong turn
     * @throws ActionPhase.WrongAction thrown in case of wrong action
     * @throws GameEndedException thrown in case of ended game
     */
    public void validateMoveMotherNature(ClientHandler player) throws WrongPhaseException, WrongTurn, ActionPhase.WrongAction, GameEndedException {
        validateCorrectActionGamePhase("Method validateMoveMotherNature cannot be called from GamePhase parent class");
    }

    /**
     *validates turn, phase and action type
     * @param player who sent the command
     * @throws WrongPhaseException thrown in case of wrong phase
     * @throws WrongTurn thrown in case of wrong turn
     * @throws ActionPhase.WrongAction thrown in case of wrong action
     * @throws GameEndedException thrown in case of ended game
     */
    public void validateChooseCloud(ClientHandler player) throws WrongPhaseException, WrongTurn, ActionPhase.WrongAction, GameEndedException {
        validateCorrectActionGamePhase("Method validateChooseCloud cannot be called from GamePhase parent class");
    }

    public void validateUse(ClientHandler player) throws WrongPhaseException, WrongTurn, ActionPhase.WrongAction, GameEndedException {
        validateCorrectActionGamePhase("Method validateMoveMotherNature cannot be called from GamePhase parent class");
    }

    /**
     *validates gamePhase
     * @param msg server response
     * @throws WrongPhaseException thrown in case of wrong phase
     * @throws WrongTurn thrown in case of wrong turn
     * @throws GameEndedException thrown in case of ended game
     */
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
