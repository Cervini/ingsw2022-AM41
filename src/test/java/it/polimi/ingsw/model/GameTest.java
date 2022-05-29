package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void fillCloudTest1() {
        Game game = new Game(2);
        game.fillCloud(game.getClouds().get(0));

        assertEquals(3, game.getClouds().get(0).getStudents().size());
    }

    @Test
    void fillCloudTest2() {
        Game game = new Game(3);
        game.fillCloud(game.getClouds().get(0));

        assertEquals(4, game.getClouds().get(0).getStudents().size());
    }

    @Test
    void fillCloudTest3() {
        Game game = new Game(4);
        game.fillCloud(game.getClouds().get(2));

        assertEquals(3, game.getClouds().get(2).getStudents().size());
    }

    @Test
    void fillCloudTest4() {
        Game game = new Game(4);

        game.getClouds().get(2).emptyIsland();
        game.fillCloud(game.getClouds().get(2));

        assertEquals(3, game.getClouds().get(2).getStudents().size());
    }

    @Test
    void fillCloudTest5() {
        Game game = new Game(4);

        int bagSize = game.getBag().size();

        game.getClouds().get(2).emptyIsland();
        game.fillCloud(game.getClouds().get(2));

        assertTrue(game.getBag().size() == bagSize - 3);
    }

    @Test
    void setTurnOrderTest1() {
        Game game = new Game(3);

        Player p1 = game.getPlayers().get(0);
        Player p2 = game.getPlayers().get(1);
        Player p3 = game.getPlayers().get(2);

        try {
            p1.playAssistant(new Assistant(7,4));
            p2.playAssistant(new Assistant(8,4));
            p3.playAssistant(new Assistant(2,1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        game.setTurnOrder();

        assertEquals(game.getTurnOrder().getFirst(), p3);
    }

    @Test
    void setTurnOrderTest2() {
        Game game = new Game(3);

        Player p1 = game.getPlayers().get(0);
        Player p2 = game.getPlayers().get(1);
        Player p3 = game.getPlayers().get(2);

        try {
            p1.playAssistant(new Assistant(1,1));
            p2.playAssistant(new Assistant(8,4));
            p3.playAssistant(new Assistant(2,1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        game.setTurnOrder();

        assertEquals(game.getTurnOrder().getFirst(), p1);
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
    }

    @Test
    void endGameTest2() throws Exception {
        Game game = new Game(3);

        game.getPlayers().getFirst().getSchool().takeTowers(5);
        game.getPlayers().getLast().getSchool().takeTowers(6);

        assertEquals(game.endGame(), game.getPlayers().getLast().getTeam());
    }

    @Test
    void endGameTest3() throws Exception {
        Game game = new Game(4);

        game.getPlayers().getFirst().getSchool().takeTowers(3);
        game.getPlayers().get(1).getSchool().takeTowers(5);
        game.getPlayers().get(0).getSchool().giveTowers(2);
        game.getPlayers().get(0).getSchool().takeTowers(7);

        assertEquals(game.endGame(), game.getPlayers().getFirst().getTeam());
    }

    @Test
    void endGameTest4() throws Exception {
        /*Game game = new Game(2);

        game.getArchipelago().get(0).setTower(TowerColour.BLACK);
        while(game.endGame() == null){
            game.getArchipelago().get(1).setTower(TowerColour.BLACK);
            game.merge(game.getArchipelago().get(0), game.getArchipelago().get(1));
        }

        //TODO fix checkWinner

        assertTrue(game.endGame().equals(TowerColour.BLACK) && game.getArchipelago().size() == 3);

        System.out.println("endGameTest4 complete");*/
    }

    //TODO add endGameTest5 and endGameTest6

    @Test
    void endGameTest7() throws Exception {
        Game game = new Game(4);

        game.getBag().clear();

        assertTrue(game.endGame() != null);
    }

    @Test
    void mergeTest1() throws NullPointerException {
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
    }

    @Test
    void mergeTest2() throws NullPointerException {
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
    }

    @Test
    void mergeTest3() throws NullPointerException {
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
    }

    @Test
    void mergeTest4() throws NullPointerException {
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
    }

    @Test
    void mergeTest5() throws NullPointerException {
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
    }

    @Test
    void moveMotherNatureTest1() {
        Game game = new Game(2);
        List<Island> archipelagoTest;
        archipelagoTest = game.getArchipelago();

        game.getPlayers().getFirst().setFace_up_assistant(new Assistant(1, 10));

        try {
            game.moveMotherNature(3, game.getPlayers().getFirst());
        } catch (Exception e) {
            System.out.println(e);
        }

        assertTrue(archipelagoTest.get(3).isMother_nature());
    }

    @Test
    void moveMotherNatureTest2() {
        Game game = new Game(3);
        List<Island> archipelagoTest;
        archipelagoTest = game.getArchipelago();

        game.getPlayers().getFirst().setFace_up_assistant(new Assistant(1, 10));

        try {
            game.moveMotherNature(1, game.getPlayers().getFirst());
        } catch (Exception e) {
            System.out.println(e);
        }

        assertTrue(archipelagoTest.get(1).isMother_nature());
    }

    @Test
    void moveMotherNatureTest3() {
        Game game = new Game(4);
        List<Island> archipelagoTest;
        archipelagoTest = game.getArchipelago();

        game.getPlayers().getFirst().setFace_up_assistant(new Assistant(1, 10));

        try {
            game.moveMotherNature(3, game.getPlayers().getFirst());
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            game.moveMotherNature(3, game.getPlayers().getFirst());
        } catch (Exception e) {
            System.out.println(e);
        }

        assertTrue(archipelagoTest.get(6).isMother_nature());
    }

    @Test
    void moveMotherNatureTest4() {
        Game game = new Game(4);
        List<Island> archipelagoTest;
        archipelagoTest = game.getArchipelago();
        game.getPlayers().getFirst().setFace_up_assistant(new Assistant(1, 20));

        try {
            game.moveMotherNature(11, game.getPlayers().getFirst());
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            game.moveMotherNature(3, game.getPlayers().getFirst());
        } catch (Exception e) {
            System.out.println(e);
        }

        assertTrue(archipelagoTest.get(2).isMother_nature());
    }

    @Test
    void moveMotherNatureTest5() {
        Game game = new Game(4);
        List<Island> archipelagoTest;
        archipelagoTest = game.getArchipelago();

        game.getPlayers().getFirst().setFace_up_assistant(new Assistant(1, 20));

        try {
            game.moveMotherNature(11, game.getPlayers().getFirst());
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            game.moveMotherNature(3, game.getPlayers().getFirst());
        } catch (Exception e) {
            System.out.println(e);
        }

        assertTrue(archipelagoTest.get(2).isMother_nature());
    }

    @Test
    void moveMotherNatureTest6() {
        Game game = new Game(2);
        List<Island> archipelagoTest;
        archipelagoTest = game.getArchipelago();

        game.getPlayers().getFirst().setFace_up_assistant(new Assistant(1, 10));

        game.merge(archipelagoTest.get(1), archipelagoTest.get(2));
        try {
            game.moveMotherNature(3, game.getPlayers().getFirst());
        } catch (Exception e) {
            System.out.println(e);
        }

        assertTrue(archipelagoTest.get(3).isMother_nature());
    }

    @Test
    @DisplayName("2 Players Game")
    void constructorTest01() {
        Game game = new Game(2);
        assertEquals(game.getPlayers().size(), 2);
        assertNotEquals(game.getPlayers().getFirst(), game.getPlayers().getLast());
        assertEquals(game.getAvailable_coins(),18);
    }

    @Test
    @DisplayName("3 Players Game")
    void constructorTest02() {
        Game game = new Game(3);
        assertEquals(game.getPlayers().size(), 3);
        assertNotEquals(game.getPlayers().get(0), game.getPlayers().get(1));
        assertNotEquals(game.getPlayers().get(1), game.getPlayers().get(2));
        assertNotEquals(game.getPlayers().get(0), game.getPlayers().get(2));
        assertEquals(game.getAvailable_coins(),17);
    }

    @Test
    @DisplayName("4 Players Game")
    void constructorTest03() {
        Game game = new Game(4);
        assertEquals(game.getPlayers().size(), 4);
        assertNotEquals(game.getPlayers().get(0), game.getPlayers().get(1));
        assertNotEquals(game.getPlayers().get(1), game.getPlayers().get(2));
        assertNotEquals(game.getPlayers().get(0), game.getPlayers().get(2));
        assertNotEquals(game.getPlayers().get(0), game.getPlayers().get(3));
        assertNotEquals(game.getPlayers().get(1), game.getPlayers().get(3));
        assertNotEquals(game.getPlayers().get(3), game.getPlayers().get(2));
        assertEquals(game.getAvailable_coins(),16);
    }

    @Test
    void countDiningTest01(){
        Game game = new Game(4);
        for(int i=0; i<3; i++){
            try {
                game.getPlayers().get(0).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<4; i++){
            try {
                game.getPlayers().get(1).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<1; i++){
            try {
                game.getPlayers().get(2).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            try {
                game.getPlayers().get(3).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LinkedList<OwnerInfo> list = new LinkedList<>();
        game.countDiningStudents(Colour.BLUE, list);
        assertEquals(list.get(0).value(), 2);
        assertEquals(list.get(1).value(), 1);
        assertEquals(list.get(2).value(), 4);
        assertEquals(list.get(3).value(), 3);
    }

    @Test
    void countDiningTest02(){
        Game game = new Game(4);

        LinkedList<OwnerInfo> list = new LinkedList<>();
        game.countDiningStudents(Colour.BLUE, list);
        assertEquals(list.get(0).value(), 0);
        assertEquals(list.get(1).value(), 0);
        assertEquals(list.get(2).value(), 0);
        assertEquals(list.get(3).value(), 0);
        assertEquals(list.size(),4);
    }

    @Test
    void findMaxValueTest01(){
        Game game = new Game(4);
        for(int i=0; i<3; i++){
            try {
                game.getPlayers().get(0).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<4; i++){
            try {
                game.getPlayers().get(1).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<1; i++){
            try {
                game.getPlayers().get(2).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            try {
                game.getPlayers().get(3).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LinkedList<OwnerInfo> list = new LinkedList<>();
        game.countDiningStudents(Colour.BLUE, list);

        OwnerInfo expected = new OwnerInfo(4, game.getPlayers().get(1));
        assertEquals(game.findMaxValue(list), expected);
    }

    @Test
    void findMaxValueTest02(){
        Game game = new Game(4);

        LinkedList<OwnerInfo> list = new LinkedList<>();
        game.countDiningStudents(Colour.BLUE, list);

        OwnerInfo expected = new OwnerInfo(0, game.getPlayers().get(3));
        assertEquals(game.findMaxValue(list), expected);
    }

    @Test
    void goodValueTest01(){
        Game game = new Game(4);

        LinkedList<OwnerInfo> list = new LinkedList<>();
        game.countDiningStudents(Colour.BLUE, list);

        OwnerInfo owner = game.findMaxValue(list);

        assertFalse(game.goodValue(owner,list));
    }

    @Test
    void goodValueTest02(){
        Game game = new Game(4);

        for(int i=0; i<3; i++){
            try {
                game.getPlayers().get(0).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<4; i++){
            try {
                game.getPlayers().get(1).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<1; i++){
            try {
                game.getPlayers().get(2).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            try {
                game.getPlayers().get(3).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LinkedList<OwnerInfo> list = new LinkedList<>();
        game.countDiningStudents(Colour.BLUE, list);

        OwnerInfo owner = game.findMaxValue(list);

        assertTrue(game.goodValue(owner,list));
    }

    @Test
    void moveProfessorTest01(){
        Game game = new Game(2);

        game.moveProfessor(Colour.BLUE, game.getPlayers().getFirst());

        assertEquals(game.getProfessors().size(), 4);
        assertEquals(game.getPlayers().getFirst().getOwned_professor().size(), 1);
    }

    @Test
    void moveProfessorTest02(){
        Game game = new Game(2);

        game.moveProfessor(Colour.BLUE, game.getPlayers().getFirst());

        game.moveProfessor(Colour.BLUE, game.getPlayers().get(1));

        assertEquals(game.getProfessors().size(), 4);
        assertEquals(game.getPlayers().get(1).getOwned_professor().size(), 1);
        assertEquals(game.getPlayers().getFirst().getOwned_professor().size(), 0);
    }

    @Test
    void checkOwnership01(){
        Game game = new Game(4);

        for(int i=0; i<3; i++){
            try {
                game.getPlayers().get(0).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<4; i++){
            try {
                game.getPlayers().get(1).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<1; i++){
            try {
                game.getPlayers().get(2).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            try {
                game.getPlayers().get(3).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(int i=0; i<5; i++){
            try {
                game.getPlayers().get(0).getSchool().getDining_room(Colour.GREEN).putStudent(new Student(Colour.GREEN));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            try {
                game.getPlayers().get(1).getSchool().getDining_room(Colour.GREEN).putStudent(new Student(Colour.GREEN));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            try {
                game.getPlayers().get(2).getSchool().getDining_room(Colour.GREEN).putStudent(new Student(Colour.GREEN));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            try {
                game.getPlayers().get(3).getSchool().getDining_room(Colour.GREEN).putStudent(new Student(Colour.GREEN));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(int i=0; i<5; i++){
            try {
                game.getPlayers().get(0).getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            try {
                game.getPlayers().get(1).getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            try {
                game.getPlayers().get(2).getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            try {
                game.getPlayers().get(3).getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(int i=0; i<1; i++){
            try {
                game.getPlayers().get(0).getSchool().getDining_room(Colour.YELLOW).putStudent(new Student(Colour.YELLOW));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<3; i++){
            try {
                game.getPlayers().get(1).getSchool().getDining_room(Colour.YELLOW).putStudent(new Student(Colour.YELLOW));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<6; i++){
            try {
                game.getPlayers().get(2).getSchool().getDining_room(Colour.YELLOW).putStudent(new Student(Colour.YELLOW));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<9; i++){
            try {
                game.getPlayers().get(3).getSchool().getDining_room(Colour.YELLOW).putStudent(new Student(Colour.YELLOW));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(int i=0; i<7; i++){
            try {
                game.getPlayers().get(0).getSchool().getDining_room(Colour.PINK).putStudent(new Student(Colour.PINK));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<3; i++){
            try {
                game.getPlayers().get(1).getSchool().getDining_room(Colour.PINK).putStudent(new Student(Colour.PINK));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            try {
                game.getPlayers().get(2).getSchool().getDining_room(Colour.PINK).putStudent(new Student(Colour.PINK));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<6; i++){
            try {
                game.getPlayers().get(3).getSchool().getDining_room(Colour.PINK).putStudent(new Student(Colour.PINK));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        game.checkOwnership();

        assertEquals(game.getProfessors().size(), 0);
        assertEquals(game.getPlayers().get(0).getOwned_professor().size(), 3);
        assertEquals(game.getPlayers().get(1).getOwned_professor().size(), 1);
        assertEquals(game.getPlayers().get(2).getOwned_professor().size(), 0);
        assertEquals(game.getPlayers().get(3).getOwned_professor().size(), 1);
    }


    @Test
    void checkOwnership02(){
        Game game = new Game(4);

        for(int i=0; i<3; i++){
            try {
                game.getPlayers().get(0).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<4; i++){
            try {
                game.getPlayers().get(1).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<1; i++){
            try {
                game.getPlayers().get(2).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            try {
                game.getPlayers().get(3).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(int i=0; i<5; i++){
            try {
                game.getPlayers().get(0).getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            try {
                game.getPlayers().get(1).getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            try {
                game.getPlayers().get(2).getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            try {
                game.getPlayers().get(3).getSchool().getDining_room(Colour.RED).putStudent(new Student(Colour.RED));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(int i=0; i<1; i++){
            try {
                game.getPlayers().get(0).getSchool().getDining_room(Colour.YELLOW).putStudent(new Student(Colour.YELLOW));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<3; i++){
            try {
                game.getPlayers().get(1).getSchool().getDining_room(Colour.YELLOW).putStudent(new Student(Colour.YELLOW));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<6; i++){
            try {
                game.getPlayers().get(2).getSchool().getDining_room(Colour.YELLOW).putStudent(new Student(Colour.YELLOW));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<9; i++){
            try {
                game.getPlayers().get(3).getSchool().getDining_room(Colour.YELLOW).putStudent(new Student(Colour.YELLOW));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(int i=0; i<7; i++){
            try {
                game.getPlayers().get(0).getSchool().getDining_room(Colour.PINK).putStudent(new Student(Colour.PINK));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<3; i++){
            try {
                game.getPlayers().get(1).getSchool().getDining_room(Colour.PINK).putStudent(new Student(Colour.PINK));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            try {
                game.getPlayers().get(2).getSchool().getDining_room(Colour.PINK).putStudent(new Student(Colour.PINK));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<6; i++){
            try {
                game.getPlayers().get(3).getSchool().getDining_room(Colour.PINK).putStudent(new Student(Colour.PINK));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        game.checkOwnership();

        assertEquals(game.getProfessors().size(), 1);
        assertEquals(game.getPlayers().get(0).getOwned_professor().size(), 2);
        assertEquals(game.getPlayers().get(1).getOwned_professor().size(), 1);
        assertEquals(game.getPlayers().get(2).getOwned_professor().size(), 0);
        assertEquals(game.getPlayers().get(3).getOwned_professor().size(), 1);
    }

    @Test
    void divideAndConquerOwnerShipTest(){
        Game game = new Game(4);

        for(int i=0; i<3; i++){
            try {
                game.getPlayers().get(0).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<4; i++){
            try {
                game.getPlayers().get(1).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<1; i++){
            try {
                game.getPlayers().get(2).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            try {
                game.getPlayers().get(3).getSchool().getDining_room(Colour.BLUE).putStudent(new Student(Colour.BLUE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        game.checkOwnership();

        assertEquals(game.getProfessors().size(), 4);
    }

    @Test
    void checkOwnership03(){
        Game game = new Game(4);

        game.checkOwnership();

        assertEquals(game.getProfessors().size(), 5);
    }

    @Test
    void moveStudent1(){
        Game game = new Game(2);
        Player player = game.getPlayers().getFirst();
        Student student = player.getSchool().getEntrance().getFirst();
        player.getSchool().removeStudent(student);

        student = new Student(Colour.RED);

        player.getSchool().putStudent(student);

        game.moveStudent(player.getSchool(), player.getSchool().getDining_room(Colour.RED),student);
    }

    @Test
    void moveStudent2(){
        Game game = new Game(2);
        Player player = game.getPlayers().getFirst();
        Student student = player.getSchool().getEntrance().getFirst();
        player.getSchool().removeStudent(student);

        student = new Student(Colour.RED);

        player.getSchool().putStudent(student);

        game.moveStudent(player.getSchool(), player.getSchool().getDining_room(Colour.BLUE),student);
    }
}