package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SchoolBoardTest {

    @Test
    public void testConstructor(){
        SchoolBoard s = new SchoolBoard(new Player(TowerColour.BLACK));
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
        SchoolBoard s = new SchoolBoard(new Player(TowerColour.BLACK));
        Student student = new Student(Colour.YELLOW);
        try {
            s.putStudent(student);
        } catch (Exception e) {
            System.out.println(e);
        }
        assertEquals(s.getEntranceSize(), 1);
    }

    @Test
    public void fullEntranceTest(){
        SchoolBoard s = new SchoolBoard(new Player(TowerColour.BLACK));
        Student student = new Student(Colour.YELLOW);
        int x=0;
        for(int i=0; i<10; i++){
            try {
                s.putStudent(student);

            } catch (Exception e) {
                System.out.println(e);
            }
            x++;
            if(x>9)
                x=9;
            assertEquals(s.getEntranceSize(), x);
        }
    }
}
