package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
            e.printStackTrace();
        }
        try {
            island.putStudent(new Student(Colour.GREEN));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(island.getStudents().size(),2);
        try {
            island.putStudent(new Student(Colour.PINK));
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        assertEquals(island.getStudents().size(),0);
    }

    @Test
    void removeStudent() {
        Island island = new Island();
        try {
            island.putStudent(new Student(Colour.BLUE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            island.removeStudent(new Student(Colour.BLUE));
        } catch (Exception e) {
            e.printStackTrace();
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

    @Test
    void canConquerFalse() {
        Island island = new Island();
        LinkedList<Player> players = new LinkedList<>();
        Player player1 = new Player(TowerColour.BLACK);
        Player player2 = new Player(TowerColour.WHITE);
        players.add(player1);
        players.add(player2);

        player1.getOwned_professor().add(new Professor(Colour.GREEN));
        player1.getOwned_professor().add(new Professor(Colour.RED));
        player2.getOwned_professor().add(new Professor(Colour.PINK));

        for(int i=0; i<3; i++)
            island.putStudent(new Student(Colour.GREEN));

        for(int i=0; i<2; i++)
            island.putStudent(new Student(Colour.PINK));

        island.putStudent(new Student(Colour.RED));

        island.setTower(TowerColour.WHITE);

        assertFalse(island.canConquer(player2, players));
    }

    @Test
    void canConquerTrue() {
        Island island = new Island();
        LinkedList<Player> players = new LinkedList<>();
        Player player1 = new Player(TowerColour.BLACK);
        Player player2 = new Player(TowerColour.WHITE);
        players.add(player1);
        players.add(player2);

        player1.getOwned_professor().add(new Professor(Colour.GREEN));
        player1.getOwned_professor().add(new Professor(Colour.RED));
        player2.getOwned_professor().add(new Professor(Colour.PINK));

        for(int i=0; i<3; i++)
            island.putStudent(new Student(Colour.GREEN));

        for(int i=0; i<2; i++)
            island.putStudent(new Student(Colour.PINK));

        island.putStudent(new Student(Colour.RED));

        island.setTower(TowerColour.WHITE);

        assertTrue(island.canConquer(player1, players));
    }

    @Test
    void canConquerTied() {
        Island island = new Island();
        LinkedList<Player> players = new LinkedList<Player>();
        Player player1 = new Player(TowerColour.BLACK);
        Player player2 = new Player(TowerColour.WHITE);
        players.add(player1);
        players.add(player2);

        player1.getOwned_professor().add(new Professor(Colour.GREEN));
        player1.getOwned_professor().add(new Professor(Colour.RED));
        player2.getOwned_professor().add(new Professor(Colour.PINK));

        for(int i=0; i<3; i++)
            island.putStudent(new Student(Colour.GREEN));

        for(int i=0; i<3; i++)
            island.putStudent(new Student(Colour.PINK));

        island.putStudent(new Student(Colour.RED));

        island.setTower(TowerColour.WHITE);

        assertFalse(island.canConquer(player1, players));
        assertFalse(island.canConquer(player2, players));
    }

    @Test
    void conquer() {
        Island island = new Island();
        LinkedList<Player> players = new LinkedList<>();
        Player player1 = new Player(TowerColour.BLACK);
        Player player2 = new Player(TowerColour.WHITE);
        players.add(player1);
        players.add(player2);

        player1.getOwned_professor().add(new Professor(Colour.GREEN));
        player1.getOwned_professor().add(new Professor(Colour.RED));
        player2.getOwned_professor().add(new Professor(Colour.PINK));

        for(int i=0; i<3; i++)
            island.putStudent(new Student(Colour.GREEN));

        for(int i=0; i<2; i++)
            island.putStudent(new Student(Colour.PINK));

        island.putStudent(new Student(Colour.RED));

        island.setTower(TowerColour.WHITE);

        try {
            island.conquer(player1, players);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(island.getTower(), TowerColour.BLACK);
    }

    @Test
    void conquerTie() {
        Island island = new Island();
        LinkedList<Player> players = new LinkedList<>();
        Player player1 = new Player(TowerColour.BLACK);
        Player player2 = new Player(TowerColour.WHITE);
        players.add(player1);
        players.add(player2);

        player1.getOwned_professor().add(new Professor(Colour.GREEN));
        player1.getOwned_professor().add(new Professor(Colour.RED));
        player2.getOwned_professor().add(new Professor(Colour.PINK));

        for(int i=0; i<3; i++)
            island.putStudent(new Student(Colour.GREEN));

        for(int i=0; i<4; i++)
            island.putStudent(new Student(Colour.PINK));

        island.putStudent(new Student(Colour.RED));

        try {
            island.conquer(player1, players);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNull(island.getTower());
    }
}