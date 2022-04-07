package it.polimi.ingsw.am41;

import it.polimi.ingsw.am41.Assistant;
import it.polimi.ingsw.am41.Player;
import it.polimi.ingsw.am41.TowerColour;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AssistantTest {

    @Test
    public void compareTo1() {
        Player player = new Player(TowerColour.BLACK);
        Assistant card1 = new Assistant(10,3,player);
        Assistant card2 = new Assistant(5,1,player);
        assertEquals(card1.compareTo(card2),1);
    }

    @Test
    public void compareTo2() {
        Player player = new Player(TowerColour.BLACK);
        Assistant card1 = new Assistant(10,3,player);
        Assistant card2 = new Assistant(5,1,player);
        assertEquals(card2.compareTo(card1),0);
    }

    @Test
    public void compareTo() {
        Player player = new Player(TowerColour.BLACK);
        Assistant card1 = new Assistant(5,3,player);
        Assistant card2 = new Assistant(5,1,player);
        assertEquals(card1.compareTo(card2),1);
    }
}