package it.polimi.ingsw.am41;

import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void testConstructor(){
        Player player = new Player(TowerColor.black);
        for (Assistant assistant : player.getAssistants()){
            System.out.println("\ninitiative: " + assistant.getValue() +
                    "\nmoves: " + assistant.getMovement_points());
        }
    }

}