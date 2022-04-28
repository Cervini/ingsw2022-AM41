package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {

    @Test
    void putStudent() {
        Cloud cloud = new Cloud();
        assertEquals(cloud.getStudents().size(),0);
        try {
            cloud.putStudent(new Student(Colour.BLUE));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            cloud.putStudent(new Student(Colour.GREEN));
        } catch (Exception e) {
            System.out.println(e);
        }
        assertEquals(cloud.getStudents().size(),2);
        try {
            cloud.putStudent(new Student(Colour.PINK));
        } catch (Exception e) {
            System.out.println(e);
        }
        assertEquals(cloud.getStudents().size(),3);
    }

    @Test
    void putStudentNull() {
        Cloud cloud = new Cloud();
        assertEquals(cloud.getStudents().size(),0);
        try {
            cloud.putStudent(null);
        } catch (Exception e) {
            System.out.println(e);
        }
        assertEquals(cloud.getStudents().size(),0);
    }

    @Test
    void putStudentFull() {
        Cloud cloud = new Cloud();
        try {
            cloud.putStudent(new Student(Colour.BLUE));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            cloud.putStudent(new Student(Colour.GREEN));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            cloud.putStudent(new Student(Colour.PINK));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            cloud.putStudent(new Student(Colour.RED));
        } catch (Exception e) {
            System.out.println(e);
        }
        assertEquals(cloud.getStudents().size(),3);
    }


    @Test
    void removeStudent() {
        Cloud cloud = new Cloud();
        try {
            cloud.putStudent(new Student(Colour.BLUE));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            cloud.putStudent(new Student(Colour.GREEN));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            cloud.putStudent(new Student(Colour.PINK));
        } catch (Exception e) {
            System.out.println(e);
        }
        cloud.removeStudent(new Student(Colour.GREEN));
        assertEquals(cloud.getStudents().size(),2);
    }

    @Test
    void removeStudentWrong() {
        Cloud cloud = new Cloud();
        try {
            cloud.putStudent(new Student(Colour.BLUE));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            cloud.putStudent(new Student(Colour.GREEN));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            cloud.putStudent(new Student(Colour.PINK));
        } catch (Exception e) {
            System.out.println(e);
        }
        cloud.removeStudent(new Student(Colour.RED));
        assertEquals(cloud.getStudents().size(),3);
    }

    @Test
    void removeStudentNull() {
        Cloud cloud = new Cloud();
        try {
            cloud.putStudent(new Student(Colour.BLUE));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            cloud.putStudent(new Student(Colour.GREEN));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            cloud.putStudent(new Student(Colour.PINK));
        } catch (Exception e) {
            System.out.println(e);
        }
        cloud.removeStudent(null);
        assertEquals(cloud.getStudents().size(),3);
    }
}