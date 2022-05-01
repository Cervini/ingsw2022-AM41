package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import javax.management.monitor.GaugeMonitor;
import java.util.LinkedList;
import java.util.List;

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
    void archipelagoSetupTest1() {
        GameSetup setup = new GameSetup();
        List<Island> archipelago;

        archipelago = setup.archipelagoSetup();

        assertTrue(archipelago.get(0).isMother_nature());

        System.out.println("archipelagoSetupTest1 complete");
    }

    @Test
    void archipelagoSetupTest2() {
        GameSetup setup = new GameSetup();
        List<Island> archipelago;

        archipelago = setup.archipelagoSetup();

        assertTrue(archipelago.get(6).getStudents().isEmpty());

        System.out.println("archipelagoSetupTest2 complete");
    }

    @Test
    void archipelagoSetupTest3() {
        GameSetup setup = new GameSetup();
        List<Island> archipelago;
        int redCounter = 0;
        int blueCounter = 0;
        int yellowCounter = 0;
        int greenCounter = 0;
        int pinkCounter = 0;

        archipelago = setup.archipelagoSetup();
        for(Island island: archipelago){
            if(!island.isMother_nature() && !island.getStudents().isEmpty()) {
                if (island.getStudents().get(0).getColour().equals(Colour.RED)) {
                    redCounter++;
                } else if (island.getStudents().get(0).getColour().equals(Colour.BLUE)) {
                    blueCounter++;
                } else if (island.getStudents().get(0).getColour().equals(Colour.YELLOW)) {
                    yellowCounter++;
                } else if (island.getStudents().get(0).getColour().equals(Colour.GREEN)) {
                    greenCounter++;
                } else if (island.getStudents().get(0).getColour().equals(Colour.PINK)) {
                    pinkCounter++;
                }
            }
        }

        assertTrue(redCounter == 2 && blueCounter == 2 && yellowCounter == 2 && greenCounter == 2 && pinkCounter == 2);

        System.out.println("archipelagoSetupTest3 complete");
    }

    @Test
    void bagSetupTest1() {
        GameSetup setup = new GameSetup();
        int numberOfStudents = 120;
        LinkedList<Student> bag;

        bag = setup.bagSetup(numberOfStudents);

        assertTrue(bag.size() == 120);

        System.out.println("bagSetupTest1 complete");
    }

    @Test
    void bagSetupTest2() {
        GameSetup setup = new GameSetup();
        int numberOfStudents = 120;
        LinkedList<Student> bag;

        bag = setup.bagSetup(numberOfStudents);

        assertTrue(bag.size() == 120);

        System.out.println("bagSetupTest2 complete");
    }

    @Test
    void bagSetupTest3() {
        GameSetup setup = new GameSetup();
        int numberOfStudents = 120;
        LinkedList<Student> bag;
        int redStudentCounter = 0;
        int blueStudentCounter = 0;
        int yellowStudentCounter = 0;
        int greenStudentCounter = 0;
        int pinkStudentCounter = 0;

        bag = setup.bagSetup(numberOfStudents);
        for(Student student: bag){
            if(student.getColour().equals(Colour.RED)){
                redStudentCounter++;
            }else if(student.getColour().equals(Colour.BLUE)){
                blueStudentCounter++;
            }else if(student.getColour().equals(Colour.YELLOW)){
                yellowStudentCounter++;
            }else if(student.getColour().equals(Colour.GREEN)){
                greenStudentCounter++;
            }else if(student.getColour().equals(Colour.PINK)){
                pinkStudentCounter++;
            }
        }

        assertTrue(redStudentCounter == 24 && blueStudentCounter == 24 && greenStudentCounter == 24 && yellowStudentCounter == 24 && pinkStudentCounter == 24);

        System.out.println("bagSetupTest3 complete");
    }

    @Test
    void bagSetupTest4() {
        GameSetup setup = new GameSetup();
        int numberOfStudents = 120;
        LinkedList<Student> bag1;
        LinkedList<Student> bag2;

        bag1 = setup.bagSetup(numberOfStudents);
        bag2 = setup.bagSetup(numberOfStudents);

        assertFalse(bag1.equals(bag2));

        System.out.println("bagSetupTest4 complete");
    }

    @Test
    void professorSetupTest1() {
        GameSetup setup = new GameSetup();
        List<Professor> professors;

        professors = setup.professorSetup();

        assertTrue(professors.get(0).getColour().equals(Colour.BLUE) && professors.get(1).getColour().equals(Colour.RED) && professors.get(2).getColour().equals(Colour.GREEN) && professors.get(3).getColour().equals(Colour.YELLOW) && professors.get(4).getColour().equals(Colour.PINK));

        System.out.println("professorSetupTest1 complete");
    }

    @Test
    void professorSetupTest2() {
        GameSetup setup = new GameSetup();
        List<Professor> professors;

        professors = setup.professorSetup();

        assertTrue(professors.size() == 5);

        System.out.println("professorSetupTest2 complete");
    }
}