package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterFunctionsTest {

    @Test
    void checkInfluenceOnSpecificIslandTest1() {
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);
        Island specificIsland;

        game.getPlayers().getFirst().addProfessor(new Professor(Colour.BLUE));
        game.getPlayers().getLast().addProfessor(new Professor(Colour.PINK));

        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(7).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(7).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(7).putStudent(new Student(Colour.BLUE));

        specificIsland = game.getArchipelago().get(7);

        cf.checkInfluenceOnSpecificIsland(game, specificIsland);

        assertEquals(game.getArchipelago().get(7).getColour(), game.getPlayers().getFirst().getTeam());

        System.out.println("checkInfluenceOnSpecificIslandTest1 complete");
    }

    @Test
    void checkInfluenceOnSpecificIslandTest2() {
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);
        Island specificIsland;

        game.getPlayers().getFirst().addProfessor(new Professor(Colour.BLUE));
        game.getPlayers().getLast().addProfessor(new Professor(Colour.PINK));

        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(7).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(7).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(7).putStudent(new Student(Colour.BLUE));

        specificIsland = game.getArchipelago().get(0);

        cf.checkInfluenceOnSpecificIsland(game, specificIsland);

        assertEquals(game.getArchipelago().get(0).getColour(), game.getPlayers().getLast().getTeam());

        System.out.println("checkInfluenceOnSpecificIslandTest2 complete");
    }

    @Test
    void checkInfluenceWithModifiedBoardTest1() {
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);

        System.out.println("checkInfluenceWithModifiedBoardTest1 complete");
    }

    @Test
    void checkInfluenceWithoutTowersTest1() {
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);

        System.out.println("checkInfluenceWithoutTowersTest1 complete");
    }

    @Test
    void checkInfluenceWithBonusTest1() {
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);

        System.out.println("checkInfluenceWithBonusTest1 complete");
    }

    @Test
    void checkInfluenceWithoutColourTest1() {
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);

        System.out.println("checkInfluenceWithoutColourTest1 complete");
    }
}