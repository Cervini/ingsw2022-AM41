package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.ClientHandler;

import java.util.List;

import static java.util.Collections.sort;

public class GamePhase {
    private GamePhase current_state;
    private Game current_game;
    private Boolean planning_phase = false;
    private Boolean action_phase = false;
    private List<ClientHandler> current_players;

    public GamePhase(Game game, List<ClientHandler> players) {
        current_game = game;
        current_players = players;
    }

    public void validatePlayAssistant(ClientHandler player, GamePhase currentState) throws PlanningPhase.WrongPhaseException, PlanningPhase.WrongTurn {
        if (this instanceof ActionPhase) {
            throw new PlanningPhase.WrongPhaseException();

        }
        //throw new PlanningPhase.WrongPhaseException("Method playAssistant cannot be called from GamePhase parent class");
    }

    public void validatePlaceStudent(ClientHandler player, GamePhase currentState) throws ActionPhase.WrongPhaseException, ActionPhase.WrongTurn {
        if (this instanceof PlanningPhase) {
            throw new ActionPhase.WrongPhaseException();
        }
        throw new ActionPhase.WrongTurn("Method playAssistant cannot be called from GamePhase parent class");
    }


    public void moveStudents(Message play_request, ClientHandler player, GamePhase current_state){}
    public void validateMoveMotherNature(Message play_request, ClientHandler player, GamePhase current_state){}
    public void validateChooseCloud(Message play_request, ClientHandler player, GamePhase current_state){}





    public GamePhase getCurrent_state() {
        return current_state;
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


}
