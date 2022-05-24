package it.polimi.ingsw.communication;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.ClientHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GamePack implements Serializable {

    private List<Island> islands;
    private List<SchoolBoard> schoolBoards;
    private List<Assistant> assistants;
    private List<Cloud> clouds;


    /**
     * Packs the information that need to be showed to client
     * @param game game to be packed
     * @param client player whose view is being packed
     */
    public GamePack(Game game, ClientHandler client){
        islands = game.getArchipelago();
        schoolBoards = new ArrayList<>();
        for(Player player: game.getPlayers()){
            schoolBoards.add(player.getSchool());
        }
        assistants = new LinkedList<>();
        assistants.addAll(game.getPlayer(client.getUsername()).getAssistants());
        clouds = new LinkedList<>();
        clouds.addAll(game.getClouds());
    }

    public void updateGamePack(Game game, ClientHandler client){
        schoolBoards.clear();
        assistants.clear();
        clouds.clear();
        islands = game.getArchipelago();
        for(Player player: game.getPlayers()){
            schoolBoards.add(player.getSchool());
        }
        assistants.addAll(game.getPlayer(client.getUsername()).getAssistants());
        clouds.addAll(game.getClouds());
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
    }

    public void printPack(){
        System.out.println("\n---------------------------------");
        printArchipelago();
        printClouds();
        System.out.println("---------------------------------");
        printSchoolBoards();
        System.out.println("---------------------------------");
        printClientAssistants();
        System.out.println("---------------------------------");
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
        //System.out.print("Towers: ");
        printIslandTowers(island);
        //System.out.print(" | Students: ");
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
        printFaceUpAssistant(schoolBoard);
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
        if(schoolBoard.getFace_up_assistant()!=null){
            System.out.println("Value: " + schoolBoard.getFace_up_assistant().getValue() + " |Movement: " + schoolBoard.getFace_up_assistant().getMovement_points());
        }
        System.out.println();
    }

    public int countFaceUpAssistant(){
        int count = 0;
        for(SchoolBoard schoolBoard: schoolBoards){
            if(schoolBoard.getFace_up_assistant()!=null)
                count++;
        }
        return count;
    }

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

    public static final String WHITE = "\033[1;97m";
    public static final String GREY = "\033[1;90m";
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\033[1;91m";
    public static final String GREEN = "\033[1;92m";
    public static final String YELLOW = "\033[1;93m";
    public static final String BLUE = "\033[1;94m";
    public static final String PINK = "\033[1;95m";

    public List<SchoolBoard> getSchoolBoards() {
        return schoolBoards;
    }
}
