package it.polimi.ingsw;

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
    void checkTurnTest1() {
        /*Game game = new Game(2);
        Player player1;
        Player player2;

        player1 = game.getPlayers().getFirst();
        player2 = game.getPlayers().getLast();

        //TODO check
        player1.setFace_up_assistant();
        player2.setFace_up_assistant();

        game.setTurnOrder();
        game.checkTurn();

        assertTrue(game.getTurnOrder().getFirst().isTurn());

        System.out.println("checkTurnTest1 complete");*/
    }

    @Test
    void endGameTest1() throws Exception {
        Game game = new Game(2);

        game.getPlayers().getFirst().getSchool().takeTowers(8);

        assertEquals(game.endGame(), game.getPlayers().getFirst().getTeam());

        System.out.println("endGameTest1 complete");
    }

    @Test
    void endGameTest2() throws Exception {
        Game game = new Game(3);

        game.getPlayers().getFirst().getSchool().takeTowers(5);
        game.getPlayers().getLast().getSchool().takeTowers(6);

        assertEquals(game.endGame(), game.getPlayers().getLast().getTeam());

        System.out.println("endGameTest2 complete");
    }

    @Test
    void endGameTest3() throws Exception {
        Game game = new Game(4);

        game.getPlayers().getFirst().getSchool().takeTowers(3);
        game.getPlayers().get(1).getSchool().takeTowers(5);
        game.getPlayers().get(0).getSchool().giveTowers(2);
        game.getPlayers().get(0).getSchool().takeTowers(7);

        assertEquals(game.endGame(), game.getPlayers().getFirst().getTeam());

        System.out.println("endGameTest3 complete");
    }


    //TODO fix checkWinner
    /*
    @Test
    void endGameTest4() throws Exception {
        Game game = new Game(2);

        game.getArchipelago().get(0).setTower(TowerColour.BLACK);
        while(game.endGame() == null){
            game.getArchipelago().get(1).setTower(TowerColour.BLACK);
            game.merge(game.getArchipelago().get(0), game.getArchipelago().get(1));
        }

         assertTrue(game.endGame().equals(TowerColour.BLACK) && game.getArchipelago().size() == 3);

        System.out.println("endGameTest4 complete");
    }

     */

    @Test
    void endGameTest7() throws Exception {
        Game game = new Game(4);

        game.getBag().clear();

        assertTrue(game.endGame() != null);

        System.out.println("endGameTest7 complete");
    }

    @Test
    void endGameTest8() throws Exception {
        Game game = new Game(4);

        game.getPlayers().getLast().getAssistants().clear();

        assertTrue(game.endGame() != null);

        System.out.println("endGameTest8 complete");
    }



    //TODO fix checkOwnership
    /*
    @Test
    void endGameTest9() throws Exception {
        Game game = new Game(2);

        game.getPlayers().getFirst().getSchool().takeTowers(2);
        game.getPlayers().getLast().getSchool().takeTowers(2);

        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.YELLOW));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.YELLOW));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.RED));
        game.getPlayers().getFirst().getSchool().putStudent(new Student(Colour.RED));
        game.getPlayers().getLast().getSchool().putStudent(new Student(Colour.YELLOW));
        game.getPlayers().getLast().getSchool().putStudent(new Student(Colour.YELLOW));
        game.getPlayers().getLast().getSchool().putStudent(new Student(Colour.YELLOW));
        game.getPlayers().getLast().getSchool().putStudent(new Student(Colour.RED));
        game.getPlayers().getLast().getSchool().putStudent(new Student(Colour.BLUE));
        game.getPlayers().getLast().getSchool().putStudent(new Student(Colour.GREEN));

        game.checkOwnership();
        game.getBag().clear();


        assertEquals(game.endGame(), game.getPlayers().getLast().getTeam());

        System.out.println("endGameTest9 complete");
    }

     */

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
        island1.setNo_entry(false);
        island1.setMother_nature(false);
        island1.putStudent(student1);
        island1.putStudent(student2);
        island1.putStudent(student3);
        island2.setIsland_size(1);
        island2.setNo_entry(false);
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
        assertFalse(island1.getNo_entry());
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
        island1.setNo_entry(true);
        island1.setMother_nature(false);
        island1.putStudent(student1);
        island1.putStudent(student2);
        island1.putStudent(student3);
        island1.putStudent(student4);
        island1.putStudent(student5);
        island2.setIsland_size(4);
        island2.setNo_entry(false);
        island2.setMother_nature(false);

        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);

        island1.mergeIslands(island2);
        assertEquals(island1.getIsland_size(), 6);
        assertTrue(island1.getNo_entry());
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
        island1.setNo_entry(false);
        island1.setMother_nature(true);
        island2.putStudent(student1);
        island2.putStudent(student2);
        island2.putStudent(student3);
        island2.putStudent(student4);
        island2.setIsland_size(4);
        island2.setNo_entry(true);
        island2.setMother_nature(true);

        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);

        island1.mergeIslands(island2);
        assertEquals(island1.getIsland_size(), 9);
        assertTrue(island1.getNo_entry());
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
        island1.setNo_entry(false);
        island1.setMother_nature(false);
        island1.putStudent(student1);
        island1.putStudent(student2);
        island1.putStudent(student3);
        island1.putStudent(student4);
        island1.putStudent(student5);
        island2.setIsland_size(1);
        island2.setNo_entry(false);
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
        assertFalse(island1.getNo_entry());
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
        island1.setNo_entry(false);
        island1.setMother_nature(false);
        island2.setIsland_size(1);
        island2.setNo_entry(true);
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
        assertTrue(island1.getNo_entry());
        assertEquals(island1.getStudents(), students);
        assertTrue(island1.isMother_nature());

        System.out.println("mergeTest5 complete");
    }

    @Test
    void moveMotherNatureTest1() {
        Game game = new Game(2);
        List<Island> archipelagoTest;
        archipelagoTest = game.getArchipelago();

        game.getPlayers().getFirst().setFace_up_assistant(new Assistant(1,10,game.getPlayers().getFirst()));

        try {
            game.moveMotherNature(3, game.getPlayers().getFirst());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(archipelagoTest.get(3).isMother_nature());

        System.out.println("moveMotherNatureTest1 complete");
    }

    @Test
    void moveMotherNatureTest2() {
        Game game = new Game(3);
        List<Island> archipelagoTest;
        archipelagoTest = game.getArchipelago();

        game.getPlayers().getFirst().setFace_up_assistant(new Assistant(1,10,game.getPlayers().getFirst()));

        try {
            game.moveMotherNature(1, game.getPlayers().getFirst());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(archipelagoTest.get(1).isMother_nature());

        System.out.println("moveMotherNatureTest2 complete");
    }

    @Test
    void moveMotherNatureTest3() {
        Game game = new Game(4);
        List<Island> archipelagoTest;
        archipelagoTest = game.getArchipelago();

        game.getPlayers().getFirst().setFace_up_assistant(new Assistant(1,10,game.getPlayers().getFirst()));

        try {
            game.moveMotherNature(3, game.getPlayers().getFirst());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            game.moveMotherNature(3, game.getPlayers().getFirst());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(archipelagoTest.get(6).isMother_nature());

        System.out.println("moveMotherNatureTest3 complete");
    }

    @Test
    void moveMotherNatureTest4() {
        Game game = new Game(4);
        List<Island> archipelagoTest;
        archipelagoTest = game.getArchipelago();

        game.getPlayers().getFirst().setFace_up_assistant(new Assistant(1,20,game.getPlayers().getFirst()));

        try {
            game.moveMotherNature(11, game.getPlayers().getFirst());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            game.moveMotherNature(3, game.getPlayers().getFirst());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(archipelagoTest.get(2).isMother_nature());

        System.out.println("moveMotherNatureTest4 complete");
    }

    @Test
    void moveMotherNatureTest5() {
        Game game = new Game(4);
        List<Island> archipelagoTest;
        archipelagoTest = game.getArchipelago();

        game.getPlayers().getFirst().setFace_up_assistant(new Assistant(1,20,game.getPlayers().getFirst()));

        try {
            game.moveMotherNature(11, game.getPlayers().getFirst());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            game.moveMotherNature(3, game.getPlayers().getFirst());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(archipelagoTest.get(2).isMother_nature());

        System.out.println("moveMotherNatureTest5 complete");
    }

    @Test
    void moveMotherNatureTest6() {
        Game game = new Game(2);
        List<Island> archipelagoTest;
        archipelagoTest = game.getArchipelago();

        game.getPlayers().getFirst().setFace_up_assistant(new Assistant(1,10,game.getPlayers().getFirst()));

        game.merge(archipelagoTest.get(1), archipelagoTest.get(2));
        try {
            game.moveMotherNature(3, game.getPlayers().getFirst());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(archipelagoTest.get(3).isMother_nature());

        System.out.println("moveMotherNatureTest6 complete");
    }

    @Test
    void moveProfessor() {
    }

    //TODO fix checkOwnership
    /*
    @Test
    void checkOwnership() throws Exception {
        Game game = new Game(2);
        LinkedList<Player> players;
        Player player1;
        Player player2;
        Student student1 = new Student(Colour.RED);
        Student student2 = new Student(Colour.RED);
        Student student3 = new Student(Colour.RED);
        Student student4 = new Student(Colour.RED);
        Student student5 = new Student(Colour.RED);
        ArrayList<Professor> professors = new ArrayList<>();

        players = game.getPlayers();
        player1 = players.getFirst();
        player2 = players.getLast();
        player1.getSchool().putStudent(student1);
        player1.getSchool().putStudent(student2);
        player1.getSchool().putStudent(student3);
        player2.getSchool().putStudent(student4);
        player2.getSchool().putStudent(student5);

        professors.add(new Professor(Colour.RED));
        checkOwnership();

        assertEquals(player1.getProfessor(), professors);
    }

     */

    @Test
    void chooseCloudTest1(){
        Game game = new Game(3);
        Cloud cloud = new Cloud();
        Player player = new Player(TowerColour.WHITE);
        Student student1 = new Student(Colour.RED);
        Student student2 = new Student(Colour.BLUE);
        Student student3 = new Student(Colour.GREEN);
        Student student4 = new Student(Colour.PINK);
        LinkedList<Student> students = new LinkedList<Student>();
        LinkedList<Student> outputStudents = new LinkedList<Student>();

        try {
            cloud.putStudent(student1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            cloud.putStudent(student2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            cloud.putStudent(student3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // putting a 4th student on the cloud should throw an exception
        try {
            cloud.putStudent(student4);
        } catch (Exception e){
            e.printStackTrace();
        }

        outputStudents.add(student1);
        outputStudents.add(student2);
        outputStudents.add(student3);
        outputStudents.add(student4);



        //TODO work in progress

    }

    @Test
    void moveStudentTest1() throws Exception {
        Game game = new Game(2);
        ArrayList<Student> entrance = game.getPlayers().getFirst().getSchool().getEntrance();
        List<Student> islandStudents = game.getArchipelago().get(0).getStudents();
        Student student = new Student(Colour.RED);
        game.getPlayers().getFirst().getSchool().putStudent(student);
        game.moveStudent(game.getPlayers().getFirst().getSchool(), game.getArchipelago().get(0), student);
        islandStudents.add(student);

        assertTrue(game.getPlayers().getFirst().getSchool().getEntrance().equals(entrance) && game.getArchipelago().get(0).getStudents().equals(islandStudents));

        System.out.println("moveStudentTest1 complete");
    }

    @Test
    void moveStudentTest2() throws Exception {
        Game game = new Game(2);
        ArrayList<Student> entrance = game.getPlayers().getFirst().getSchool().getEntrance();
        Student student = new Student(Colour.RED);
        game.getPlayers().getFirst().getSchool().putStudent(student);
        game.moveStudent(game.getPlayers().getFirst().getSchool(), game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED), student);

        assertTrue(game.getPlayers().getFirst().getSchool().getEntrance().equals(entrance) && game.getPlayers().getFirst().getSchool().getDining_room(Colour.RED).getStudents() == 1);

        System.out.println("moveStudentTest2 complete");
    }

    @Test
    void moveStudentTest3() throws Exception {
        Game game = new Game(2);
        ArrayList<Student> entrance = game.getPlayers().getFirst().getSchool().getEntrance();
        List<Student> islandStudents = game.getArchipelago().get(0).getStudents();
        Student student = new Student(Colour.BLUE);
        game.getPlayers().getFirst().getSchool().putStudent(student);
        game.moveStudent(game.getPlayers().getFirst().getSchool(), game.getArchipelago().get(0), student);
        islandStudents.add(student);

        assertTrue(game.getPlayers().getFirst().getSchool().getEntrance().equals(entrance) && game.getArchipelago().get(0).getStudents().equals(islandStudents));

        System.out.println("moveStudentTest3 complete");
    }

    @Test
    void moveStudentTest4() throws Exception {
        Game game = new Game(2);
        ArrayList<Student> entrance = game.getPlayers().getFirst().getSchool().getEntrance();
        Student student = new Student(Colour.BLUE);
        game.getPlayers().getFirst().getSchool().putStudent(student);
        game.moveStudent(game.getPlayers().getFirst().getSchool(), game.getPlayers().getFirst().getSchool().getDining_room(Colour.BLUE), student);

        assertTrue(game.getPlayers().getFirst().getSchool().getEntrance().equals(entrance) && game.getPlayers().getFirst().getSchool().getDining_room(Colour.BLUE).getStudents() == 1);

        System.out.println("moveStudentTest4 complete");
    }


    @Test
    void playCharacter() {
    }
}