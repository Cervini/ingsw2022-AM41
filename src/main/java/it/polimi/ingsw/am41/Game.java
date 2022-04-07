package it.polimi.ingsw.am41;

import java.util.*;

public class Game {
    private List<Player> players = new ArrayList<Player>();
    private String status = new String();
    private static final int initialCoinNumber = 20;
    private int availableCoins = initialCoinNumber;
    private List<Student> bag = new ArrayList<Student>();
    private List<Professor> professors = new ArrayList<Professor>();
    private List<Island> archipelago = new ArrayList<Island>();
    private Queue<Player> turnOrder = new LinkedList<Player>();
    private List<Cloud> clouds = new ArrayList<Cloud>();
    private ArrayList<SimpleCharacter> characters = new ArrayList<SimpleCharacter>();

    public void setGame(int numberOfPlayers){
        Island islandToAdd;
        Cloud cloudToAdd;
        int countRed = 0;
        int countGreen = 0;
        int countBlue = 0;
        int countYellow = 0;
        int countPink = 0;
        int random = 0;
        for(int i = 0; i < 12; i++){
            islandToAdd = new Island();
            // TODO islandToAdd.island();
            archipelago.add(islandToAdd);
        }
        for(int i = 0; i < 4; i++){
            cloudToAdd = new Cloud();
        }
        for(int i = 0; i < 120; i++){
            random = (int)Math.random()*(4+1);
            //if()...TODO
        }
    }


    //In order to decide the order in which the players will play during the second phase of the turn, this method checks the assistants chosen by each player and then orders them from the one who played the assistant with the smallest value, which should go first, to the one who played the assistant with the greatest value, which should go last
    private List<Player> startTurn(){
        List<Player> playerOrder = new ArrayList<Player>();
        List<Assistant> assistants = new ArrayList<Assistant>();
        //set up assistants list
        for(Player selectPlayer : players){
            //get the played assistant by each player
            assistants.add(selectPlayer.getFace_up_assistant());
        }
        //order the assistants list (comparator defined in Assistant class uses Value attribute to compare)
        Collections.sort(assistants);
        //set up playerOrder list
        for(Assistant a: assistants){
            //get the owner of each assistant played
            playerOrder.add(a.getPlayer());
        }
        return playerOrder;
    }


    //The boolean attribute "turn" of the player in the first position of the queue is set to true
    public void checkTurn(){
        turnOrder.peek().setTurn(true);
        return;
    }


    //This class is called every time an event there is an opportunity for the game to end, if the game is supposed to end also checks who's the winner
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
                if(playerToCheck.getAssistants().size() !=0){
                    check = false;
                }
            }
            checkWinner();
        }
        else{
            for(Player playerToCheck : players){
                if(playerToCheck.getSchool().getTowers() == 0){
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
                if(playerToCheck.equals(islandToCheck.getTower())){
                    tmp = tmp + islandToCheck.getIsland_size();
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
                    if(playerToCheck.getOwned_professor().size() > winner.getOwned_professor().size()){
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
        island1.setIsland_size(island1.getIsland_size() + island2.getIsland_size());
        island1.setIs_mother_nature(island1.getIs_mother_nature() || island2.getIs_mother_nature());
        island1.setIs_deny_card(island1.getIs_deny_card() || island2.getIs_deny_card());
        island1.getStudents().addAll(island2.getStudents());
        return island1;
    }


    //This function moves mother nature from an island to another by finding the island where it is standing now, changing its boolean value of motherNature to false and setting it to true to another island that is exactly after a number of islands equals to "movement"
    public void moveMotherNature(int movement){
        Island fromIsland = findMotherNature();
        Island toIsland = setMotherNature(movement, fromIsland);
        fromIsland.setIs_mother_nature(false);
        toIsland.setIs_mother_nature(false);
        return;
    }

    //Sub-function of moveMotherNature that finds and returns the island on which the attribute isMotherNature is true, this represents the island where mother nature is in the game
    private Island findMotherNature(){
        for(Island islandToCheck : archipelago){
            if(islandToCheck.getIs_mother_nature()){
                return islandToCheck;
            }
        }
        return null;
    }

    //Sub-function of moveMotherNature that counts the decides on which island the attribute isMotherNature to true, depending on the value of "movement"
    private Island setMotherNature(int movement, Island fromIsland){
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


    public void moveProfessor(Colour colour){
        int maxStudent = 0;
        int tmp = 0;
        Player maxPlayer = null;
        Player ownProfessorPlayer = null;
        int ownProfessorNumber = 0;
        Professor professorToMove = null;
        for(Player playerToCheck : players){
            tmp = studentsInDiningRoom(colour, playerToCheck);
            if(tmp>maxStudent){
                maxStudent = tmp;
                maxPlayer = playerToCheck;
            }
            if(ownsProfessor(colour, playerToCheck)){
                ownProfessorNumber = tmp;
                ownProfessorPlayer = playerToCheck;
            }
        }
        if(ownProfessorNumber < maxStudent){
            professorToMove = returnProfessor(colour, ownProfessorPlayer);
            ownProfessorPlayer.getOwned_professor().remove(professorToMove);
            maxPlayer.getOwned_professor().add(professorToMove);
        }
    }

    //This function returns how many students are in a Dining Room of a given color and player
    private int studentsInDiningRoom(Colour colour, Player playerToCheck){
        Optional<DiningRoom> right_dining = playerToCheck.getSchool().getDining_rooms().stream()
                .filter(n -> n.getColour()==colour).findAny();

        return right_dining.get().getStudents();
    }

    //This function returns if a player has the professor of a specified color
    private boolean ownsProfessor(Colour colour, Player playerToCheck) {
        for(Professor professorToCheck : playerToCheck.getOwned_professor()) {
            if (professorToCheck.getColour() == colour) {
                return true;
            }
        }
        return false;
    }

    //This function returns the professor of a specified color from a specified player
    private Professor returnProfessor(Colour colour, Player playerToCheck) {
        for(Professor professorToCheck : playerToCheck.getOwned_professor()) {
            if (professorToCheck.getColour() == colour) {
                return professorToCheck;
            }
        }
        return null;
    }


    //When a player chooses a cloud it takes all the students on top of it and moves them to it's SchoolBoard entrance, this function returns all the students from a cloud and deletes it after so that no other player can choose it again.
    public void chooseCloud(Cloud cloud, Player player){
        for(Student s: cloud.getStudents()){
            try {
                player.getSchool().putStudent(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cloud.getStudents().clear();
    }


    public void moveStudent(Tile fromTile, Tile toTile, Student student){
        try {
            fromTile.removeStudent(student);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            toTile.putStudent(student);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void playCharacter(SimpleCharacter character){
        character.effect();
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
            if(islandToCheck.getIs_mother_nature()){
                return islandToCheck;
            }
        }
        return null;
    }

    //This function returns the influence given an island and a player
    private int checkInfluenceByPlayer(Island islandToCheck, Player playerToCheck){
        int influence = 0;
        for(Professor currentProfessor : playerToCheck.getOwned_professor()){
            for(Student currentStudent : islandToCheck.getStudents()){
                if(currentProfessor.getColour() == currentStudent.getColour()){
                    influence = influence + 1;
                }
            }
        }
        if(playerToCheck.equals(islandToCheck.getTower())){
            influence = influence + islandToCheck.getIsland_size();
        }
        return influence;
    }
}
