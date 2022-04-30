package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import javax.management.monitor.GaugeMonitor;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class GameSetupTest {

    @Test
    void playerSetupTest1() {
        GameSetup setup = new GameSetup();
        int numberOfPlayers = 2;
        LinkedList<Player> players;

        players = setup.playerSetup(numberOfPlayers);

        assertTrue(players.size() == 2 && players.getFirst().getTeam().equals(TowerColour.WHITE) && players.getLast().getTeam().equals(TowerColour.BLACK));

        System.out.println("playerSetupTest1 complete");
    }

    @Test
    void playerSetupTest2() {
        GameSetup setup = new GameSetup();
        int numberOfPlayers = 3;
        LinkedList<Player> players;

        players = setup.playerSetup(numberOfPlayers);

        assertTrue(players.size() == 3 && players.getFirst().getTeam().equals(TowerColour.WHITE) && players.get(1).getTeam().equals(TowerColour.BLACK) && players.getLast().getTeam().equals(TowerColour.GREY));

        System.out.println("playerSetupTest2 complete");
    }

    @Test
    void playerSetupTest3() {
        GameSetup setup = new GameSetup();
        int numberOfPlayers = 4;
        LinkedList<Player> players;

        players = setup.playerSetup(numberOfPlayers);

        assertTrue(players.size() == 4 && players.getFirst().getTeam().equals(TowerColour.WHITE) && players.get(1).getTeam().equals(TowerColour.BLACK) && players.get(2).getTeam().equals(TowerColour.WHITE) && players.getLast().getTeam().equals(TowerColour.BLACK));

        System.out.println("playerSetupTest3 complete");
    }

    @Test
    void coinSetupTest1() {
        GameSetup setup = new GameSetup();
        int numberOfPlayers = 2;
        int numberOfCoins;
        int predictedNumberOfCoins = 18;

        numberOfCoins = setup.coinSetup(numberOfPlayers);

        assertEquals(numberOfCoins, predictedNumberOfCoins);

        System.out.println("coinSetupTest1 complete");
    }

    @Test
    void coinSetupTest2() {
        GameSetup setup = new GameSetup();
        int numberOfPlayers = 3;
        int numberOfCoins;
        int predictedNumberOfCoins = 17;

        numberOfCoins = setup.coinSetup(numberOfPlayers);

        assertEquals(numberOfCoins, predictedNumberOfCoins);

        System.out.println("coinSetupTest2 complete");
    }

    @Test
    void coinSetupTest3() {
        GameSetup setup = new GameSetup();
        int numberOfPlayers = 4;
        int numberOfCoins;
        int predictedNumberOfCoins = 16;

        numberOfCoins = setup.coinSetup(numberOfPlayers);

        assertEquals(numberOfCoins, predictedNumberOfCoins);

        System.out.println("coinSetupTest3 complete");
    }

    @Test
    void archipelagoSetup() {
    }

    @Test
    void bagSetup() {
    }

    @Test
    void professorSetup() {
    }
}