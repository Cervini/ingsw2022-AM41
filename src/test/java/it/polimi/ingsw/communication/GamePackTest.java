package it.polimi.ingsw.communication;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

class GamePackTest {

    @Test
    void printPack1() {
        Game game = new Game(4);
        GamePack pack = new GamePack(game);
        pack.printPack();
    }

    @Test
    void printPack2() {
        Game game = new Game(4);
        List<Island> archipelago = game.getArchipelago();
        game.merge(archipelago.get(4), archipelago.get(5));
        game.getArchipelago().get(4).setTower(TowerColour.WHITE);

        game.merge(archipelago.get(1), archipelago.get(2));
        game.getArchipelago().get(1).setTower(TowerColour.BLACK);

        game.merge(archipelago.get(7), archipelago.get(8));
        game.getArchipelago().get(7).setTower(TowerColour.GREY);

        try {
            game.getPlayers().get(0).playAssistant(5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            game.getPlayers().get(3).playAssistant(7);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        game.moveProfessor(Colour.RED, game.getPlayers().getFirst());
        game.moveProfessor(Colour.BLUE, game.getPlayers().getFirst());

        game.moveProfessor(Colour.GREEN, game.getPlayers().getLast());
        game.moveProfessor(Colour.YELLOW, game.getPlayers().get(2));

        GamePack pack = new GamePack(game);
        pack.printPack();
    }
}