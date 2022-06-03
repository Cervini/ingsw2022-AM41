package it.polimi.ingsw.model;

import java.util.Collections;
import java.util.LinkedList;

public class Character{

    private final int characterNumber;
    private int cost;
    private int noEntry;
    private boolean increasedCost;

    private final CharacterFunctions characterFunctions = new CharacterFunctions();
    private final LinkedList<Student> students = new LinkedList<>();

    private static final int numberOfStudentsToRemove = 3;
    private static final int maxNoEntry = 4;

    public Character(int characterNumber){
        this.characterNumber = characterNumber;

        increasedCost = false;
        switch (characterNumber){
            case 0 -> {
                cost = 1;
            }
            case 1 -> {
                cost = 2;
            }
            case 2 -> {
                cost = 3;
            }
            case 3 -> {
                cost = 1;
            }
            case 4 -> {
                cost = 2;
                noEntry = maxNoEntry;
            }
            case 5 -> {
                cost = 3;
            }
            case 6 -> {
                cost = 1;
            }
            case 7 -> {
                cost = 2;
            }
            case 8 -> {
                cost = 3;
            }
            case 9 -> {
                cost = 1;
            }
            case 10 -> {
                cost = 2;
            }
            case 11 -> {
                cost = 3;
            }
        }
    }

    public int getCharacterNumber(){
        return characterNumber;
    }

    public void returnNoEntry(){

        if(noEntry < maxNoEntry) {
            noEntry++;
        }
    }

    public int getCost() {return cost; }

    public LinkedList<Student> getStudents() {return students;}

    public void addStudent(Student student) {students.add(student);}

    private void getNoEntry() throws Exception {
        if(noEntry > 0){
            noEntry--;
        }else{
            throw new Exception("All the No Entry cards are already returned");
        }
    }

    public Game effect(Game game, Player player, LinkedList<Student> studentList1, LinkedList<Student> studentList2, Island island, Assistant assistant, Colour colour) throws Exception{
        Game modifiedGame = null;

        if(increasedCost == false){
            cost = cost + 1;
            increasedCost = true;
        }
        switch (characterNumber){
            case 0 -> {
                modifiedGame = effect1(game, studentList1.getFirst(), island);
            }
            case 1 -> {
                modifiedGame = effect2(game, player);
            }
            case 2 -> {
                modifiedGame = effect3(game, island);
            }
            case 3 -> {
                modifiedGame = effect4(game, assistant);
            }
            case 4 -> {
                modifiedGame = effect5(game, island);
            }
            case 5 -> {
                modifiedGame = effect6(game);
            }
            case 6 -> {
                modifiedGame = effect7(game, player, studentList1, studentList2);
            }
            case 7 -> {
                modifiedGame = effect8(game, player);
            }
            case 8 -> {
                modifiedGame = effect9(game, colour);
            }
            case 9 -> {
                modifiedGame = effect10(game, player, studentList1.get(0), studentList1.get(1), studentList2.get(0), studentList2.get(1));
            }
            case 10 -> {
                modifiedGame = effect11(game, player, studentList1.get(0));
            }
            case 11 -> {
                modifiedGame = effect12(game, colour);
            }
        }
        return modifiedGame;
    }

    //Character 1 (case 0)
    private Game effect1(Game game, Student selectedStudent, Island island){
        int indexOfIslandToEdit;
        students.remove(selectedStudent);
        indexOfIslandToEdit = game.getArchipelago().indexOf(island);
        game.getArchipelago().get(indexOfIslandToEdit).putStudent(selectedStudent);
        students.add(game.drawStudent());
        return game;
    }

    //Character 2 (case 1)
    private Game effect2(Game game, Player player) throws Exception{
        game = characterFunctions.checkInfluenceWithModifiedBoard(game, player);
        return game;
    }

    //Character 3 (case 2)
    private Game effect3(Game game, Island island) throws Exception{
        game = characterFunctions.checkInfluenceOnSpecificIsland(game, island);
        return game;
    }

    //Character 4 (case 3)
    private Game effect4(Game game, Assistant assistant){
        int playerIndex = -1;
        for(Player playerToCheck: game.getPlayers()){
            if(playerToCheck.getFace_up_assistant().equals(assistant)){
                playerIndex = game.getPlayers().indexOf(playerToCheck);
            }
        }
        game.getPlayers().get(playerIndex).getFace_up_assistant().add2MovementPoints();
        return game;
    }

    //Character 5 (case 4)
    private Game effect5(Game game, Island island) throws Exception{
        int islandIndex;
        islandIndex = game.getArchipelago().indexOf(island);
        if(!game.getArchipelago().get(islandIndex).getNo_entry()){
            game.getArchipelago().get(islandIndex).setNo_entry(true);
            getNoEntry();
        }
        return game;
    }

    //Character 6 (case 5)
    private Game effect6(Game game) throws Exception{
        game = characterFunctions.checkInfluenceWithoutTowers(game);
        return game;
    }

    //Character 7 (case 6)
    private Game effect7(Game game, Player player, LinkedList<Student> studentsToAdd, LinkedList<Student> studentsToRemove){
        int playerIndex;
        playerIndex = game.getPlayers().indexOf(player);
        game.getPlayers().get(playerIndex).getSchool().removeStudents(studentsToAdd);
        game.getPlayers().get(playerIndex).getSchool().putStudents(studentsToRemove);
        students.remove(studentsToRemove);
        students.addAll(studentsToAdd);
        return game;
    }

    //Character 8 (case 7)
    private Game effect8(Game game, Player player) throws Exception{
        game = characterFunctions.checkInfluenceWithBonus(game, player);
        return game;
    }

    //Character 9 (case 8)
    private Game effect9(Game game, Colour colour) throws Exception{
        game = characterFunctions.checkInfluenceWithoutColour(game, colour);
        return game;
    }

    //Character 10 (case 9)
    private Game effect10(Game game, Player player, Student studentInEntrance1, Student studentInEntrance2, Student studentInDiningRoom1, Student studentInDiningRoom2) throws Exception{
        int playerIndex;
        playerIndex = game.getPlayers().indexOf(player);
        game.getPlayers().get(playerIndex).getSchool().removeStudent(studentInEntrance1);
        game.getPlayers().get(playerIndex).getSchool().putStudent(studentInDiningRoom1);
        game.getPlayers().get(playerIndex).getSchool().getDining_room(studentInEntrance1.getColour()).putStudent(studentInDiningRoom1);
        game.getPlayers().get(playerIndex).getSchool().getDining_room(studentInDiningRoom1.getColour()).removeStudent(studentInDiningRoom1);
        if(studentInEntrance2 != null && studentInDiningRoom2 != null){
            game.getPlayers().get(playerIndex).getSchool().removeStudent(studentInEntrance2);
            game.getPlayers().get(playerIndex).getSchool().putStudent(studentInDiningRoom2);
            game.getPlayers().get(playerIndex).getSchool().getDining_room(studentInEntrance2.getColour()).putStudent(studentInDiningRoom2);
            game.getPlayers().get(playerIndex).getSchool().getDining_room(studentInDiningRoom2.getColour()).removeStudent(studentInDiningRoom2);
        }
        return game;
    }

    //Character 11 (case 10)
    private Game effect11(Game game, Player player, Student student) throws Exception{
        students.remove(student);
        player.getSchool().getDining_room(student.getColour()).putStudent(student);
        students.add(game.getBag().removeFirst());
        return game;
    }

    //Character 12 (case 11)
    private Game effect12(Game game, Colour colour) throws Exception{
        for(Player playerToCheck: game.getPlayers()) {
            for (int i = 0; i < numberOfStudentsToRemove; i++) {
                if (playerToCheck.getSchool().getDining_room(colour).getStudents() > 0) {
                    playerToCheck.getSchool().getDining_room(colour).removeStudent(new Student(colour));
                    game.getBag().add(new Student(colour));
                }
            }
        }
        Collections.shuffle(game.getBag());
        return game;
    }
}
