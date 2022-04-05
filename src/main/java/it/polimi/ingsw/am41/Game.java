package it.polimi.ingsw.am41;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Game {
    List<Player> players = new ArrayList<Player>();
    String status = new String();
    public static final int initialCoinNumber = 20;
    public int availableCoins = initialCoinNumber;
    List<Student> bag = new ArrayList<Student>();
    List<Professor> professors = new ArrayList<Professor>();
    List<Island> archipelago = new ArrayList<Island>();
    Queue<Player> turnOrder = new LinkedList<Player>();
    List<Cloud> clouds = new ArrayList<Cloud>();
    Array<SimpleCharacter> characters = new ArrayList<SimpleCharacter>();

    public void setGame(int numberOfPlayers){
        Island islandToAdd;
        Cloud cloudToAdd;
        for(int i = 0; i < 12; i++){
            islandToAdd = new Island();
            islandToAdd.island();
            archipelago.add(islandToAdd);
        }
        for(int i = 0; i < 4; i++){
            cloudToAdd = new Cloud();
        }
    }


    //In order to decide the order in which the players will play during the second phase of the turn, this method checks the assistants choosen by each player and then orders them from the one who played the assistant with the smallest value, which should go first, to the one who played the assistant with the greatest value, which should go last
    private List<Player> startTurn(){
        List<Player> playerOrder = new ArrayList<Player>();
        List<Assistant> assistants = new ArrayList<Assistant>();
        for(Player selectPlayer : players){
            selectPlayer.faceUpAssistant.player = selectPlayer;
            assistants.add(selectPlayer.faceUpAssistant);
            selectPlayer.faceUpAssistant = null;
        }
        assistants.sort(assistants.value);
        playerOrder.add(assistants.player);
        return playerOrder;
    }


    //The boolean attribute "turn" of the player in the first position of the queue is set to true
    public void checkTurn(Player turnOrder){
        turnOrder.remove().turn = true;
        return;
    }


    //This class is called every time an event there is an opportunity for the game to end, if the game is supposed to end also checks who is the winner
    public void endGame(){
        boolean check = true;
        if(archipelago.size() <= 3){
            checkWinner();
        }
        else if(bag.size() == 0){
            checkWinner();
        }
        else if(check){
            for(Player playerToCheck : players){
                if(playerToCheck.assistants.size() !=0){
                    check = false;
                }
            }
            checkWinner();
        }
        else{
            for(Player playerToCheck : players){
                if(playerToCheck.SchoolBoard.towers == 0){
                    System.out.println(playerToCheck + "wins!");
                }
            }
        }
        return;
    }

    //Sub-function needed by endGame
    private void checkWinner(){
        Player winner = null;
        int towers = 0;
        int tmp = 0;
        boolean tie = false;
        for(Player playerToCheck : players){
            tmp = 0;
            for(Island islandToCheck : archipelago){
                if(playerToCheck.equals(islandToCheck.tower)){
                    tmp = tmp + islandToCheck.islandSize;
                }
            }
            if(towers < tmp){
                winner = playerToCheck;
                towers = tmp;
            }
            else if(towers == tmp){
                tie = true;
            }
        }
        if(!tie){
            System.out.println(winner + "wins!");
        }
        else{
            for(Player playerToCheck : players){
                if(winner == null){
                    winner = playerToCheck;
                }else{
                    if(playerToCheck.ownedProfessors.lenght > winner.ownedProfessors.lenght){
                        winner = playerToCheck;
                    }
                }
            }
            System.out.println(winner + "wins!");
        }
        return;
    }


    //This function has to be executed when two islands that are next to each other are conquered by the same player, this means that they have to merge into a single island
    public Island merge(Island island1, Island island2){
        island1.islandSize = island1.islandSize + island2.islandSize;
        island1.isMotherNature = island1.isMotherNature || island2.isMotherNature;
        island1.isDenyCard = island1.isDenyCard || island2.isDenyCard;
        island1.students.addAll(island2.students);
        return island1;
    }


    //This function moves mother nature from an island to another by finding the island where it is standing now, changing its boolean value of motherNature to false and setting it to true to another island that is exactly after a number of islands equals to "movement"
    public void moveMotherNature(int movement){
        Island fromIsland = findMotherNature();
        Island toIsland = setMotherNature(movement, fromIsland);
        fromIsland.isMotherNature = false;
        toIsland.isMotherNature = true;
        return;
    }

    //Sub-function of moveMotherNature that finds and returns the island on which the attribute isMotherNature is true, this represents the island where mother nature is in the game
    private Island findMotherNature(){
        for(Island islandToCheck : archipelago){
            if(islandToCheck.isMotherNature){
                return islandToCheck;
            }
        }
        return null;
    }

    //Sub-function of moveMotherNature that counts the decides on which island the attribute isMotherNature to true, depending on the value of "movement"
    private Island setMotherNature(int movement, Island formIsland){
        boolean startCount = false;
        int count = 0;
        int iterations = 0;
        Island firstIsland = archipelago.get(0);
        for(Island islandToCheck : archipelago){
            if(islandToCheck.equals(fromIsland)){
                startCount = true;
                if(iterations + movement > archipelago.size()){
                    islandToCheck = firstIsland;
                    movement = iterations + movement - archipelago.size();
                }
            }
            if(count == movement-1){
                return islandToCheck;
            }
            if(startCount){
                count++;
            }
            iterations++;
        }
        return null;
    }


    public void moveProfessor(Color color){
        int maxStudent = 0;
        int tmp = 0;
        Player maxPlayer = null;
        Player ownProfessorPlayer = null;
        int ownProfessorNumber = 0;
        Professor professorToMove = null;
        for(Player playerToCheck : players){
            tmp = studentsInDinningRoom(color, playerToCheck);
            if(tmp>maxStudent){
                maxStudent = tmp;
                maxPlayer = playerToCheck;
            }
            if(ownsProfessor(color, playerToCheck)){
                ownProfessorNumber = tmp;
                ownProfessorPlayer = playerToCheck;
            }
        }
        if(ownProfessorNumber < maxStudent){
            professorToMove = returnProfessor(color, ownProfessorPlayer);
            ownProfessorPlayer.ownedProfessors.remove(professorToMove);
            maxPlayer.ownedProfessors.add(professorToMove);
        }
        return;
    }

    //This function returns how many students are in a Dining Room of a given color and player
    private int studentsInDinningRoom(Color color, Player playerToCheck){
        for(int i = 0; i < playerToCheck.school.diningRoom.lenght; i++){
            if(playerToCheck.school.diningRoom[i].color = color){
                return playerToCheck.school.diningRoom[i].students;
            }
        }
        return -1;
    }

    //This function returns if a player has the professor of a specified color
    private boolean ownsProfessor(Color color, Player playerToCheck) {
        for(Professor professorToCheck : playerToCheck.ownedProfessors) {
            if (professorToCheck.color == color) {
                return true;
            }
        }
        return false;
    }

    //This function returns the professor of a specified color from a specified player
    private Professor returnProfessor(Color color, Player playerToCheck) {
        for(Professor professorToCheck : playerToCheck.ownedProfessors) {
            if (professorToCheck.color == color) {
                return professorToCheck;
            }
        }
        return null;
    }


    //When a player chooses a cloud it takes all the students on top of it and moves them to it's SchoolBoard entrance, this function returns all the students from a cloud and deletes it after so that no other player can choose it again..
    public List<Student> chooseCloud(Cloud cloud){
        cloud.clear();
        return cloud.students;
    }


    public void moveStudent(Tile fromTile, Tile toTile, Student student){
        fromTile.removeStudent(student);
        toTile.addStudent(student);
        return;
    }


    public void playCharacter(SimpleCharacter character, Player player){
        character.effect(player);
        return;
    }


    //This functions returns the player who has the most influence on the island on which is positioned Mother Nature, it requires the subfunctions motherNaturePosition and checkInfluenceByPlayer
    public Player checkInfluence(){
        int influence = 0;
        int tmp = 0;
        Player influencePlayer = null;
        Island islandToCheck = motherNaturePosition();
        for(Player playerToCheck : players){
            tmp = checkInfluenceByPlayer(islandToCheck, playerToCheck);
            if(tmp > influence){
                influence = tmp;
                influencePlayer = playerToCheck;
            }
        }
        return influencePlayer;
    }

    //This function returns the island on which Mother Nature is positioned
    private Island motherNaturePosition(){
        for(Island islandToCheck : archipelago){
            if(islandToCheck.isMotherNature){
                return islandToCheck;
            }
        }
        return null;
    }

    //This function returns the influence given an island and a player
    private int checkInfluenceByPlayer(Island islandToCheck, Player playerToCheck){
        int influence = 0;
        for(Professor currentProfessor : playerToCheck.ownedProfessors){
            for(Student currentStudent : islandToCheck.students){
                if(currentProfessor.color == currentStudent.color){
                    influence = influence + 1;
                }
            }
        }
        if(playerToCheck.equals(islandToCheck.tower)){
            influence = influence + islandToCheck.islandSize;
        }
        return influence;
    }
}
