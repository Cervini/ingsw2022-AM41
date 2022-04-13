package it.polimi.ingsw.am41;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void fillCloud() {
        Game game = new Game(2);
    }

    @Test
    void setTurnOrder() {
    }

    @Test
    void checkTurn() {
    }

    @Test
    void endGame() {
    }

    @Test
    void mergeTest1() throws NullPointerException{
        Island island1 = new Island();
        Island island2 = new Island();
        Student student1 = new Student(Colour.RED);
        Student student2 = new Student(Colour.BLUE);
        Student student3 = new Student(Colour.GREEN);
        Student student4 = new Student(Colour.RED);
        Student student5 = new Student(Colour.RED);
        LinkedList<Student> students = new LinkedList<Student>();

        island1.setIsland_size(1);
        island1.setDeny_card(false);
        island1.setMother_nature(false);
        island1.putStudent(student1);
        island1.putStudent(student2);
        island1.putStudent(student3);
        island2.setIsland_size(1);
        island2.setDeny_card(false);
        island2.setMother_nature(false);
        island2.putStudent(student4);
        island2.putStudent(student5);
        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);

        island1.mergeIslands(island2);
        assertEquals(island1.getIsland_size(), 2);
        assertFalse(island1.getDeny_card());
        assertEquals(island1.getStudents(), students);
        assertFalse(island1.isMother_nature());

        System.out.println("mergeTest1 complete");
    }

    @Test
    void mergeTest2() throws NullPointerException{
        Island island1 = new Island();
        Island island2 = new Island();
        Student student1 = new Student(Colour.RED);
        Student student2 = new Student(Colour.RED);
        Student student3 = new Student(Colour.RED);
        Student student4 = new Student(Colour.RED);
        Student student5 = new Student(Colour.RED);
        LinkedList<Student> students = new LinkedList<Student>();

        island1.setIsland_size(2);
        island1.setDeny_card(true);
        island1.setMother_nature(false);
        island1.putStudent(student1);
        island1.putStudent(student2);
        island1.putStudent(student3);
        island1.putStudent(student4);
        island1.putStudent(student5);
        island2.setIsland_size(4);
        island2.setDeny_card(false);
        island2.setMother_nature(false);

        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);

        island1.mergeIslands(island2);
        assertEquals(island1.getIsland_size(), 6);
        assertTrue(island1.getDeny_card());
        assertEquals(island1.getStudents(), students);
        assertFalse(island1.isMother_nature());

        System.out.println("mergeTest2 complete");
    }

    @Test
    void mergeTest3() throws NullPointerException{
        Island island1 = new Island();
        Island island2 = new Island();
        Student student1 = new Student(Colour.RED);
        Student student2 = new Student(Colour.PINK);
        Student student3 = new Student(Colour.GREEN);
        Student student4 = new Student(Colour.PINK);
        LinkedList<Student> students = new LinkedList<Student>();

        island1.setIsland_size(5);
        island1.setDeny_card(false);
        island1.setMother_nature(true);
        island2.putStudent(student1);
        island2.putStudent(student2);
        island2.putStudent(student3);
        island2.putStudent(student4);
        island2.setIsland_size(4);
        island2.setDeny_card(true);
        island2.setMother_nature(true);

        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);

        island1.mergeIslands(island2);
        assertEquals(island1.getIsland_size(), 9);
        assertTrue(island1.getDeny_card());
        assertEquals(island1.getStudents(), students);
        assertTrue(island1.isMother_nature());

        System.out.println("mergeTest3 complete");
    }

    @Test
    void mergeTest4() throws NullPointerException{
        Island island1 = new Island();
        Island island2 = new Island();
        Student student1 = new Student(Colour.GREEN);
        Student student2 = new Student(Colour.YELLOW);
        Student student3 = new Student(Colour.YELLOW);
        Student student4 = new Student(Colour.PINK);
        Student student5 = new Student(Colour.BLUE);
        Student student6 = new Student(Colour.GREEN);
        Student student7 = new Student(Colour.RED);
        Student student8 = new Student(Colour.YELLOW);
        Student student9 = new Student(Colour.PINK);
        Student student10 = new Student(Colour.BLUE);
        LinkedList<Student> students = new LinkedList<Student>();

        island1.setIsland_size(8);
        island1.setDeny_card(false);
        island1.setMother_nature(false);
        island1.putStudent(student1);
        island1.putStudent(student2);
        island1.putStudent(student3);
        island1.putStudent(student4);
        island1.putStudent(student5);
        island2.setIsland_size(1);
        island2.setDeny_card(false);
        island2.setMother_nature(true);
        island2.putStudent(student6);
        island2.putStudent(student7);
        island2.putStudent(student8);
        island2.putStudent(student9);
        island2.putStudent(student10);

        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);
        students.add(student6);
        students.add(student7);
        students.add(student8);
        students.add(student9);
        students.add(student10);

        island1.mergeIslands(island2);
        assertEquals(island1.getIsland_size(), 9);
        assertFalse(island1.getDeny_card());
        assertEquals(island1.getStudents(), students);
        assertTrue(island1.isMother_nature());

        System.out.println("mergeTest4 complete");
    }

    @Test
    void mergeTest5() throws NullPointerException{
        Island island1 = new Island();
        Island island2 = new Island();
        Student student1 = new Student(Colour.GREEN);
        Student student2 = new Student(Colour.RED);
        Student student3 = new Student(Colour.YELLOW);
        Student student4 = new Student(Colour.PINK);
        Student student5 = new Student(Colour.BLUE);
        Student student6 = new Student(Colour.GREEN);
        Student student7 = new Student(Colour.RED);
        Student student8 = new Student(Colour.YELLOW);
        Student student9 = new Student(Colour.BLUE);
        Student student10 = new Student(Colour.BLUE);
        Student student11 = new Student(Colour.YELLOW);
        Student student12 = new Student(Colour.PINK);
        Student student13 = new Student(Colour.PINK);
        LinkedList<Student> students = new LinkedList<Student>();

        island1.setIsland_size(8);
        island1.setDeny_card(false);
        island1.setMother_nature(false);
        island2.setIsland_size(1);
        island2.setDeny_card(true);
        island2.setMother_nature(true);
        island2.putStudent(student1);
        island2.putStudent(student2);
        island2.putStudent(student3);
        island2.putStudent(student4);
        island2.putStudent(student5);
        island2.putStudent(student6);
        island2.putStudent(student7);
        island2.putStudent(student8);
        island2.putStudent(student9);
        island2.putStudent(student10);
        island2.putStudent(student11);
        island2.putStudent(student12);
        island2.putStudent(student13);

        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);
        students.add(student6);
        students.add(student7);
        students.add(student8);
        students.add(student9);
        students.add(student10);
        students.add(student11);
        students.add(student12);
        students.add(student13);

        island1.mergeIslands(island2);
        assertEquals(island1.getIsland_size(), 9);
        assertTrue(island1.getDeny_card());
        assertEquals(island1.getStudents(), students);
        assertTrue(island1.isMother_nature());

        System.out.println("mergeTest5 complete");
    }

    @Test
    void moveMotherNature() {
    }

    @Test
    void moveProfessor() {
    }

    @Test
    void checkOwnership() {
    }

    @Test
    void chooseCloudTest1() throws Exception{
        Game game = new Game(3);
        Cloud cloud = new Cloud();
        Player player = new Player(TowerColour.WHITE);
        Student student1 = new Student(Colour.RED);
        Student student2 = new Student(Colour.BLUE);
        Student student3 = new Student(Colour.GREEN);
        Student student4 = new Student(Colour.PINK);
        LinkedList<Student> students = new LinkedList<Student>();
        LinkedList<Student> outputStudents = new LinkedList<Student>();

        cloud.putStudent(student1);
        cloud.putStudent(student2);
        cloud.putStudent(student3);
        cloud.putStudent(student4);

        //TODO work in progress

    }

    @Test
    void moveStudent() {
    }

    @Test
    void playCharacter() {
    }

    @Test
    void checkInfluence() {
    }
}