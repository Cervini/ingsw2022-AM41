package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.ClientHandler;

import java.util.List;

public class PlanningPhase extends GamePhase {

    public PlanningPhase(Game game, List<ClientHandler> players) {
        super(game, players);
        setPlanningPhase(true);
        setActionPhase(false);
    }

    @Override
    public void validatePlayAssistant(ClientHandler clientHandler) throws WrongPhaseException, WrongTurn {

        boolean isFirstPlayer = clientHandler.isPlayerFirstMove; //checks if clienthandler is the first one
        Game game = clientHandler.getGame();
        Player player = game.getPlayer(clientHandler.getUsername());

        boolean assistantPlayed = player.getFace_up_assistant() != null; //checks if player's face up assistant is not null
        if (assistantPlayed) {
            throw new PlanningPhase.WrongTurn("Player already played his assistant card");
        }

        if (!isFirstPlayer) {
            ClientHandler firstPlayerHandler = clientHandler.sameMatchPlayers()
                    .stream()
                    .filter(p -> p.isPlayerFirstMove)
                    .findFirst()
                    .get();
            Player firstPlayer = game.getPlayer(firstPlayerHandler.getUsername());
            boolean firstPlayerPlayed = firstPlayer.getFace_up_assistant() != null;
            if (!firstPlayerPlayed) {
                throw new PlanningPhase.WrongTurn("First player have to play before your move");
            }
            boolean existPlayerBeforeMeThatHaveToPlay = false; //checks if there are no players before you
            for (String id : clientHandler.getCurrentGamePhase().getTurnOrder()) {
                if (id == player.getPlayer_id()) {
                    break;
                }
                boolean playerPlayed = clientHandler.getGame().getPlayer(id).getFace_up_assistant() != null;
                if (!playerPlayed) {
                    existPlayerBeforeMeThatHaveToPlay = true;
                    break;
                }
            }

            if (existPlayerBeforeMeThatHaveToPlay) { // there are players before me
                throw new PlanningPhase.WrongTurn("You have to wait your turn");
            }
        }



    }

}
