package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {

    @Test
    void getCharacterNumberTest1() {
        Character character = new Character(1);

        assertEquals(1, character.getCharacterNumber());

        System.out.println("getCharacterNumberTest1 complete");
    }

    @Test
    void getCharacterNumberTest2() {
        Character character = new Character(0);

        assertEquals(0, character.getCharacterNumber());

        System.out.println("getCharacterNumberTest2 complete");
    }

    @Test
    void getCostTest1() {
        Character character = new Character(0);

        assertEquals(1, character.getCost());

        System.out.println("getCostTest1 complete");
    }

    @Test
    void getCostTest2() throws Exception{
        Character character = new Character(4);
        Game game = new Game(2);
        int initialCost;

        initialCost = character.getCost();
        character.effect(game, null, null, null, game.getArchipelago().get(0), null);

        assertEquals(initialCost + 1, character.getCost());

        System.out.println("getCostTest2 complete");
    }

    @Test
    void getCostTest3() throws Exception{
        Character character = new Character(4);
        Game game = new Game(2);
        int initialCost;

        initialCost = character.getCost();
        character.effect(game, null, null, null, game.getArchipelago().get(0), null);
        character.effect(game, null, null, null, game.getArchipelago().get(0), null);

        assertEquals(initialCost + 1, character.getCost());

        System.out.println("getCostTest2 complete");
    }

    @Test
    void effectTest1_0() throws Exception{
        Game game = new Game(2);
        Character character = new Character(0);
        int initialBagSize;
        LinkedList<Student> selectedStudent = new LinkedList<>();

        character.addStudent(new Student(Colour.RED));
        character.addStudent(new Student(Colour.BLUE));
        character.addStudent(new Student(Colour.GREEN));
        character.addStudent(new Student(Colour.YELLOW));
        initialBagSize = game.getBag().size();
        selectedStudent.add(character.getStudents().get(0));
        character.effect(game, null, selectedStudent, null, game.getArchipelago().get(0), null);

        assertEquals(initialBagSize - 1, game.getBag().size());

        System.out.println("effectTest1.0 complete");
    }

    @Test
    void effectTest1_1() throws Exception{
        Game game = new Game(2);
        Character character = new Character(0);
        LinkedList<Student> selectedStudent = new LinkedList<>();

        character.addStudent(new Student(Colour.RED));
        character.addStudent(new Student(Colour.BLUE));
        character.addStudent(new Student(Colour.GREEN));
        character.addStudent(new Student(Colour.YELLOW));
        selectedStudent.add(character.getStudents().get(0));
        character.effect(game, null, selectedStudent, null, game.getArchipelago().get(0), null);

        assertEquals(Colour.RED, game.getArchipelago().get(0).getStudents().get(0).getColour());

        System.out.println("effectTest1.1 complete");
    }

    @Test
    void effectTest2_0() throws Exception{
        Game game = new Game(2);
        Character character = new Character(1);

        //Need to fix checkInfluenceWithModifiedBoard berfore testing

        System.out.println("effectTest2.0 complete");
    }

    @Test
    void effectTest3_0() throws Exception{
        Game game = new Game(2);
        Character character = new Character(2);
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

        game = character.effect(game, null, null, null, specificIsland, null);

        assertEquals(game.getArchipelago().get(7).getColour(), game.getPlayers().getFirst().getTeam());

        System.out.println("effectTest3.0 complete");
    }

    @Test
    void effectTest3_1() throws Exception{
        Game game = new Game(2);
        Character character = new Character(2);
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

        game = character.effect(game, null, null, null, specificIsland, null);

        assertEquals(game.getPlayers().getLast().getTeam(), game.getArchipelago().get(0).getColour());

        System.out.println("effectTest3.1 complete");
    }

    @Test
    void effectTest4_0() throws Exception{
        Game game = new Game(2);
        Character character = new Character(3);
        Assistant assistant;
        int initialMaxMovement;

        assistant = game.getPlayers().getFirst().getAssistants().get(0);
        initialMaxMovement = assistant.getMovement_points();
        game.getPlayers().getFirst().playAssistant(0);

        //game = character.effect(game, game.getPlayers().getFirst(), null, null, null, null);
        //TODO fix
        //assertEquals(initialMaxMovement + 2, game.getPlayers().getFirst().getFace_up_assistant().getMovement_points());

        System.out.println("effectTest4.0 complete");
    }

    @Test
    void effectTest5_0() throws Exception{
        Game game = new Game(2);
        Character character = new Character(4);

        game = character.effect(game, null, null, null, game.getArchipelago().get(1), null);

        assertTrue(game.getArchipelago().get(1).getNo_entry());

        System.out.println("effectTest5.0 complete");
    }

    @Test
    void effectTest5_1() throws Exception{
        Game game = new Game(2);
        Character character = new Character(4);

        game.getCharacters().add(character);
        game = character.effect(game, null, null, null, game.getArchipelago().get(1), null);

        assertTrue(game.getArchipelago().get(1).getNo_entry());
        game.getPlayers().getFirst().playAssistant(0);
        game.moveMotherNature(1, game.getPlayers().getFirst());

        assertFalse(game.getArchipelago().get(1).getNo_entry());

        System.out.println("effectTest5.1 complete");
    }

    @Test
    void effectTest5_2() throws Exception{
        Game game = new Game(2);
        Character character = new Character(4);

        game.getCharacters().add(character);
        game = character.effect(game, null, null, null, game.getArchipelago().get(1), null);

        game.getArchipelago().get(1).putStudent(new Student(Colour.RED));
        game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
        game.checkOwnership();
        assertTrue(game.getArchipelago().get(1).getNo_entry());
        game.getPlayers().getFirst().playAssistant(0);
        game.moveMotherNature(1, game.getPlayers().getFirst());

        assertNull(game.getArchipelago().get(1).getColour());

        System.out.println("effectTest5.2 complete");
    }

    @Test
    void effectTest6_0() throws Exception{
        Game game = new Game(2);
        Character character = new Character(5);

        //Need to fix checkInfluenceWithoutTowers before testing

        System.out.println("effectTest6.0 complete");
    }

    @Test
    void effectTest7_0() throws Exception{
        Game game = new Game(2);
        Character character = new Character(6);
        LinkedList<Student> studentsToRemove = new LinkedList<>();
        LinkedList<Student> studentsToAdd = new LinkedList<>();
        LinkedList<Student> expectedOutput = new LinkedList<>();

        character.addStudent(new Student(Colour.RED));
        character.addStudent(new Student(Colour.RED));
        character.addStudent(new Student(Colour.RED));
        character.addStudent(new Student(Colour.BLUE));
        character.addStudent(new Student(Colour.BLUE));
        character.addStudent(new Student(Colour.BLUE));
        game.getPlayers().getFirst().getSchool().getEntrance().clear();
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.BLUE));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.GREEN));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.YELLOW));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.PINK));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.RED));
        studentsToAdd.add(new Student(Colour.RED));
        studentsToRemove.add(new Student(Colour.PINK));
        expectedOutput.add(new Student(Colour.BLUE));
        expectedOutput.add(new Student(Colour.GREEN));
        expectedOutput.add(new Student(Colour.YELLOW));
        expectedOutput.add(new Student(Colour.RED));
        expectedOutput.add(new Student(Colour.RED));

        game = character.effect(game, game.getPlayers().getFirst(), studentsToRemove, studentsToAdd, null, null);

        assertEquals(expectedOutput, game.getPlayers().getFirst().getSchool().getEntrance());

        System.out.println("effectTest7.0 complete");
    }

    @Test
    void effectTest7_1() throws Exception{
        Game game = new Game(2);
        Character character = new Character(6);
        LinkedList<Student> studentsToRemove = new LinkedList<>();
        LinkedList<Student> studentsToAdd = new LinkedList<>();
        LinkedList<Student> expectedOutput = new LinkedList<>();

        character.getStudents().clear();
        character.addStudent(new Student(Colour.RED));
        character.addStudent(new Student(Colour.RED));
        character.addStudent(new Student(Colour.RED));
        character.addStudent(new Student(Colour.BLUE));
        character.addStudent(new Student(Colour.BLUE));
        character.addStudent(new Student(Colour.BLUE));
        game.getPlayers().getFirst().getSchool().getEntrance().clear();
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.BLUE));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.GREEN));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.YELLOW));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.PINK));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.RED));
        studentsToAdd.add(new Student(Colour.RED));
        studentsToRemove.add(new Student(Colour.PINK));
        expectedOutput.add(new Student(Colour.RED));
        expectedOutput.add(new Student(Colour.RED));
        expectedOutput.add(new Student(Colour.BLUE));
        expectedOutput.add(new Student(Colour.BLUE));
        expectedOutput.add(new Student(Colour.BLUE));
        expectedOutput.add(new Student(Colour.PINK));

        game = character.effect(game, game.getPlayers().getFirst(), studentsToRemove, studentsToAdd, null, null);

        assertEquals(expectedOutput, character.getStudents());

        System.out.println("effectTest7.1 complete");
    }

    @Test
    void effectTest7_2() throws Exception{
        Game game = new Game(2);
        Character character = new Character(6);
        LinkedList<Student> studentsToRemove = new LinkedList<>();
        LinkedList<Student> studentsToAdd = new LinkedList<>();
        LinkedList<Student> expectedOutput = new LinkedList<>();

        character.addStudent(new Student(Colour.RED));
        character.addStudent(new Student(Colour.RED));
        character.addStudent(new Student(Colour.RED));
        character.addStudent(new Student(Colour.BLUE));
        character.addStudent(new Student(Colour.BLUE));
        character.addStudent(new Student(Colour.BLUE));
        game.getPlayers().getFirst().getSchool().getEntrance().clear();
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.BLUE));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.GREEN));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.PINK));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.PINK));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.PINK));
        studentsToAdd.add(new Student(Colour.RED));
        studentsToRemove.add(new Student(Colour.PINK));
        expectedOutput.add(new Student(Colour.BLUE));
        expectedOutput.add(new Student(Colour.GREEN));
        expectedOutput.add(new Student(Colour.PINK));
        expectedOutput.add(new Student(Colour.PINK));
        expectedOutput.add(new Student(Colour.RED));

        game = character.effect(game, game.getPlayers().getFirst(), studentsToRemove, studentsToAdd, null, null);

        assertEquals(expectedOutput, game.getPlayers().getFirst().getSchool().getEntrance());

        System.out.println("effectTest7.2 complete");
    }

    @Test
    void effectTest7_3() throws Exception{
        Game game = new Game(2);
        Character character = new Character(6);
        LinkedList<Student> studentsToRemove = new LinkedList<>();
        LinkedList<Student> studentsToAdd = new LinkedList<>();
        LinkedList<Student> expectedOutput = new LinkedList<>();

        character.addStudent(new Student(Colour.RED));
        character.addStudent(new Student(Colour.RED));
        character.addStudent(new Student(Colour.RED));
        character.addStudent(new Student(Colour.BLUE));
        character.addStudent(new Student(Colour.BLUE));
        character.addStudent(new Student(Colour.BLUE));
        game.getPlayers().getFirst().getSchool().getEntrance().clear();
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.BLUE));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.GREEN));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.PINK));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.PINK));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.PINK));
        studentsToAdd.add(new Student(Colour.RED));
        studentsToAdd.add(new Student(Colour.RED));
        studentsToRemove.add(new Student(Colour.BLUE));
        studentsToRemove.add(new Student(Colour.GREEN));
        expectedOutput.add(new Student(Colour.PINK));
        expectedOutput.add(new Student(Colour.PINK));
        expectedOutput.add(new Student(Colour.PINK));
        expectedOutput.add(new Student(Colour.RED));
        expectedOutput.add(new Student(Colour.RED));

        game = character.effect(game, game.getPlayers().getFirst(), studentsToRemove, studentsToAdd, null, null);

        assertEquals(expectedOutput, game.getPlayers().getFirst().getSchool().getEntrance());

        System.out.println("effectTest7.3 complete");
    }
}