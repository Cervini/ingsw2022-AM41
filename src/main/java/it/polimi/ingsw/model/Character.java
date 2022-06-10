package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

public class Character implements Serializable {

    private final int characterNumber;
    private int cost;
    private int noEntry;
    private boolean increasedCost;
    private String description;

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
                description = "Take 1 student from this card and place it on an island of your choice.";
            }
            case 1 -> {
                cost = 2;
                description = "During this turn you take control of any number of Professors even if you have the same\nnumber of Students as the player who currently controls them.";
            }
            case 2 -> {
                cost = 3;
                description = "Choose an Island and resolve the Island as if Mother Nature had ended her movement there.\nMother Nature will still move and the Island where she ends her movement will also be resolved.";
            }
            case 3 -> {
                cost = 1;
                description = "You may move Mother Nature up to 2 additional Islands than is indicated by the Assistant\ncard you've played.";
            }
            case 4 -> {
                cost = 2;
                noEntry = maxNoEntry;
                description = "Place a No Entry tile non an Island of your choice. The first time Mother Nature ends her\nmovement there, put the No Entry tile back onto this card DO NOT calculate influence on that Island, or place any Towers.";
            }
            case 5 -> {
                cost = 3;
                description = "When resolving a Conquering on an Island, Towers do not count towards influence";
            }
            case 6 -> {
                cost = 1;
                description = "You may take up to 3 Students from this card and replace them with the same number of\nStudents from your Entrance.";
            }
            case 7 -> {
                cost = 2;
                description = "During the influence calculation this turn, you count as having 2 more influence.";
            }
            case 8 -> {
                cost = 3;
                description = "Choose a color of Student: during the influence calculation this turn, that color adds no influence.";
            }
            case 9 -> {
                cost = 1;
                description = "You may exchange up to 2 Students between your Entrance and your Dining Room.";
            }
            case 10 -> {
                cost = 2;
                description = "Take 1 Student from this card and place it in your Dining Room.";
            }
            case 11 -> {
                cost = 3;
                description = "Choose a type of Student: every player (including yourself) must return 3 Students from\ntheir Dining Room to the bag. If any player has fewer tha 3 Students of that type, return as many Students as they have.";
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

    public int gettNoEntry(){
        return noEntry;
    }

    public LinkedList<Student> getStudents() {return students;}

    public void addStudent(Student student) {students.add(student);}

    private void getNoEntry() throws Exception {
        if(noEntry > 0){
            noEntry--;
        }else{
            throw new Exception("All the No Entry cards are already returned");
        }
    }

    public Game effect(Game game, Player player, LinkedList<Student> studentList1, LinkedList<Student> studentList2, Island island, Colour colour) throws Exception{
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
                modifiedGame = effect4(game, player, player.getFace_up_assistant());
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
                modifiedGame = effect10(game, player, studentList1, studentList2);
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
    private Game effect4(Game game, Player player, Assistant assistant){
        int playerIndex = game.getPlayers().indexOf(player);
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
    private Game effect7(Game game, Player player, LinkedList<Student> studentsSelectedFromPlayer, LinkedList<Student> studentsSelectedFromCharacter) throws Exception{
        int playerIndex;
        playerIndex = game.getPlayers().indexOf(player);
        if(studentsSelectedFromPlayer.size() == studentsSelectedFromCharacter.size()) {
            game.getPlayers().get(playerIndex).getSchool().removeStudents(studentsSelectedFromPlayer);
            game.getPlayers().get(playerIndex).getSchool().putStudents(studentsSelectedFromCharacter);
            for (Student studentToRemove : studentsSelectedFromCharacter) {
                students.remove(studentToRemove);
            }
            students.addAll(studentsSelectedFromPlayer);
        }else{
            throw new Exception("You must select an equal ammount of students both from the island and the character");
        }
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
    private Game effect10(Game game, Player player, LinkedList<Student> studentsInEntrance, LinkedList<Student> studentsInDiningRoom) throws Exception{
        int playerIndex;
        playerIndex = game.getPlayers().indexOf(player);
        game.getPlayers().get(playerIndex).getSchool().removeStudent(studentsInEntrance.getFirst());
        game.getPlayers().get(playerIndex).getSchool().putStudent(studentsInDiningRoom.getFirst());
        game.getPlayers().get(playerIndex).getSchool().getDining_room(studentsInEntrance.getFirst().getColour()).putStudent(studentsInEntrance.getFirst());
        game.getPlayers().get(playerIndex).getSchool().getDining_room(studentsInDiningRoom.getFirst().getColour()).removeStudent(studentsInDiningRoom.getFirst());
        if(studentsInEntrance.size() == 2 && studentsInDiningRoom.size() == 2){
            game.getPlayers().get(playerIndex).getSchool().removeStudent(studentsInEntrance.getLast());
            game.getPlayers().get(playerIndex).getSchool().putStudent(studentsInDiningRoom.getLast());
            game.getPlayers().get(playerIndex).getSchool().getDining_room(studentsInEntrance.getLast().getColour()).putStudent(studentsInEntrance.getLast());
            game.getPlayers().get(playerIndex).getSchool().getDining_room(studentsInDiningRoom.getLast().getColour()).removeStudent(studentsInDiningRoom.getLast());
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

    public String getDescription() {
        return description;
    }
}
