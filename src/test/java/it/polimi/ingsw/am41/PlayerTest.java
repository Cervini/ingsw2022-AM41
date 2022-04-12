package it.polimi.ingsw.am41;


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
        player1.setFace_up_assistant(new Assistant(3,5, player1));
        player2.setFace_up_assistant(new Assistant(6,10, player2));
        assertEquals(player1.compareTo(player2), -1);
    }

    @Test
    public void compareToSmaller(){
        Player player1 = new Player(TowerColour.BLACK);
        Player player2 = new Player(TowerColour.WHITE);
        player1.setFace_up_assistant(new Assistant(3,5, player1));
        player2.setFace_up_assistant(new Assistant(2,4, player2));
        assertEquals(player1.compareTo(player2), 1);
    }

    @Test
    public void compareToEqual(){
        Player player1 = new Player(TowerColour.BLACK);
        Player player2 = new Player(TowerColour.WHITE);
        player1.setFace_up_assistant(new Assistant(3,5, player1));
        player2.setFace_up_assistant(new Assistant(3,4, player2));
        assertEquals(player1.compareTo(player2), 0);
    }
}