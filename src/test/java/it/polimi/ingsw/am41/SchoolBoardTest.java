package it.polimi.ingsw.am41;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class SchoolBoardTest {
    @Test
    @DisplayName("Builder Test")
    void testConstructor(){
        SchoolBoard s = new SchoolBoard();
        ArrayList<DiningRoom> m = new ArrayList<DiningRoom>();
        m.add(new DiningRoom(Color.red));
        m.add(new DiningRoom(Color.green));
        m.add(new DiningRoom(Color.blue));
        m.add(new DiningRoom(Color.yellow));
        m.add(new DiningRoom(Color.pink));

        assertEquals(s.getDining_rooms(), m);
    }

    @Test
    void putStudentTest(){
        SchoolBoard s = new SchoolBoard();
        Student student = new Student(Color.yellow);
        try {
            s.putStudent(student);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(s.getEntranceSize(), 1);
    }

    @Test
    void fullEntranceTest(){
        SchoolBoard s = new SchoolBoard();
        Student student = new Student(Color.yellow);
        int x=0;
        for(int i=0; i<10; i++){
            try {
                s.putStudent(student);

            } catch (Exception e) {
                e.printStackTrace();
            }
            x++;
            if(x>9)
                x=9;
            assertEquals(s.getEntranceSize(), x);
        }
    }
}
