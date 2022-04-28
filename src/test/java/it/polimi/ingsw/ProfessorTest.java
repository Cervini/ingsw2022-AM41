package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorTest {

    @Test
    void testNotEquals() {
        Professor prof1 = new Professor(Colour.RED);
        Professor prof2 = new Professor(Colour.GREEN);
        boolean equal = prof1.equals(prof2);

        assertFalse(equal);
    }

    @Test
    void testEquals() {
        Professor prof1 = new Professor(Colour.RED);
        Professor prof2 = new Professor(Colour.RED);
        boolean equal = prof1.equals(prof2);

        assertTrue(equal);
    }
}