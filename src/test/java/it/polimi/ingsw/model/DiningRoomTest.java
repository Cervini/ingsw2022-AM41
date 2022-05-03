package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.DiningRoom;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DiningRoomTest {

    @Test
    public void addRightStudentTest(){
        Student pinkStudent = new Student(Colour.PINK);
        DiningRoom room = new DiningRoom(Colour.PINK);
        assertEquals(room.getStudents(),0);
        try{
            room.putStudent(pinkStudent);
        } catch (Exception e) {
            System.out.println(e);
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
            System.out.println(e);
        }
        assertEquals(room.getStudents(),0);
    }

    @Test
    public void gainCoinTest(){
        Student pinkStudent = new Student(Colour.PINK);
        DiningRoom room = new DiningRoom(Colour.PINK);
        assertEquals(room.getStudents(),0);
        assertEquals(room.getGiven_coins(), 0);
        for(int i=0; i<3; i++){
            try{
                room.putStudent(pinkStudent);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        assertEquals(room.getStudents(),3);
        assertEquals(room.getGiven_coins(), 1);
    }

    @Test
    public void reGainCoinTest(){
        Student pinkStudent = new Student(Colour.PINK);
        DiningRoom room = new DiningRoom(Colour.PINK);
        assertEquals(room.getStudents(),0);
        for(int i=0; i<3; i++){
            try{
                room.putStudent(pinkStudent);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        assertEquals(room.getStudents(),3);
        try{
            room.removeStudent(pinkStudent);
        } catch (Exception e) {
            System.out.println(e);
        }
        assertEquals(room.getGiven_coins(), 1);
        try{
            room.putStudent(pinkStudent);
        } catch (Exception e) {
            System.out.println(e);
        }
        assertEquals(room.getGiven_coins(), 1);
    }

    @Test
    public void removeStudentTest() {
        Student pinkStudent = new Student(Colour.PINK);
        DiningRoom room = new DiningRoom(Colour.PINK);
        try {
            room.putStudent(pinkStudent);
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            room.removeStudent(pinkStudent);
        } catch (Exception e) {
            System.out.println(e);
        }

        assertEquals(room.getStudents(), 0);
    }

    @Test
    public void removeStudentFromEmptyTest() {
        Student pinkStudent = new Student(Colour.PINK);
        DiningRoom room = new DiningRoom(Colour.PINK);
        try {
            room.removeStudent(pinkStudent);
        } catch (Exception e) {
            System.out.println(e);
        }
        assertEquals(room.getStudents(), 0);
    }

    @Test
    public void notEqualsTest(){
        DiningRoom room1 = new DiningRoom(Colour.BLUE);
        DiningRoom room2 = new DiningRoom(Colour.PINK);
        assertFalse(room1.equals(room2));
    }

    @Test
    public void equalsTest(){
        DiningRoom room1 = new DiningRoom(Colour.BLUE);
        DiningRoom room2 = new DiningRoom(Colour.BLUE);
        assertTrue(room1.equals(room2));
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
            System.out.println(e);
        }
        assertTrue(room1.equals(room2));
    }

    @Test
    public void numberNotEqualsTest(){
        DiningRoom room1 = new DiningRoom(Colour.BLUE);
        DiningRoom room2 = new DiningRoom(Colour.BLUE);
        Student s1 = new Student(Colour.BLUE);
        try {
            room1.putStudent(s1);
        } catch (Exception e) {
            System.out.println(e);
        }
        assertNotEquals(room1.equals(room2), true);
    }

    @Test
    public void fullRoomTest(){
        DiningRoom room = new DiningRoom(Colour.BLUE);
        for(int i=0; i<11; i++){
            try {
                room.putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        assertEquals(room.getStudents(), 10);
    }

    @Test
    public void compareTestEquals(){
        DiningRoom room1 = new DiningRoom(Colour.BLUE);
        DiningRoom room2 = new DiningRoom(Colour.BLUE);
        assertEquals(room1.compareTo(room2), 0);
    }

    @Test
    public void compareTestMore(){
        DiningRoom room1 = new DiningRoom(Colour.PINK);
        DiningRoom room2 = new DiningRoom(Colour.BLUE);
        boolean greater = false;
        if(room1.compareTo(room2)>0)
            greater = true;
        assertEquals(greater, true);
    }

    @Test
    public void compareTestLess(){
        DiningRoom room1 = new DiningRoom(Colour.PINK);
        DiningRoom room2 = new DiningRoom(Colour.BLUE);
        boolean lesser = false;
        if(room2.compareTo(room1)<0)
            lesser = true;
        assertEquals(lesser, true);
    }
}
