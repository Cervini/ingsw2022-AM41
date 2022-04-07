package it.polimi.ingsw.am41;

import it.polimi.ingsw.am41.Assistant;
import it.polimi.ingsw.am41.Player;
import it.polimi.ingsw.am41.TowerColour;
import org.junit.Test;

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