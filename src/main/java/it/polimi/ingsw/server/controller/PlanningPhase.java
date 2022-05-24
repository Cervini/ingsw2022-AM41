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
        // NEW
        // 1. devo capire se e' il primo player, se si, dobbiamo verificare se ha gia' giocato carta assistente o no
        // 1.1 se ha gia' giocato, ritorniamo un errore
        // 1.2 se no, ok, puo' giocare

        // 2. se il giocatore NON e' primo che gioca, dobbiamo verificare che il primo giocatore ha gia' giocato carta assitente
        // 2.1 se il check del 1o giocatore e' OK, verifichiamo se il giocatore che fa la mossa ha gia' giocato o no
        // 2.2 se ha gia' giocato - errore
        // 2.3 se non ha giocato, controlliamo se prima di lui c'e' qualcun altro che non ha ancora giocato
        // 2.4 se c'e' - errore, se non c'e', OK
        // 2.5 puo' fare la mossa, il metodo di validazione non ritorna nessuna eccezione/errore

        boolean isFirstPlayer = clientHandler.isPlayerFirstMove;
        Game game = clientHandler.getGame();
        Player player = game.getPlayer(clientHandler.getUsername());

        boolean assistantPlayed = player.getFace_up_assistant() != null;
        if (assistantPlayed) {
            throw new PlanningPhase.WrongTurn("Player already played his assistant card");
        }

        if (!isFirstPlayer) {
            // check il primo giocatore ha gia' fatto la mossa
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

            // check se ci sono giocatore prima di me che devono ancora giocare
            boolean existPlayerBeforeMeThatHaveToPlay = false;
            for (Player pl : game.getPlayers()) {
                if (pl.getPlayer_id() == player.getPlayer_id()) {
                    break;
                }
                boolean playerPlayed = pl.getFace_up_assistant() != null;
                if (!playerPlayed) {
                    existPlayerBeforeMeThatHaveToPlay = true;
                    break;
                }
            }

            if (existPlayerBeforeMeThatHaveToPlay) {
                throw new PlanningPhase.WrongTurn("You have to wait your turn");
            }
        }



    }

}
