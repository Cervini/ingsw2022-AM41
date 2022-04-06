package it.polimi.ingsw.am41;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class DiningRoomTest {

    @Test
    void addRightStudentTest(){
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
    void addWrongStudentTest(){
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
    void removeStudentTest() {
        Student pinkStudent = new Student(Colour.PINK);
        DiningRoom room = new DiningRoom(Colour.PINK);
        try {
            room.putStudent(pinkStudent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Student removed = room.removeStudent();
        assertEquals(removed.getColour(), pinkStudent.getColour());
    }

    @Test
    void hashRightTest(){
        DiningRoom room1 = new DiningRoom(Colour.BLUE);
        DiningRoom room2 = new DiningRoom(Colour.BLUE);
        assertEquals(room1.hashCode(),room2.hashCode());
    }

    @Test
    void hashWrongTest(){
        DiningRoom room1 = new DiningRoom(Colour.BLUE);
        DiningRoom room2 = new DiningRoom(Colour.PINK);
        assertNotEquals(room1.hashCode(),room2.hashCode());
    }

    @Test
    void notEqualsTest(){
        DiningRoom room1 = new DiningRoom(Colour.BLUE);
        DiningRoom room2 = new DiningRoom(Colour.PINK);
        assertEquals(room1.equals(room2), false);
    }

    @Test
    void equalsTest(){
        DiningRoom room1 = new DiningRoom(Colour.BLUE);
        DiningRoom room2 = new DiningRoom(Colour.BLUE);
        assertEquals(room1.equals(room2), true);
    }

    @Test
    void numberEqualsTest(){
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
    void numberNotEqualsTest(){
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
