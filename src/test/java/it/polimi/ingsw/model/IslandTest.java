package it.polimi.ingsw.model;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {

    @Test
    void putStudent() {
        Island island = new Island();
        assertEquals(island.getStudents().size(),0);
        try {
            island.putStudent(new Student(Colour.BLUE));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            island.putStudent(new Student(Colour.GREEN));
        } catch (Exception e) {
            System.out.println(e);
        }
        assertEquals(island.getStudents().size(),2);
        try {
            island.putStudent(new Student(Colour.PINK));
        } catch (Exception e) {
            System.out.println(e);
        }
        assertEquals(island.getStudents().size(),3);
    }

    @Test
    void putStudentNull() {
        Island island = new Island();
        assertEquals(island.getStudents().size(),0);
        try {
            island.putStudent(null);
        } catch (Exception e) {
            System.out.println(e);
        }
        assertEquals(island.getStudents().size(),0);
    }

    @Test
    void removeStudent() {
        Island island = new Island();
        try {
            island.putStudent(new Student(Colour.BLUE));
        } catch (Exception e) {
            System.out.println(e);
        }
        try{
            island.removeStudent(new Student(Colour.BLUE));
        } catch (Exception e) {
            System.out.println(e);
        }
        assertEquals(island.getStudents().size(), 1);
    }

    @Test
    void influenceTest(){
        Player player = new Player(TowerColour.BLACK);
        Island island = new Island();

        player.getOwned_professor().add(new Professor(Colour.GREEN));
        player.getOwned_professor().add(new Professor(Colour.RED));

        for(int i=0; i<3; i++)
            island.putStudent(new Student(Colour.GREEN));

        for(int i=0; i<2; i++)
            island.putStudent(new Student(Colour.PINK));

        island.putStudent(new Student(Colour.RED));

        assertEquals(island.influence(player), 4);
    }

    @Test
    void influenceTest2(){
        Player player = new Player(TowerColour.BLACK);
        Island island = new Island();

        player.getOwned_professor().add(new Professor(Colour.GREEN));
        player.getOwned_professor().add(new Professor(Colour.RED));

        for(int i=0; i<3; i++)
            island.putStudent(new Student(Colour.GREEN));

        for(int i=0; i<2; i++)
            island.putStudent(new Student(Colour.PINK));

        island.putStudent(new Student(Colour.RED));

        island.setTower(TowerColour.BLACK);

        assertEquals(island.influence(player), 5);
    }

    @Test
    void influenceTest3(){
        Player player = new Player(TowerColour.BLACK);
        Island island = new Island();

        player.getOwned_professor().add(new Professor(Colour.GREEN));
        player.getOwned_professor().add(new Professor(Colour.RED));

        for(int i=0; i<3; i++)
            island.putStudent(new Student(Colour.GREEN));

        for(int i=0; i<2; i++)
            island.putStudent(new Student(Colour.PINK));

        island.putStudent(new Student(Colour.RED));

        island.setTower(TowerColour.WHITE);

        assertEquals(island.influence(player), 4);
    }

    @Test
    void influenceTest4(){
        Player player = new Player(TowerColour.BLACK);
        Island island = new Island();

        player.getOwned_professor().add(new Professor(Colour.GREEN));
        player.getOwned_professor().add(new Professor(Colour.RED));

        island.setTower(TowerColour.WHITE);

        assertEquals(island.influence(player), 0);
    }

    //TODO improve conquerCheck testing

    @Test
    void conquerCheckTest1() throws Exception{
        Game game = new Game(2);

        game.getPlayers().getFirst().addProfessor(new Professor(Colour.BLUE));
        game.getPlayers().getFirst().addProfessor(new Professor(Colour.RED));
        game.getPlayers().getLast().addProfessor(new Professor(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(0).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(0).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(0).putStudent(new Student(Colour.BLUE));

        game.getArchipelago().get(0).conquerCheck(game.getPlayers());

        assertEquals(game.getPlayers().getFirst().getTeam(), game.getArchipelago().get(0).getColour());

        System.out.println("conquerCheckTest1 complete");
    }

    @Test
    void conquerCheckTest2() throws Exception{
        Game game = new Game(3);

        game.getPlayers().getFirst().addProfessor(new Professor(Colour.BLUE));
        game.getPlayers().getFirst().addProfessor(new Professor(Colour.RED));
        game.getPlayers().get(1).addProfessor(new Professor(Colour.GREEN));
        game.getPlayers().getLast().addProfessor(new Professor(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(0).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(0).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(0).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(0).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(0).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(0).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(0).putStudent(new Student(Colour.GREEN));

        game.getArchipelago().get(0).conquerCheck(game.getPlayers());

        assertEquals(game.getPlayers().get(1).getTeam(), game.getArchipelago().get(0).getColour());

        System.out.println("conquerCheckTest2 complete");
    }

    @Test
    void conquerCheckTest3() throws Exception{
        Game game = new Game(3);

        game.getPlayers().getFirst().addProfessor(new Professor(Colour.BLUE));
        game.getPlayers().getFirst().addProfessor(new Professor(Colour.RED));
        game.getPlayers().get(1).addProfessor(new Professor(Colour.GREEN));
        game.getPlayers().getLast().addProfessor(new Professor(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(0).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(0).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(0).putStudent(new Student(Colour.GREEN));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.PINK));
        game.getArchipelago().get(0).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(0).putStudent(new Student(Colour.RED));
        game.getArchipelago().get(0).putStudent(new Student(Colour.BLUE));
        game.getArchipelago().get(0).putStudent(new Student(Colour.BLUE));

        game.getArchipelago().get(0).conquerCheck(game.getPlayers());

        assertNull(game.getArchipelago().get(0).getColour());

        System.out.println("conquerCheckTest3 complete");
    }
}