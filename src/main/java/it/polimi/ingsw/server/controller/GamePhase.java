package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.ClientHandler;

import java.util.List;

public class GamePhase {

    private Game current_game;
    private Boolean planning_phase = false;
    private Boolean action_phase = false;
    private List<ClientHandler> current_players;

    public GamePhase(Game game, List<ClientHandler> players) {
        current_game = game;
        current_players = players;
    }

    public void validatePlayAssistant(ClientHandler player) throws PlanningPhase.WrongPhaseException, PlanningPhase.WrongTurn {
        if (this instanceof ActionPhase) {
            throw new PlanningPhase.WrongPhaseException();

        }
        //throw new PlanningPhase.WrongPhaseException("Method playAssistant cannot be called from GamePhase parent class");
    }

    public void validatePlaceStudent(ClientHandler player) throws WrongPhaseException, WrongTurn, ActionPhase.WrongAction {
        validateCorrectGamePhase("Method validatePlaceStudent cannot be called from GamePhase parent class");
    }

    public void validateMoveMotherNature(ClientHandler player) throws WrongPhaseException, WrongTurn, ActionPhase.WrongAction {
        validateCorrectGamePhase("Method validateMoveMotherNature cannot be called from GamePhase parent class");
    }

    public void validateChooseCloud(ClientHandler player) throws WrongPhaseException, WrongTurn, ActionPhase.WrongAction {
        validateCorrectGamePhase("Method validateChooseCloud cannot be called from GamePhase parent class");
    }

    private void validateCorrectGamePhase(String msg) throws WrongPhaseException, WrongTurn {
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
}
