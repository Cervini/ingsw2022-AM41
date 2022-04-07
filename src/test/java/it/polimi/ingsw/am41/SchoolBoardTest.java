package it.polimi.ingsw.am41;

import it.polimi.ingsw.am41.Colour;
import it.polimi.ingsw.am41.DiningRoom;
import it.polimi.ingsw.am41.SchoolBoard;
import it.polimi.ingsw.am41.Student;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class SchoolBoardTest {
    @Test
    public void testConstructor(){
        SchoolBoard s = new SchoolBoard();
        ArrayList<DiningRoom> m = new ArrayList<DiningRoom>();
        m.add(new DiningRoom(Colour.RED));
        m.add(new DiningRoom(Colour.GREEN));
        m.add(new DiningRoom(Colour.BLUE));
        m.add(new DiningRoom(Colour.YELLOW));
        m.add(new DiningRoom(Colour.PINK));

        Collections.sort(m);
        Collections.sort(s.getDining_rooms());

        assertEquals(m.equals(s.getDining_rooms()),true);
        }

    @Test
    public void putStudentTest(){
        SchoolBoard s = new SchoolBoard();
        Student student = new Student(Colour.YELLOW);
        try {
            s.putStudent(student);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(s.getEntranceSize(), 1);
    }

    @Test
    public void fullEntranceTest(){
        SchoolBoard s = new SchoolBoard();
        Student student = new Student(Colour.YELLOW);
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
