package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColour;
import it.polimi.ingsw.server.ClientHandler;

import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.server.controller.GameController.alert;

public class GameResultsController {

    public Message getStatus(Message request, ClientHandler clientHandler) {
        // TODO: implement
        return null;
    }

    public void getWinner(Game game, ClientHandler player, List<ClientHandler> players){
        if (game.endGame() != null) {
            TowerColour winningTeam = game.endGame();
            ClientHandler winner = null;
            for(ClientHandler p : players){
                if (p.getGame().getPlayer(p.getUsername()).getTeam() == winningTeam){
                    winner = p;
                }
            }
            for (ClientHandler c : players){
                alert(c,"The winner is: " + winner.getUsername());
            }

            //TODO chiudere la partita e non dare più accesso ai giocatori al vecchio gioco
        }

    }

}
