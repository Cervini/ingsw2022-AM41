package it.polimi.ingsw.am41;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class DiningRoomTest {

    @Test
    void addRightStudentTest(){
        Student pinkStudent = new Student(Color.pink);
        DiningRoom room = new DiningRoom(Color.pink);
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
        Student redStudent = new Student(Color.red);
        DiningRoom room = new DiningRoom(Color.green);
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
        Student pinkStudent = new Student(Color.pink);
        DiningRoom room = new DiningRoom(Color.pink);
        try {
            room.putStudent(pinkStudent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Student removed = room.removeStudent();
        assertEquals(removed.getColor(), pinkStudent.getColor());
    }

    @Test
    void hashRightTest(){
        DiningRoom room1 = new DiningRoom(Color.blue);
        DiningRoom room2 = new DiningRoom(Color.blue);
        assertEquals(room1.hashCode(),room2.hashCode());
    }

    @Test
    void hashWrongTest(){
        DiningRoom room1 = new DiningRoom(Color.blue);
        DiningRoom room2 = new DiningRoom(Color.pink);
        assertNotEquals(room1.hashCode(),room2.hashCode());
    }

    @Test
    void notEqualsTest(){
        DiningRoom room1 = new DiningRoom(Color.blue);
        DiningRoom room2 = new DiningRoom(Color.pink);
        assertEquals(room1.equals(room2), false);
    }

    @Test
    void equalsTest(){
        DiningRoom room1 = new DiningRoom(Color.blue);
        DiningRoom room2 = new DiningRoom(Color.blue);
        assertEquals(room1.equals(room2), true);
    }

    @Test
    void numberEqualsTest(){
        DiningRoom room1 = new DiningRoom(Color.blue);
        DiningRoom room2 = new DiningRoom(Color.blue);
        Student s1 = new Student(Color.blue);
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
        DiningRoom room1 = new DiningRoom(Color.blue);
        DiningRoom room2 = new DiningRoom(Color.blue);
        Student s1 = new Student(Color.blue);
        try {
            room1.putStudent(s1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotEquals(room1.equals(room2), true);
    }
}
