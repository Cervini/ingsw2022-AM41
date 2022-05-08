package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameConclusionChecksTest {

    private final static int minSize = 3;

    @Test
    void endBecauseOfArchipelagoSizeTest1() {
        GameConclusionChecks check = new GameConclusionChecks();
        Game game = new Game(2);
        TowerColour winner = null;

        for(int i = 0; i < 9; i++){
            game.merge(game.getArchipelago().get(0), game.getArchipelago().get(1));
            game.getArchipelago().get(0).setTower(TowerColour.BLACK);
        }
        game.getArchipelago().get(1).setTower(TowerColour.WHITE);
        game.getArchipelago().get(2).setTower(TowerColour.WHITE);

        winner = check.endBecauseOfArchipelagoSize(minSize, game.getArchipelago(), game.getPlayers());

        assertTrue(winner.equals(TowerColour.BLACK));

        System.out.println("endBecauseOfArchipelagoSizeTest1 complete");
    }

    @Test
    void endBecauseOfArchipelagoSizeTest2() {
        GameConclusionChecks check = new GameConclusionChecks();
        Game game = new Game(2);
        TowerColour winner = null;

        for(int i = 0; i < 8; i++){
            game.merge(game.getArchipelago().get(0), game.getArchipelago().get(1));
            game.getArchipelago().get(0).setTower(TowerColour.BLACK);
        }
        game.getArchipelago().get(1).setTower(TowerColour.WHITE);
        game.getArchipelago().get(2).setTower(TowerColour.WHITE);

        winner = check.endBecauseOfArchipelagoSize(minSize, game.getArchipelago(), game.getPlayers());

        assertTrue(winner == null);

        System.out.println("endBecauseOfArchipelagoSizeTest2 complete");
    }

    @Test
    void endBecauseOfArchipelagoSizeTest3() {
        GameConclusionChecks check = new GameConclusionChecks();
        Game game = new Game(3);
        TowerColour winner = null;

        for(int i = 0; i < 9; i++){
            game.merge(game.getArchipelago().get(0), game.getArchipelago().get(1));
            game.getArchipelago().get(0).setTower(TowerColour.GREY);
        }
        game.getArchipelago().get(1).setTower(TowerColour.BLACK);
        game.getArchipelago().get(2).setTower(TowerColour.WHITE);

        winner = check.endBecauseOfArchipelagoSize(minSize, game.getArchipelago(), game.getPlayers());

        assertTrue(winner.equals(TowerColour.GREY));

        System.out.println("endBecauseOfArchipelagoSizeTest3 complete");
    }

    @Test
    void endBecauseOfArchipelagoSizeTest4() {
        GameConclusionChecks check = new GameConclusionChecks();
        Game game = new Game(4);
        TowerColour winner = null;

        for(int i = 0; i < 9; i++){
            game.merge(game.getArchipelago().get(0), game.getArchipelago().get(1));
            game.getArchipelago().get(0).setTower(TowerColour.WHITE);
        }
        game.getArchipelago().get(1).setTower(TowerColour.BLACK);
        game.getArchipelago().get(2).setTower(TowerColour.WHITE);

        winner = check.endBecauseOfArchipelagoSize(minSize, game.getArchipelago(), game.getPlayers());

        assertTrue(winner.equals(TowerColour.WHITE));

        System.out.println("endBecauseOfArchipelagoSizeTest4 complete");
    }

    @Test
    void endBecauseAvailableStudentsFinishedTest1() {
        GameConclusionChecks check = new GameConclusionChecks();
        Game game = new Game(2);
        TowerColour winner = null;

        winner = check.endBecauseAvailableStudentsFinished(game.getBag(), game.getArchipelago(), game.getPlayers(), winner);

        assertTrue(winner == null);

        System.out.println("endBecauseAvailableStudentsFinishedTest1 complete");
    }

    @Test
    void endBecauseAvailableStudentsFinishedTest2() {
        GameConclusionChecks check = new GameConclusionChecks();
        Game game = new Game(2);
        TowerColour winner = null;

        game.getBag().clear();
        for(int i = 0; i < 9; i++){
            game.merge(game.getArchipelago().get(0), game.getArchipelago().get(1));
            game.getArchipelago().get(0).setTower(TowerColour.BLACK);
        }
        game.getArchipelago().get(1).setTower(TowerColour.WHITE);
        game.getArchipelago().get(2).setTower(TowerColour.WHITE);


        winner = check.endBecauseAvailableStudentsFinished(game.getBag(), game.getArchipelago(), game.getPlayers(), winner);

        assertTrue(winner.equals(TowerColour.BLACK));

        System.out.println("endBecauseAvailableStudentsFinishedTest2 complete");
    }

    @Test
    void endBecauseAvailableStudentsFinishedTest3() {
        GameConclusionChecks check = new GameConclusionChecks();
        Game game = new Game(3);
        TowerColour winner = null;

        game.getBag().clear();
        game.getArchipelago().get(0).setTower(TowerColour.BLACK);
        game.getArchipelago().get(1).setTower(TowerColour.WHITE);
        game.getArchipelago().get(2).setTower(TowerColour.WHITE);
        game.getArchipelago().get(3).setTower(TowerColour.GREY);
        game.getArchipelago().get(4).setTower(TowerColour.GREY);
        game.getArchipelago().get(5).setTower(TowerColour.GREY);
        game.getArchipelago().get(6).setTower(TowerColour.GREY);
        game.getArchipelago().get(10).setTower(TowerColour.BLACK);

        winner = check.endBecauseAvailableStudentsFinished(game.getBag(), game.getArchipelago(), game.getPlayers(), winner);

        assertTrue(winner.equals(TowerColour.GREY));

        System.out.println("endBecauseAvailableStudentsFinishedTest3 complete");
    }

    @Test
    void endBecauseAvailableStudentsFinishedTest4() throws Exception {
        GameConclusionChecks check = new GameConclusionChecks();
        Game game = new Game(4);
        TowerColour winner = null;
        Student student = new Student(Colour.RED);

        game.getBag().clear();
        game.getArchipelago().get(0).setTower(TowerColour.BLACK);
        game.getArchipelago().get(1).setTower(TowerColour.WHITE);
        game.getArchipelago().get(3).setTower(TowerColour.WHITE);
        game.getArchipelago().get(4).setTower(TowerColour.BLACK);
        game.getArchipelago().get(5).setTower(TowerColour.WHITE);
        game.getArchipelago().get(6).setTower(TowerColour.WHITE);
        game.getArchipelago().get(10).setTower(TowerColour.BLACK);
        game.getArchipelago().get(11).setTower(TowerColour.BLACK);
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(student);
        game.checkOwnership();

        winner = check.endBecauseAvailableStudentsFinished(game.getBag(), game.getArchipelago(), game.getPlayers(), winner);

        assertTrue(winner.equals(TowerColour.WHITE));

        System.out.println("endBecauseAvailableStudentsFinishedTest4 complete");
    }

    @Test
    void endBecauseAvailableAssistantsFinishedTest1() {
        GameConclusionChecks check = new GameConclusionChecks();
        Game game = new Game(2);
        TowerColour winner = null;

        game.getPlayers().getFirst().getAssistants().remove(0);

        winner = check.endBecauseAvailableAssistantsFinished(game.getArchipelago(), game.getPlayers(), winner);

        assertTrue(winner == null);

        System.out.println("endBecauseAvailableAssistantsFinishedTest1 complete");
    }

    @Test
    void endBecauseAvailableAssistantsFinishedTest2() throws Exception{
        GameConclusionChecks check = new GameConclusionChecks();
        Game game = new Game(2);
        TowerColour winner = null;

        game.getPlayers().getFirst().getAssistants().clear();
        game.getPlayers().getLast().getAssistants().clear();
        game.getArchipelago().get(0).setTower(TowerColour.BLACK);
        game.getArchipelago().get(1).setTower(TowerColour.WHITE);
        game.getArchipelago().get(3).setTower(TowerColour.WHITE);
        game.getArchipelago().get(4).setTower(TowerColour.BLACK);
        game.getArchipelago().get(5).setTower(TowerColour.WHITE);

        winner = check.endBecauseAvailableAssistantsFinished(game.getArchipelago(), game.getPlayers(), winner);

        assertTrue(winner.equals(TowerColour.WHITE));

        System.out.println("endBecauseAvailableAssistantsFinishedTest2 complete");
    }

    @Test
    void endBecauseAvailableAssistantsFinishedTest3() throws Exception{
        GameConclusionChecks check = new GameConclusionChecks();
        Game game = new Game(3);
        TowerColour winner = null;

        game.getPlayers().getFirst().getAssistants().clear();
        game.getPlayers().getLast().getAssistants().clear();
        game.getArchipelago().get(0).setTower(TowerColour.BLACK);
        game.getArchipelago().get(1).setTower(TowerColour.WHITE);
        game.getArchipelago().get(9).setTower(TowerColour.GREY);
        game.getArchipelago().get(10).setTower(TowerColour.GREY);
        game.getArchipelago().get(11).setTower(TowerColour.GREY);


        winner = check.endBecauseAvailableAssistantsFinished(game.getArchipelago(), game.getPlayers(), winner);

        assertTrue(winner.equals(TowerColour.GREY));

        System.out.println("endBecauseAvailableAssistantsFinishedTest3 complete");
    }

    @Test
    void endBecauseAvailableAssistantsFinishedTest4() throws Exception{
        GameConclusionChecks check = new GameConclusionChecks();
        Game game = new Game(4);
        TowerColour winner = null;
        Student student = new Student(Colour.RED);

        game.getPlayers().getFirst().getAssistants().clear();
        game.getPlayers().getLast().getAssistants().clear();
        game.getArchipelago().get(0).setTower(TowerColour.BLACK);
        game.getArchipelago().get(1).setTower(TowerColour.WHITE);
        game.getPlayers().get(1).getSchool().getDining_room(Colour.RED).putStudent(student);
        game.checkOwnership();

        winner = check.endBecauseAvailableAssistantsFinished(game.getArchipelago(), game.getPlayers(), winner);

        assertTrue(winner.equals(TowerColour.BLACK));

        System.out.println("endBecauseAvailableAssistantsFinishedTest4 complete");
    }

    @Test
    void endBecauseAvailableTowersFinishedTest1() throws Exception{
        GameConclusionChecks check = new GameConclusionChecks();
        Game game = new Game(2);
        game.getPlayers().getFirst().getSchool().takeTowers(8);
        TowerColour winner = null;

        winner = check.endBecauseAvailableTowersFinished(game.getPlayers(), winner);

        assertTrue(winner.equals(TowerColour.WHITE));

        System.out.println("endBecauseAvailableTowersFinishedTest1 complete");
    }

    @Test
    void endBecauseAvailableTowersFinishedTest2() throws Exception{
        GameConclusionChecks check = new GameConclusionChecks();
        Game game = new Game(3);
        game.getPlayers().getLast().getSchool().takeTowers(6);
        TowerColour winner = null;

        winner = check.endBecauseAvailableTowersFinished(game.getPlayers(), winner);

        assertTrue(winner.equals(TowerColour.GREY));

        System.out.println("endBecauseAvailableTowersFinishedTest2 complete");
    }

    @Test
    void endBecauseAvailableTowersFinishedTest3() throws Exception{
        GameConclusionChecks check = new GameConclusionChecks();
        Game game = new Game(4);
        game.getPlayers().getFirst().getSchool().takeTowers(6);
        TowerColour winner = TowerColour.BLACK;

        winner = check.endBecauseAvailableTowersFinished(game.getPlayers(), winner);

        assertTrue(winner.equals(TowerColour.BLACK));

        System.out.println("endBecauseAvailableTowersFinishedTest3 complete");
    }

    @Test
    void endBecauseAvailableTowersFinishedTest4() throws Exception{
        GameConclusionChecks check = new GameConclusionChecks();
        Game game = new Game(4);
        game.getPlayers().getFirst().getSchool().takeTowers(6);
        game.getPlayers().get(1).getSchool().takeTowers(4);
        TowerColour winner = null;

        winner = check.endBecauseAvailableTowersFinished(game.getPlayers(), winner);

        assertTrue(winner == null);

        System.out.println("endBecauseAvailableTowersFinishedTest4 complete");
    }

    @Test
    void endBecauseAvailableTowersFinishedTest5() throws Exception{
        GameConclusionChecks check = new GameConclusionChecks();
        Game game = new Game(4);
        game.getPlayers().getFirst().getSchool().takeTowers(6);
        game.getPlayers().get(1).getSchool().takeTowers(8);
        TowerColour winner = null;

        winner = check.endBecauseAvailableTowersFinished(game.getPlayers(), winner);

        assertTrue(winner.equals(TowerColour.BLACK));

        System.out.println("endBecauseAvailableTowersFinishedTest5 complete");
    }
}