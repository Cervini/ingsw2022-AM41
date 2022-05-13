package it.polimi.ingsw.model;

public class GameSession {
    private static Game game = null;

    public static void startGame(int playersCount) {
        game = new Game(playersCount);
    }

    public static Game getCurrentGame() {
        return game;
    }
}
