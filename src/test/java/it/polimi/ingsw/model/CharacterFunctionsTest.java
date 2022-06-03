package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CharacterFunctionsTest {

    @Test
    void checkInfluenceOnSpecificIslandTest1()  throws Exception{
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

        game = cf.checkInfluenceOnSpecificIsland(game, specificIsland);

        assertEquals(game.getArchipelago().get(7).getColour(), game.getPlayers().getFirst().getTeam());
    }

    @Test
    void checkInfluenceOnSpecificIslandTest2() throws Exception{
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

        game = cf.checkInfluenceOnSpecificIsland(game, specificIsland);

        assertEquals(game.getArchipelago().get(0).getColour(), game.getPlayers().getLast().getTeam());
    }

    @Test
    void checkInfluenceOnSpecificIslandTest3() throws Exception{
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);
        Island specificIsland;

        game.getPlayers().getFirst().addProfessor(new Professor(Colour.BLUE));
        game.getPlayers().getLast().addProfessor(new Professor(Colour.PINK));
        game.getPlayers().getFirst().addProfessor(new Professor(Colour.RED));

        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(1).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(1).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(1).putStudent(new Student(Colour.BLUE));
        game.merge(game.getArchipelago().get(0), game.getArchipelago().get(1));
        game.getArchipelago().get(0).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(0).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(0).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(0).putStudent(new Student(Colour.BLUE));
        specificIsland = game.getArchipelago().get(0);

        game = cf.checkInfluenceOnSpecificIsland(game, specificIsland);

        assertEquals(game.getArchipelago().get(0).getColour(), game.getPlayers().getFirst().getTeam());
    }

    @Test
    void checkInfluenceOnSpecificIslandTest4() throws Exception{
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(3);
        Island specificIsland;

        game.getPlayers().getFirst().addProfessor(new Professor(Colour.BLUE));
        game.getPlayers().getLast().addProfessor(new Professor(Colour.PINK));
        game.getPlayers().getFirst().addProfessor(new Professor(Colour.RED));
        game.getPlayers().get(1).addProfessor(new Professor(Colour.GREEN));
        game.getPlayers().get(1).addProfessor(new Professor(Colour.YELLOW));

        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(7).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(7).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(7).putStudent(new Student(Colour.BLUE));
        game.merge(game.getArchipelago().get(0), game.getArchipelago().get(1));
        game.getArchipelago().get(0).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(0).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(0).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(0).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(1).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(1).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(1).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(1).putStudent(new Student(Colour.YELLOW));
        game.getArchipelago().get(0).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(0).putStudent(new Student(Colour.YELLOW));
        game.merge(game.getArchipelago().get(0), game.getArchipelago().get(1));
        specificIsland = game.getArchipelago().get(0);

        game = cf.checkInfluenceOnSpecificIsland(game, specificIsland);

        assertEquals(game.getArchipelago().get(0).getColour(), game.getPlayers().get(1).getTeam());
    }

    @Test
    void checkInfluenceOnSpecificIslandTest5() throws Exception{
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(4);
        Island specificIsland;

        game.getPlayers().getFirst().addProfessor(new Professor(Colour.BLUE));
        game.getPlayers().getLast().addProfessor(new Professor(Colour.PINK));
        game.getPlayers().getFirst().addProfessor(new Professor(Colour.RED));
        game.getPlayers().get(1).addProfessor(new Professor(Colour.GREEN));
        game.getPlayers().get(1).addProfessor(new Professor(Colour.YELLOW));

        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.merge(game.getArchipelago().get(0), game.getArchipelago().get(1));
        game.getArchipelago().get(0).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(0).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(0).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(0).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(1).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(1).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(1).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(1).putStudent(new Student(Colour.YELLOW));
        game.getArchipelago().get(0).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(0).putStudent(new Student(Colour.YELLOW));
        game.merge(game.getArchipelago().get(0), game.getArchipelago().get(1));
        specificIsland = game.getArchipelago().get(0);

        game = cf.checkInfluenceOnSpecificIsland(game, specificIsland);

        assertEquals(game.getPlayers().getLast().getTeam(), game.getArchipelago().get(0).getColour());
    }

    @Test
    void checkInfluenceWithModifiedBoardTest1() throws Exception{
        //TODO fix, board reset does not work
        /*CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);

        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.checkOwnership();
        game.getPlayers().getLast().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getLast().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(game.getArchipelago().indexOf(game.motherNaturePosition())).putStudent(new Student(Colour.RED));

        game = cf.checkInfluenceWithModifiedBoard(game, game.getPlayers().getLast());

        assertTrue(game.getPlayers().getFirst().getOwned_professor().contains(new Professor(Colour.RED)));

        System.out.println("checkInfluenceWithModifiedBoardTest1 complete");*/
    }

    @Test
    void checkInfluenceWithModifiedBoardTest2() throws Exception{
        //TODO fix
        /*CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);

        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.checkOwnership();
        game.getPlayers().getLast().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getLast().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(game.getArchipelago().indexOf(game.motherNaturePosition())).putStudent(new Student(Colour.RED));

        game = cf.checkInfluenceWithModifiedBoard(game, game.getPlayers().getLast());

        assertTrue(game.getPlayers().getFirst().getOwned_professor().contains(new Professor(Colour.RED)));

        System.out.println("checkInfluenceWithModifiedBoardTest2 complete");*/
    }

    @Test
    void checkInfluenceWithModifiedBoardTest3() throws Exception{
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);

        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.checkOwnership();
        game.getPlayers().getLast().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getLast().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(game.getArchipelago().indexOf(game.motherNaturePosition())).putStudent(new Student(Colour.RED));

        game = cf.checkInfluenceWithModifiedBoard(game, game.getPlayers().getLast());

        assertTrue(game.getPlayers().getLast().getOwned_professor().contains(new Professor(Colour.RED)));
    }

    @Test
    void checkInfluenceWithModifiedBoardTest4() throws Exception{
        Game game = new Game(2);
        boolean ownsProfessor;

        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.checkOwnership();
        game.getPlayers().getLast().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getLast().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(game.getArchipelago().indexOf(game.motherNaturePosition())).putStudent(new Student(Colour.RED));
        game.checkOwnership();
        ownsProfessor = game.getPlayers().getFirst().hasProfessor(Colour.RED);

        assertTrue(ownsProfessor);
    }

    @Test
    void checkInfluenceWithModifiedBoardTest5() throws Exception{
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);
        List<Professor> ownedProfessor1;
        List<Professor> ownedProfessor2;

        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.checkOwnership();
        game.getPlayers().getLast().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getLast().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(game.getArchipelago().indexOf(game.motherNaturePosition())).putStudent(new Student(Colour.RED));
        game.checkOwnership();
        ownedProfessor1 = game.getPlayers().getFirst().getOwned_professor();
        ownedProfessor2 = game.getPlayers().getLast().getOwned_professor();

        game = cf.checkInfluenceWithModifiedBoard(game, game.getPlayers().getLast());

        assertTrue(ownedProfessor1.equals(game.getPlayers().getFirst().getOwned_professor()) && ownedProfessor2.equals(game.getPlayers().getLast().getOwned_professor()));
    }

    @Test
    void checkInfluenceWithModifiedBoardTest6() throws Exception{
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(3);

        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.checkOwnership();
        game.getPlayers().get(1).getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().get(1).getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getLast().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getLast().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(game.getArchipelago().indexOf(game.motherNaturePosition())).putStudent(new Student(Colour.RED));

        game = cf.checkInfluenceWithModifiedBoard(game, game.getPlayers().get(1));

        assertEquals(game.getArchipelago().get(game.getArchipelago().indexOf(game.motherNaturePosition())).getColour(), game.getPlayers().get(1).getTeam());
    }

    @Test
    void checkInfluenceWithoutTowersTest1() throws Exception{
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);
        int islandIndex;

        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.checkOwnership();
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).setTower(game.getPlayers().getLast().getTeam());

        game = cf.checkInfluenceWithoutTowers(game);

        assertEquals(game.getArchipelago().get(islandIndex).getColour(), game.getPlayers().getFirst().getTeam());
    }

    @Test
    void checkInfluenceWithoutTowersTest2() throws Exception{
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);
        int islandIndex;

        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getLast().getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
        game.checkOwnership();
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(islandIndex).setTower(game.getPlayers().getLast().getTeam());

        game = cf.checkInfluenceWithoutTowers(game);

        assertEquals(game.getArchipelago().get(islandIndex).getColour(), game.getPlayers().getLast().getTeam());
    }

    //TODO fix, reset tower does not work
    /*@Test
    void checkInfluenceWithoutTowersTest3() throws Exception{
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);
        int islandIndex;

        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getLast().getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
        game.checkOwnership();
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).setTower(game.getPlayers().getLast().getTeam());
        game.getPlayers().getLast().getSchool().takeTowers(1);

        game = cf.checkInfluenceWithoutTowers(game);

        assertEquals(8, game.getPlayers().getLast().getSchool().getTowers());

        System.out.println("checkInfluenceWithoutTowersTest3 complete");
    }*/

    @Test
    void checkInfluenceWithBonusTest1() throws Exception{
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);
        int islandIndex;

        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getLast().getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
        game.checkOwnership();
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.BLUE));

        game = cf.checkInfluenceWithBonus(game, game.getPlayers().getLast());

        assertEquals(game.getArchipelago().get(islandIndex).getColour(), game.getPlayers().getLast().getTeam());
    }

    @Test
    void checkInfluenceWithBonusTest2() throws Exception{
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(3);
        int islandIndex;

        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getLast().getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
        game.getPlayers().get(1).getSchool().getDining_room(Colour.PINK).putStudent(new Student(Colour.PINK));
        game.getPlayers().get(1).getSchool().getDining_room(Colour.GREEN).putStudent(new Student(Colour.GREEN));
        game.checkOwnership();
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.GREEN));

        game = cf.checkInfluenceWithBonus(game, game.getPlayers().getLast());

        assertEquals(game.getPlayers().getLast().getTeam(), game.getArchipelago().get(islandIndex).getColour());
    }

    @Test
    void checkInfluenceWithBonusTest3() throws Exception{
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(4);
        int islandIndex;

        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getLast().getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
        game.getPlayers().get(1).getSchool().getDining_room(Colour.PINK).putStudent(new Student(Colour.PINK));
        game.getPlayers().get(2).getSchool().getDining_room(Colour.GREEN).putStudent(new Student(Colour.GREEN));
        game.checkOwnership();
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).conquerCheck(game.getPlayers());
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.PINK));

        game = cf.checkInfluenceWithBonus(game, game.getPlayers().getLast());

        assertEquals(game.getPlayers().getLast().getTeam(), game.getArchipelago().get(islandIndex).getColour());
    }

    @Test
    void checkInfluenceWithBonusTest4() throws Exception{
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);
        int islandIndex;

        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getLast().getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
        game.checkOwnership();
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.PINK));

        game = cf.checkInfluenceWithBonus(game, game.getPlayers().getLast());

        assertNull(game.getArchipelago().get(islandIndex).getColour());
    }

    @Test
    void checkInfluenceWithoutColourTest1() throws Exception{
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);
        int islandIndex;

        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getLast().getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
        game.checkOwnership();
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.BLUE));

        game = cf.checkInfluenceWithoutColour(game, Colour.RED);

        assertEquals(game.getPlayers().getLast().getTeam(), game.getArchipelago().get(islandIndex).getColour());
    }

    @Test
    void checkInfluenceWithoutColourTest2() throws Exception{
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(2);
        int islandIndex;

        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().getLast().getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
        game.checkOwnership();
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(islandIndex).conquerCheck(game.getPlayers());

        game = cf.checkInfluenceWithoutColour(game, Colour.RED);

        assertEquals(game.getPlayers().getLast().getTeam(), game.getArchipelago().get(islandIndex).getColour());
    }

    @Test
    void checkInfluenceWithoutColourTest3() throws Exception{
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(3);
        int islandIndex;

        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().get(1).getSchool().getDining_room(Colour.GREEN).putStudent(new Student(Colour.GREEN));
        game.getPlayers().get(1).getSchool().getDining_room(Colour.YELLOW).putStudent(new Student(Colour.YELLOW));
        game.getPlayers().getLast().getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
        game.checkOwnership();
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.YELLOW));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(islandIndex).conquerCheck(game.getPlayers());

        game = cf.checkInfluenceWithoutColour(game, Colour.GREEN);

        assertEquals(game.getPlayers().getLast().getTeam(), game.getArchipelago().get(islandIndex).getColour());
    }

    @Test
    void checkInfluenceWithoutColourTest4() throws Exception{
        CharacterFunctions cf = new CharacterFunctions();
        Game game = new Game(3);
        int islandIndex;

        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.getPlayers().get(1).getSchool().getDining_room(Colour.GREEN).putStudent(new Student(Colour.GREEN));
        game.getPlayers().get(1).getSchool().getDining_room(Colour.YELLOW).putStudent(new Student(Colour.YELLOW));
        game.getPlayers().getLast().getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
        game.checkOwnership();
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.YELLOW));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(islandIndex).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(islandIndex).conquerCheck(game.getPlayers());

        game = cf.checkInfluenceWithoutColour(game, Colour.YELLOW);

        assertEquals(game.getPlayers().get(1).getTeam(), game.getArchipelago().get(islandIndex).getColour());
    }
}