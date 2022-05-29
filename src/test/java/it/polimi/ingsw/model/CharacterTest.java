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
        character.effect(game, null, null, null, game.getArchipelago().get(0), null, null);

        assertEquals(initialCost + 1, character.getCost());

        System.out.println("getCostTest2 complete");
    }

    @Test
    void getCostTest3() throws Exception{
        Character character = new Character(4);
        Game game = new Game(2);
        int initialCost;

        initialCost = character.getCost();
        character.effect(game, null, null, null, game.getArchipelago().get(0), null, null);
        character.effect(game, null, null, null, game.getArchipelago().get(0), null, null);

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
        character.effect(game, null, selectedStudent, null, game.getArchipelago().get(0), null, null);

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
        character.effect(game, null, selectedStudent, null, game.getArchipelago().get(0), null, null);

        assertEquals(Colour.RED, game.getArchipelago().get(0).getStudents().get(0).getColour());

        System.out.println("effectTest1.1 complete");
    }

    @Test
    void effectTest2() throws Exception{
        Game game = new Game(2);
        Character character = new Character(0);

        System.out.println("effectTest1 complete");
    }
}