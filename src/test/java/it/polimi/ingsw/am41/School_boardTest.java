package it.polimi.ingsw.am41;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class School_boardTest {
    @Test
    @DisplayName("Builder Test")
    void testConstructor(){
        School_board s = new School_board();
        ArrayList<Dining_room> m = new ArrayList<Dining_room>();
        m.add(new Dining_room(Color.red));
        m.add(new Dining_room(Color.green));
        m.add(new Dining_room(Color.blue));
        m.add(new Dining_room(Color.yellow));
        m.add(new Dining_room(Color.pink));

        assertEquals(s.getDining_rooms(), m);
    }
}
