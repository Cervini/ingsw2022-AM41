package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameSession;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.util.List;

public class GameController {

    public Message start(Message request, ClientHandler client) {
        Message output = new Message("string");
        int available = availableClients(client.getClients());//count available clients
        if(available>=4){ // if there are at least 4 available players
            Game game = new Game(4);
            setAsPlaying(4, game, client.getClients());
        } else if (available<2) {
            output.setArgString("Not enough players, wait some time then retry.");
        } else {
            Game game = new Game(available);
            setAsPlaying(available, game, client.getClients());
        }
        return output;
    }

    /**
     * @return number of Clients not participating in a game
     */
    private int availableClients(List<ClientHandler> clients){
        return (int) clients.stream().filter(ClientHandler::isAvailable).count();
    }


    /**
     * @param numberOfPlayers
     * @param game
     * @param clients
     * Set the first 'NumberOfPlayers' available clients as playing the 'game' instance
     */
    private void setAsPlaying(int numberOfPlayers, Game game, List<ClientHandler> clients){
        int count = 0;
        for(ClientHandler handler: clients){
            if(handler.isAvailable()){
                handler.setGame(game);
                game.getPlayers().get(count).setPlayer_id(handler.getUsername());
                Message alert = new Message("string");
                alert.setArgString("Game of " + numberOfPlayers + " started");
                try {
                    handler.getOut().writeObject(alert);
                    handler.getOut().flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                count++;
                if(count>=numberOfPlayers)
                    break;
            }
        }
    }








    public Message place(Message request, ClientHandler clientHandler) {

        // TODO: implement
        return null;
    }

    public Message move(Message request, ClientHandler clientHandler) {
        Game game = GameSession.getCurrentGame();
        // prima di fare la mossa dobbiamo validare se e' OK in questo momento del gioco
        // step di validazione
        // TODO
        // se ok, faccio la mossa
        // TODO: implement
        return null;
    }
}
