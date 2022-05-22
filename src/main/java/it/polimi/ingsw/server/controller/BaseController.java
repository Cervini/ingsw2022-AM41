package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.ClientHandler;

import java.util.List;

public abstract class BaseController {

    protected static void setGamePhaseForAllPlayers(List<ClientHandler> sameMatchPlayers, GamePhase gamePhase) {
        sameMatchPlayers.stream()
                .forEach(player -> player.setCurrentGamePhase(gamePhase));
    }

}
