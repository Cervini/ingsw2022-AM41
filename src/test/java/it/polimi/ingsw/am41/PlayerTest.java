package it.polimi.ingsw.am41;


import org.junit.jupiter.api.Test;

public class PlayerTest {

    @Test
    public void testConstructor(){
        Player player = new Player(TowerColour.BLACK);
        for (Assistant assistant : player.getAssistants()){
            System.out.println("\ninitiative: " + assistant.getValue() +
                    "\nmoves: " + assistant.getMovement_points());
        }
    }

}