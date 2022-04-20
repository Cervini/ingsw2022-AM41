package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorTest {

    @Test
    void testNotEquals() {
        Professor prof1 = new Professor(Colour.RED);
        Professor prof2 = new Professor(Colour.GREEN);

        assertNotEquals(prof1, prof2);
    }

    @Test
    void testEquals() {
        Professor prof1 = new Professor(Colour.RED);
        Professor prof2 = new Professor(Colour.RED);

        assertEquals(prof1, prof2);
    }
}