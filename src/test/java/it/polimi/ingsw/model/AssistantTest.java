package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssistantTest {

    @Test
    public void compareTo1() {
        Player player = new Player(TowerColour.BLACK);
        Assistant card1 = new Assistant(10,3);
        Assistant card2 = new Assistant(5,1);
        assertEquals(card1.compareTo(card2),1);
    }

    @Test
    public void compareTo2() {
        Player player = new Player(TowerColour.BLACK);
        Assistant card1 = new Assistant(10,3);
        Assistant card2 = new Assistant(5,1);
        assertEquals(card2.compareTo(card1),0);
    }

    @Test
    public void compareTo() {
        Player player = new Player(TowerColour.BLACK);
        Assistant card1 = new Assistant(5,3);
        Assistant card2 = new Assistant(5,1);
        assertEquals(card1.compareTo(card2),1);
    }

    @Test
    @DisplayName("Constructor Test")
    public void constructorTest(){
        Assistant assistant = new Assistant(3,2);
        assertEquals(assistant.getValue(),3);
        assertEquals(assistant.getMovement_points(),2);
    }
}