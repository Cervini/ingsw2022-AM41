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

    //Variables used to memorize variables and play the effect at the required time during the match
    private Player memoryPlayer;
    private Colour memoryColour;


    private static final int numberOfStudentsToRemove = 3;
    private static final int maxNoEntry = 4;

    /** Constructor of Character
     *  @requires characterNumber != null && characterNumber >= 0 && characterNumber < 12
     */
    public Character(int characterNumber){
        this.characterNumber = characterNumber;

        increasedCost = false;
        switch (characterNumber){
            case 0 -> {
                cost = 1;
                description = "Take 1 student from this card and place it on an island of your choice.\nTo use: USE [character index] [student index] [island index]";
            }
            case 1 -> {
                cost = 2;
                description = "During this turn you take control of any number of Professors even if you have the same\nnumber of Students as the player who currently controls them.\nTo use: USE [character index]";
            }
            case 2 -> {
                cost = 3;
                description = "Choose an Island and resolve the Island as if Mother Nature had ended her movement there.\nMother Nature will still move and the Island where she ends her movement will also be resolved.\nTo use: USE [character index] [island index]";
            }
            case 3 -> {
                cost = 1;
                description = "You may move Mother Nature up to 2 additional Islands than is indicated by the Assistant\ncard you've played.\nTo use: USE [character index] [student index] [island index]";
            }
            case 4 -> {
                cost = 2;
                noEntry = maxNoEntry;
                description = "Place a No Entry tile on an Island of your choice. The first time Mother Nature ends her\nmovement there, put the No Entry tile back onto this card DO NOT calculate influence on that Island, or place any Towers.\nTo use: USE [character index] [island index]";
            }
            case 5 -> {
                cost = 3;
                description = "When resolving a Conquering on an Island, Towers do not count towards influence.\nTo use: USE [character index]";
            }
            case 6 -> {
                cost = 1;
                description = "You may take up to 3 Students from this card and replace them with the same number of\nStudents from your Entrance.\nTo use: USE [character index] [character student index]x3 [entrance student index]x3";
            }
            case 7 -> {
                cost = 2;
                description = "During the influence calculation this turn, you count as having 2 more influence.\nTo use: USE [character index]";
            }
            case 8 -> {
                cost = 3;
                description = "Choose a color of Student: during the influence calculation this turn, that color adds no influence.\nTo use: USE [character index] [color to choose]";
            }
            case 9 -> {
                cost = 1;
                description = "You may exchange up to 2 Students between your Entrance and your Dining Room.\nTo use: USE [character index] [entrance student index]x2 [dining room color] [dining room student index]";
            }
            case 10 -> {
                cost = 2;
                description = "Take 1 Student from this card and place it in your Dining Room.\nTo use: USE [character index] [character student index]";
            }
            case 11 -> {
                cost = 3;
                description = "Choose a type of Student: every player (including yourself) must return 3 Students from\ntheir Dining Room to the bag. If any player has fewer tha 3 Students of that type, return as many Students as they have.\nTo use: USE [character index] [color to choose]";
            }
        }
    }

    /** Returns the character number, this value also indicates what kind of effect this character can produce
     *  @ensures \return != null && \return >= 0 && \return < 12;
     */
    public int getCharacterNumber(){
        return characterNumber;
    }

    /** Increases the number of NoEntry of this Character, this happens as a consequence of activating the
     * NoEntry on an island
     * @ensures getNoEntryNumber() > 0 && getNoEntryNumber() <= 4
     */
    public void returnNoEntry(){
        if(noEntry < maxNoEntry) {
            noEntry++;
        }
    }

    /** Returns the cost of playing this character
     * @ensures \return != null && \return > 0
     */
    public int getCost() {return cost; }

    /** Returns the number of NoEntry still available on the island
     * @ensures \return >= 0 && \return <= 4
     */
    public int getNoEntryNumber(){
        return noEntry;
    }

    /** Returns the students situated on this island
     * @ensures (size() == 4 || size() == 6) && (\old(size()) == 4 || \old(size()) == 6)
     */
    public LinkedList<Student> getStudents() {return students;}

    /** Adds the student passed as a parameter to the character
     * @requires student != null
     * @ensures (size() == \old(size()) + 1) && students.contains(student)
     * @param student is the student to be added to the character
     */
    public void addStudent(Student student) {students.add(student);}

    /** Removes a NoEntry from the character because it has to be put on an island
     *  @ensures getNoEntryNumber() == 0    ? null
     *                                      : getNoEntryNumber() = (\old(getNoEntryNumber) - 1)
     *  @throws Exception when there are no NoEntry available
     */
    private void getNoEntry() throws Exception {
        if(noEntry > 0){
            noEntry--;
        }else{
            throw new Exception("All the No Entry cards are already returned");
        }
    }

    /** @requires game != null && player != null && characterNumber != null &&
     *      characterNumber >= 0 && characterNumber < 12
     *  @ensures increasedCost  ? getCost() == \old(getCost())
     *                          : getCost() == (\old(getCost() + 1 )
     * @param game is the current situation of the match that is going to be modified by the character
     * @param player is the player that activated the effect of the character
     * @param studentList1 it's function depends on whether the characterNumber is 0, 6, 9 or 10
     * @param studentList2 it's function depends on whether the characterNumber is 6 or 9
     * @param island is the island on which the character effect will have consequences
     * @param colour is the colour selected by the player that the character will consider differently
     */
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
                effect2(player);
            }
            case 2 -> {
                modifiedGame = effect3(game, island);
            }
            case 3 -> {
                modifiedGame = effect4(game, player);
            }
            case 4 -> {
                modifiedGame = effect5(game, island);
            }
            case 5 -> {
                //Doesn't do anything at this time, it's effect is activated at the end of the turn
            }
            case 6 -> {
                modifiedGame = effect7(game, player, studentList1, studentList2);
            }
            case 7 -> {
                effect8(player);
            }
            case 8 -> {
                effect9(colour);
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

    /**Character 1 (case 0)
     * @requires game != null && selectedStudent != null && island != null
     * @ensures game.getBag().size() == (\old(game.getBag().size() - 1) &&
     *          island.getStudents().contains(selectedStudent)
     * @param game is the current situation of the match that is going to be modified by the character
     * @param selectedStudent is the student that has to be moved from this character to the island
     * @param island is the island to which selectedStudent is going to be moved
     */
    private Game effect1(Game game, Student selectedStudent, Island island){
        int indexOfIslandToEdit;
        students.remove(selectedStudent);
        indexOfIslandToEdit = game.getArchipelago().indexOf(island);
        game.getArchipelago().get(indexOfIslandToEdit).putStudent(selectedStudent);
        students.add(game.drawStudent());
        return game;
    }

    /**Character 2 (case 1)
     * @requires game != null && player != null
     * @param player is the player that activated the effect of the character
     */
    private void effect2(Player player) {
        memoryPlayer = player;
    }

    /**Character 3 (case 2)
     * @requires game != null && island != null
     * @param game is the current situation of the match that is going to be modified by the character
     * @param island island on which it has to be calculated the influence, even if mother nature is not there
     */
    private Game effect3(Game game, Island island) {
        game = characterFunctions.checkInfluenceOnSpecificIsland(game, island);
        return game;
    }

    /**Character 4 (case 3)
     * @requires game != null && player != null
     * @ensures player.getFace_up_assistant().getMovement_points() ==
     *          (\old(player.getFace_up_assistant().getMovement_points()) + 2)
     * @param game is the current situation of the match that is going to be modified by the character
     * @param player is the player that activated the effect of the character
     */
    private Game effect4(Game game, Player player){
        int playerIndex = game.getPlayers().indexOf(player);
        game.getPlayers().get(playerIndex).getFace_up_assistant().add2MovementPoints();
        return game;
    }

    /**Character 5 (case 4)
     * @requires game != null && island != null
     * @ensures island.getNo_entry() &&
     *          getNoEntryNumber() == (\old(getNoEntryNumber() - 1))
     * @param game is the current situation of the match that is going to be modified by the character
     * @param island is the island on which is going to be set a NoEntry
     */
    private Game effect5(Game game, Island island) throws Exception{
        int islandIndex;
        islandIndex = game.getArchipelago().indexOf(island);
        if(!game.getArchipelago().get(islandIndex).getNo_entry()){
            game.getArchipelago().get(islandIndex).setNo_entry(true);
            getNoEntry();
        }
        return game;
    }

    /**Character 7 (case 6)
     * @requires game != null && player != null && studentsSelectedFromPlayer != null &&
     *          studentsSelectedFromCharacter != null &&
     *          studentsSelectedFromPlayer.size() == studentsSelectedFromCharacter.size() &&
     *          this.contains(studentsSelectedFromCharacter)
     * @ensures player.getSchool().getStudents().contains(selectedStudentsFromCharacter) &&
     *          students.size() == \old(students.size()) &&
     *          students.contains(studentsSelectedFromPlayer)
     * @param game is the current situation of the match that is going to be modified by the character
     * @param player is the player that activated the effect of the character
     * @param studentsSelectedFromPlayer students that are going to be moved from the player entrance to
     *                                   this character
     * @param studentsSelectedFromCharacter students that are going to be moved from this character to
     *                                      the player's entrance
     * @throws Exception if studentsSelectedFromCharacter and studentsSelectedFromPlayer are not the same size
     */
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
            throw new Exception("You must select an equal amount of students both from the island and the character");
        }
        return game;
    }

    /**Character 8 (case 7)
     * @requires game != null && player != null
     * @param player is the player that activated the effect of the character
     */
    private void effect8(Player player) {
        memoryPlayer = player;
    }

    /**Character 9 (case 8)
     * @requires game !=  null && colour != null
     * @param colour is the colour selected by the player that will not affect the influence calculations
     *               during this round
     */
    private void effect9(Colour colour) {
        memoryColour = colour;
    }

    /**Character 10 (case 9)
     * @requires game != null && player != null && studentsInEntrance != null && studentsInDiningRoom != null
     *          && studentsInEntrance.size() == studentsInDiningRoom.size() && studentsInEntrance.size() <= 2
     * @ensures player.getSchool().getStudents().contains(studentsInDiningRoom) &&
     *          \old(player.getSchool().getStudents().contains(studentsInEntrance))
     * @param game is the current situation of the match that is going to be modified by the character
     * @param player is the player that activated the effect of the character
     * @param studentsInEntrance students to move from the player's entrance to its dining room
     * @param studentsInDiningRoom students to move from the player's dining room to its entrance
     */
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

    /**Character 11 (case 10)
     * @requires game != null && player != null && student != null && this.contains(student)
     * @ensures player.getSchool().getDining_room(student.getColour()) ==
     *          (\old(player.getSchool().getDining_room(student.getColour())) + 1) &&
     *          game.getBag().size() == (\old(game.getBag().size()) + 1) &&
     *          students.size() == \old(students.size())
     * @param game is the current situation of the match that is going to be modified by the character
     * @param player is the player that activated the effect of the character
     * @param student is the student to be moved from this character to the player's dining room
     */
    private Game effect11(Game game, Player player, Student student) throws Exception{
        students.remove(student);
        player.getSchool().getDining_room(student.getColour()).putStudent(student);
        students.add(game.getBag().removeFirst());
        return game;
    }

    /**Character 12 (case 11)
     * @requires game != null && colour != null
     * @ensures game.getBag().size() >= \old(game.getBag().size())
     * @param game is the current situation of the match that is going to be modified by the character
     * @param colour is the colour of the 3 students that will be removed from each player's dining room
     */
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

    /**Characters 2, 6, 8, 9 need to activate their effect at the end of the turn, even if they are played
     * at another time during the game phase. This function is automatically called by the game when it's
     * the right time to activate these characters' effect
     * @param game is the current situation of the match that is going to be modified by the character
     */
    public Game activateEffect(Game game) throws Exception{
        switch (characterNumber){
            case 1 -> {
                game = characterFunctions.checkInfluenceWithModifiedBoard(game, memoryPlayer);
            }
            case 5 -> {
                game = characterFunctions.checkInfluenceWithoutTowers(game);
            }
            case 7 -> {
                game = characterFunctions.checkInfluenceWithBonus(game, memoryPlayer);
            }
            case 8 -> {
                game = characterFunctions.checkInfluenceWithoutColour(game, memoryColour);
            }
        }
        memoryPlayer = null;
        memoryColour = null;
        return game;
    }

    //Returns a brief description of the character and its effect
    public String getDescription() {
        return description;
    }
}
