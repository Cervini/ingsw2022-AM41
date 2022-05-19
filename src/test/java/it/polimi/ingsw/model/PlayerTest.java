package it.polimi.ingsw.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testConstructor(){
        Player player = new Player(TowerColour.BLACK);
        for (Assistant assistant : player.getAssistants()){
            System.out.println("\ninitiative: " + assistant.getValue() +
                    "\nmoves: " + assistant.getMovement_points());
        }
    }

    @Test
    public void compareToGreater(){
        Player player1 = new Player(TowerColour.BLACK);
        Player player2 = new Player(TowerColour.WHITE);
        player1.setFace_up_assistant(new Assistant(3,5));
        player2.setFace_up_assistant(new Assistant(6,10));
        assertEquals(player1.compareTo(player2), -1);
    }

    @Test
    public void compareToSmaller(){
        Player player1 = new Player(TowerColour.BLACK);
        Player player2 = new Player(TowerColour.WHITE);
        player1.setFace_up_assistant(new Assistant(3,5));
        player2.setFace_up_assistant(new Assistant(2,4));
        assertEquals(player1.compareTo(player2), 1);
    }

    @Test
    public void compareToEqual(){
        Player player1 = new Player(TowerColour.BLACK);
        Player player2 = new Player(TowerColour.WHITE);
        player1.setFace_up_assistant(new Assistant(3,5));
        player2.setFace_up_assistant(new Assistant(3,4));
        assertEquals(player1.compareTo(player2), 0);
    }

    @Test
    public void coin_checkSingle(){
        Player player = new Player(TowerColour.BLACK);
        DiningRoom redRoom = player.getSchool().getDining_room(Colour.RED);
        // put two students
        for(int i=0; i<2; i++){
            try {
                redRoom.putStudent(new Student(Colour.RED));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        player.coin_check();
        // should not have earned any coins
        assertEquals(player.getCoins(), 1);

        // put a third student
        try {
            redRoom.putStudent(new Student(Colour.RED));
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.coin_check();

        // should have earned another coin
        assertEquals(player.getCoins(), 2);
    }

    @Test
    public void coin_checkMultiple(){
        Player player = new Player(TowerColour.BLACK);
        DiningRoom redRoom = player.getSchool().getDining_room(Colour.RED);
        DiningRoom blueRoom = player.getSchool().getDining_room(Colour.BLUE);
        DiningRoom yellowRoom = player.getSchool().getDining_room(Colour.YELLOW);
        DiningRoom greenRoom = player.getSchool().getDining_room(Colour.GREEN);

        for(int i=0; i<2; i++){
            try {
                redRoom.putStudent(new Student(Colour.RED));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            blueRoom.putStudent(new Student(Colour.BLUE));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i=0; i<2; i++){
            try {
                yellowRoom.putStudent(new Student(Colour.YELLOW));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(int i=0; i<2; i++){
            try {
                greenRoom.putStudent(new Student(Colour.GREEN));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // put a third student
        try {
            redRoom.putStudent(new Student(Colour.RED));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            greenRoom.putStudent(new Student(Colour.GREEN));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            blueRoom.putStudent(new Student(Colour.BLUE));
        } catch (Exception e) {
            e.printStackTrace();
        }

        player.coin_check();

        // should have earned another coin
        assertEquals(player.getCoins(), 3);
    }

    @Test
    public void playAssistantTest(){
        Player player = new Player(TowerColour.BLACK);
        Assistant assistant = new Assistant(7, 4);

        try {
            player.playAssistant(assistant);
        } catch (Exception e) {
            System.out.println(e);
        }

        assertEquals(player.getFace_up_assistant(), assistant);
    }

    @Test
    public void replayAssistantTest(){
        Player player = new Player(TowerColour.BLACK);
        Assistant assistant = new Assistant(7, 4);

        try {
            player.playAssistant(assistant);
        } catch (Exception e) {
            System.out.println(e);
        }

        assertEquals(player.getFace_up_assistant(), assistant);

        try {
            player.playAssistant(assistant);
        } catch (Exception e) {
            System.out.println(e);
            assertTrue(true);
        }
    }

    @Test
    public void wrongAssistantTest(){
        Player player = new Player(TowerColour.BLACK);
        Assistant assistant = new Assistant(1, 7);

        try {
            player.playAssistant(assistant);
        } catch (Exception e) {
            System.out.println(e);
            assertTrue(true);
        }

        assertNull(player.getFace_up_assistant());
    }
}