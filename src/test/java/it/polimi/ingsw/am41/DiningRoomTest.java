package it.polimi.ingsw.am41;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DiningRoomTest {

    @Test
    public void addRightStudentTest(){
        Student pinkStudent = new Student(Colour.PINK);
        DiningRoom room = new DiningRoom(Colour.PINK);
        assertEquals(room.getStudents(),0);
        try{
            room.putStudent(pinkStudent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(room.getStudents(),1);
    }

    @Test
    public void addWrongStudentTest(){
        Student redStudent = new Student(Colour.RED);
        DiningRoom room = new DiningRoom(Colour.GREEN);
        assertEquals(room.getStudents(),0);
        try{
            room.putStudent(redStudent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(room.getStudents(),0);
    }

    @Test
    public void removeStudentTest() {
        Student pinkStudent = new Student(Colour.PINK);
        DiningRoom room = new DiningRoom(Colour.PINK);
        try {
            room.putStudent(pinkStudent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            room.removeStudent(pinkStudent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(room.getStudents(), 0);
    }

    @Test
    public void notEqualsTest(){
        DiningRoom room1 = new DiningRoom(Colour.BLUE);
        DiningRoom room2 = new DiningRoom(Colour.PINK);
        assertEquals(room1.equals(room2), false);
    }

    @Test
    public void equalsTest(){
        DiningRoom room1 = new DiningRoom(Colour.BLUE);
        DiningRoom room2 = new DiningRoom(Colour.BLUE);
        assertEquals(room1.equals(room2), true);
    }

    @Test
    public void numberEqualsTest(){
        DiningRoom room1 = new DiningRoom(Colour.BLUE);
        DiningRoom room2 = new DiningRoom(Colour.BLUE);
        Student s1 = new Student(Colour.BLUE);
        try {
            room1.putStudent(s1);
            room2.putStudent(s1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(room1.equals(room2), true);
    }

    @Test
    public void numberNotEqualsTest(){
        DiningRoom room1 = new DiningRoom(Colour.BLUE);
        DiningRoom room2 = new DiningRoom(Colour.BLUE);
        Student s1 = new Student(Colour.BLUE);
        try {
            room1.putStudent(s1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotEquals(room1.equals(room2), true);
    }
}
