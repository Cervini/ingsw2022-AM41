package it.polimi.ingsw.communication;

import it.polimi.ingsw.communication.messages.CharacterInfo;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.server.ClientHandler;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GamePack implements Serializable {

    private List<Island> islands;
    private final List<SchoolBoard> schoolBoards;
    private final List<Assistant> assistants;
    private final List<Cloud> clouds;
    private final LinkedList<CharacterInfo> characters;

    /**
     * Packs the information that need to be showed to client
     * @param game game to be packed
     * @param client player whose view is being packed
     */
    public GamePack(Game game, ClientHandler client) {
        islands = game.getArchipelago(); // get islands from game
        schoolBoards = new ArrayList<>(); // create empty list of school boards
        for (Player player : game.getPlayers()) {
            schoolBoards.add(player.getSchool()); // for each player get the school board
        }
        assistants = new LinkedList<>(); // create empty list of Assistants
        assistants.addAll(game.getPlayer(client.getUsername()).getAssistants()); // get the Assistants from the client player
        clouds = new LinkedList<>(); // create empty list of clouds
        clouds.addAll(game.getClouds()); // get the clouds from game
        characters = new LinkedList<CharacterInfo>(); // create empty list of clouds
        for(Character character: game.getSelectedCharacters()){
            getCharInfo(characters, character);
        }
    }

    /**
     * updates the information of the same GamePack given a new game status
     */
    public void updateGamePack(Game game, ClientHandler client){
        //clear all the lists containing data
        schoolBoards.clear();
        assistants.clear();
        clouds.clear();
        characters.clear();
        //refill the lists as done in constructor
        islands = game.getArchipelago();
        for(Player player: game.getPlayers()){
            schoolBoards.add(player.getSchool());
        }
        assistants.addAll(game.getPlayer(client.getUsername()).getAssistants());
        clouds.addAll(game.getClouds());
        for(Character character: game.getSelectedCharacters()){
            getCharInfo(characters, character);
        }
    }

    /**
     * Packs the information that need to be showed to the first player -- for TEST
     * @param game game to be packed
     */
    public GamePack(Game game){
        islands = game.getArchipelago();
        schoolBoards = new ArrayList<>();
        for(Player player: game.getPlayers()){
            schoolBoards.add(player.getSchool());
        }
        assistants = new LinkedList<>();
        assistants.addAll(game.getPlayers().getFirst().getAssistants());
        clouds = new LinkedList<>();
        clouds.addAll(game.getClouds());
        characters = new LinkedList<>(); // create empty list of clouds
        for(Character character: game.getSelectedCharacters()){
            getCharInfo(characters, character);
        }
    }

    /**
     * Creates a new CharacterInfo instance and adds it to a List of CharacterInfo
     * @param characters list
     * @param character character to get the info from
     */
    private void getCharInfo(LinkedList<CharacterInfo> characters, Character character) {
        int cost = character.getCost();
        String description = character.getDescription();
        List<Student> students = character.getStudents();
        int noEntry = character.getNoEntryNumber();
        CharacterInfo characterInfo = new CharacterInfo(cost, description, students, noEntry);
        characters.add(characterInfo);
    }

    /**
     * prints the state of the game saved by the GamePack (CLI use)
     */
    public void printPack(){
        System.out.println("\n---------------------------------");
        printArchipelago();
        printClouds();
        System.out.println("---------------------------------");
        printCharacters();
        System.out.println("---------------------------------");
        printSchoolBoards();
        System.out.println("---------------------------------");
        printClientAssistants();
        System.out.println("---------------------------------");
    }

    private void printCharacters() {
        for(int i = 0; i< characters.size(); i++){
            System.out.print("| Character "+ i +" | cost: "+ characters.get(i).cost()+" |");
            if(characters.get(i).students().size()!=0){
                for(Student student: characters.get(i).students()){
                    System.out.print(changeColorStudent(student.getColour()));
                    System.out.print("S ");
                    System.out.print(RESET);
                }
                System.out.print(" | ");
            }
            if(characters.get(i).noEntry()!=0){
                System.out.print(" No Entry Cards: " + characters.get(i).noEntry());
            }
            System.out.println();
        }
    }

    private void printArchipelago(){
        for(Island island: islands){
            System.out.print("|"+islands.indexOf(island)+"|-> ");
            printIsland(island);
        }
    }

    private void printClouds(){
        for(Cloud cloud: clouds){
            System.out.print("|Cloud " + clouds.indexOf(cloud) + "| ->");
            for(Student student: cloud.getStudents()){
                System.out.print(changeColorStudent(student.getColour()));
                System.out.print("S ");
                System.out.print(RESET);
            }
            System.out.println();
        }
    }
    private void printIsland(Island island){
        printIslandTowers(island);
        printIslandStudents(island);
        if(island.isMother_nature())
            System.out.print("| M |");
        System.out.println();
    }

    private void printIslandTowers(Island island){
        if(island.getColour()!=null){
            System.out.print(changeColorTower(island.getColour()));
            for(int i=0; i< island.getIsland_size(); i++){
                System.out.print("T ");
            }
            System.out.print(RESET);
        } else {
            System.out.print("- ");
        }

    }

    private void printIslandStudents(Island island){
        for(Student student: island.getStudents()){
            System.out.print(changeColorStudent(student.getColour()));
            System.out.print("S " + RESET);
        }
    }

    private void printSchoolBoards(){
        for(SchoolBoard schoolBoard: schoolBoards){
            printSchoolBoard(schoolBoard);
        }
    }

    private void printSchoolBoard(SchoolBoard schoolBoard){
        System.out.println("\nPlayer: " + changeColorTower(schoolBoard.getTeam()) + schoolBoard.getOwner() + RESET);
        System.out.print("Entrance: ");
        printEntranceStudents(schoolBoard);
        System.out.println();
        for(DiningRoom diningRoom: schoolBoard.getDining_rooms()){
            printDiningStudents(diningRoom);
        }
        printSchoolBoardTowers(schoolBoard);
        printProfessors(schoolBoard);
        printFaceUpAssistant(schoolBoard);
    }

    private void printProfessors(SchoolBoard schoolBoard){
        System.out.print("Professors: ");
        for(Professor professor: schoolBoard.getOwned_professor()){
            System.out.print(changeColorStudent(professor.getColour()));
            System.out.print(" P" + RESET);
        }
        System.out.println();
    }

    private void printEntranceStudents(SchoolBoard schoolBoard){
        for(Student student: schoolBoard.getEntrance()){
            System.out.print(changeColorStudent(student.getColour()));
            System.out.print("S " + RESET);
        }
    }

    private void printDiningStudents(DiningRoom diningRoom){
        System.out.print(changeColorStudent(diningRoom.getColour()));
        System.out.print(diningRoom.getColour() + ": ");
        for(int i=0; i<diningRoom.getStudents(); i++){
            System.out.print("S ");
        }
        System.out.print(RESET+"\n");
    }

    private void printSchoolBoardTowers(SchoolBoard schoolBoard){
        System.out.print("Towers: " + changeColorTower(schoolBoard.getTeam()));
        for(int i=0; i<schoolBoard.getTowers(); i++){
            System.out.print("T ");
        }
        System.out.println(RESET);
    }

    private void printClientAssistants(){
        System.out.println("Your Assistants:");
        for(Assistant assistant: assistants){
            System.out.print("|"+assistants.indexOf(assistant)+"|-> ");
            printAssistant(assistant);
        }
    }

    private void printAssistant(Assistant assistant){
        System.out.println("Value: " + assistant.getValue() + " |Movement: " + assistant.getMovement_points());
    }

    void printFaceUpAssistant(SchoolBoard schoolBoard){
        System.out.print("Last played Assistant|-> ");
        try {
            System.out.println("Value: " + schoolBoard.getFace_up_assistant().getValue() + " |Movement: " + schoolBoard.getFace_up_assistant().getMovement_points());
        } catch (NullPointerException e) {
            System.out.print("Not played yet");
        }
        System.out.println();
    }

    /**
     * @param colour color defining the print color change
     * @return string that changes the color of the next printed strings based on colour param
     */
    private String changeColorTower(TowerColour colour){
        if(colour!=null){
            switch(colour){
                case WHITE -> {
                    return WHITE;
                }
                case BLACK -> {
                    return BLACK;
                }
                case GREY -> {
                    return GREY;
                }
            }
        }
        return null;
    }

    /**
     * @param colour color defining the print color change
     * @return string that changes the color of the next printed strings based on colour param
     */
    private String changeColorStudent(Colour colour){
        switch(colour){
            case PINK -> {return PINK;}
            case BLUE -> {return BLUE;}
            case RED -> {return RED;}
            case GREEN -> {return GREEN;}
            case YELLOW -> {return YELLOW;}
        }
        return null;
    }

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\033[1;91m";
    public static final String GREEN = "\033[1;92m";
    public static final String YELLOW = "\033[1;93m";
    public static final String BLUE = "\033[1;94m";
    public static final String PINK = "\033[1;95m";
    public static final String WHITE = "\033[1;97m";
    public static final String GREY = "\033[1;90m";
}