package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.ClientHandler;

import java.util.List;

public class ActionPhase extends GamePhase {

    public ActionPhase(Game game, List<ClientHandler> players) {
        super(game, players);
        setPlanningPhase(false);
        setActionPhase(true);
    }

    @Override
    public void validatePlaceStudent(ClientHandler clientHandler, GamePhase currentState) throws ActionPhase.WrongPhaseException, ActionPhase.WrongTurn{

    }











    @Override
    public void validateMoveMotherNature(Message play_request, ClientHandler player, GamePhase current_state) {
    }

    @Override
    public void validateChooseCloud(Message play_request, ClientHandler player, GamePhase current_state) {
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
