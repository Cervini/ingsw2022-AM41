package it.polimi.ingsw.am41;

import java.util.*;

public class Game {
    private LinkedList<Player> players; // list of all the active players
    private int available_coins; // number of all the coins not owned by any player or placed on character cards
    private LinkedList<Student> bag; // list of all the students
    private List<Professor> professors; // list of all the professors
    private List<Island> archipelago; // list of all the islands
    private LinkedList<Player> turnOrder; // playing order of the turn
    private List<Cloud> clouds; // list of all the clouds
    private ArrayList<SimpleCharacter> characters; // list of the three character playable in the current match
    private String status; // status of the game

    // Game constructor sets up the game
    public Game(int numberOfPlayers){
        turnOrder = new LinkedList<>();
        // create and set up the players
        players = new LinkedList<Player>();
        switch(numberOfPlayers){
            case 2: {
                // default Player constructor (each player will have 8 towers)
                players.add(new Player(TowerColour.WHITE));
                players.add(new Player(TowerColour.BLACK));
            } break;
            case 3: {
                // alternative Player constructor (each player will have 6 towers)
                players.add(new Player(TowerColour.WHITE, 6));
                players.add(new Player(TowerColour.BLACK, 6));
                players.add(new Player(TowerColour.GREY, 6));
            } break;
            case 4: {
                // alternative Player constructor (each team will have 8 towers,
                // with one player holding all the team's towers)
                players.add(new Player(TowerColour.WHITE, 8));
                players.add(new Player(TowerColour.BLACK, 8));
                players.add(new Player(TowerColour.WHITE, 0));
                players.add(new Player(TowerColour.BLACK, 0));
            }
        }

        // available coins is set to 20 minus one coin for each player
        available_coins = 20 - numberOfPlayers;
        // initialize archipelago
        archipelago = new ArrayList<Island>();
        for(int i=0; i<12; i++){
            archipelago.add(new Island());
        }
        // as per Eriantys' rule pick 2 students of each color to set up the islands
        bag = new LinkedList<Student>();
        for(Colour colour: Colour.values()){
            bag.add(new Student(colour));
            bag.add(new Student(colour));
        }
        // shuffle the 10 students
        Collections.shuffle(bag);
        // place two null "students" in the bag in the positions of the island with Mother Nature anf its opposite
        bag.add(0,null);
        bag.add(6, null);
        // place a student on each island from the 10 just created
        for(int i=0; i<12; i++){
            archipelago.get(i).putStudent(bag.get(i));
        }
        // place Mother Nature on the first Island
        archipelago.get(0).setMother_nature(true);
        bag.clear();
        // generate the other 120 students, 24 for each color
        for(Colour colour: Colour.values()){
            for(int i=0; i<24; i++){
                bag.add(new Student(colour));
            }
        }
        // shuffle the students in the bag
        Collections.shuffle(bag);
        // set up the clouds
        clouds = new ArrayList<Cloud>();
        // create clouds
        for(int i=0; i<numberOfPlayers; i++){
            clouds.add(new Cloud());
        }
        // fill clouds
        for(Cloud cloud: clouds){
            fillCloud(cloud);
        }
        // set up professors
        professors = new ArrayList<Professor>();
        for(Colour colour: Colour.values()){
            professors.add(new Professor(colour));
        }
        // TODO set up characters
    }

    /**
     * if the cloud given as parameter is empty it gets filled
     * @param cloud cloud to be filled
     */
    public void fillCloud(Cloud cloud){
        // if the cloud is empty
        if(cloud.getStudents().size()==0){
            // fill the cloud with 3 students picked from bag
            for(int i=0; i<3; i++){
                cloud.putStudent(bag.get(0));
                bag.removeFirst();
            }
        }
    }

    // set up the playing order for the next turn
    public void setTurnOrder(){
        turnOrder = players;
        Collections.sort(turnOrder);
    }

    //The boolean attribute "turn" of the player in the first position of the queue is set to true
    public void checkTurn(){
        turnOrder.peek().setTurn(true);
        return;
    }

    /**
     * @return winner team or null if the game has to continue
     */
    public TowerColour endGame(){
        Player winner;
        if(archipelago.size() <= 3){
            winner = checkWinner();
            return winner.getTeam();
        }
        if(bag.size() == 0){
            winner = checkWinner();
            return winner.getTeam();
        }
        for(Player playerToCheck : players){
                if(playerToCheck.getAssistants().size()==0){
                    winner = checkWinner();
                    return winner.getTeam();
                }
        }
        for(Player playerToCheck : players){
            if((playerToCheck.isTower_holder())&&(playerToCheck.getSchool().getTowers() == 0)){
                return playerToCheck.getTeam();
            }
        }
        return null;
    }

    /**
     * method used in endGame
     * @return the player who meets the winning conditions
     */
    private Player checkWinner(){
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
            return winner;
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
            return winner;
        }
    }


    //This function has to be executed when two islands that are next to each other are conquered by the same player, this means that they have to merge into a single island

    /**
     * merges island2 into island1
     * @param island1
     * @param island2
     */
    public void merge(Island island1, Island island2){
        island1.setIsland_size(island1.getIsland_size() + island2.getIsland_size());
        island1.setMother_nature(island1.isMother_nature() || island2.isMother_nature());
        island1.setDeny_card(island1.getDeny_card() || island2.getDeny_card());
        island1.getStudents().addAll(island2.getStudents());
        archipelago.remove(island2);
    }

    //This function moves mother nature from an island to another by finding the island where it is standing now, changing its boolean value of motherNature to false and setting it to true to another island that is exactly after a number of islands equals to "movement"
    public void moveMotherNature(int movement){
        Island fromIsland = motherNaturePosition();
        int from = archipelago.indexOf(fromIsland);
        archipelago.get((from + movement)% archipelago.size()).setMother_nature(true);
        archipelago.get(from).setMother_nature(false);
    }

    /**
     * finds and returns the island on which the attribute mother_nature is true
     * @return island on which the attribute mother_nature is true
     */
    private Island motherNaturePosition(){
        for(Island islandToCheck : archipelago){
            if(islandToCheck.isMother_nature()){
                return islandToCheck;
            }
        }
        return null;
    }

    /**
     * moves the professor of the parameter colour from its current position to the parameter player
     * @param colour
     * @param player
     */
    public void moveProfessor(Colour colour, Player player){
        Professor prof = new Professor(colour);
        // if the professor is not owned by anyone
        if(professors.contains(prof)){
            professors.remove(prof);
        }else{
            // else look for the professor in players' owned professors
            for(Player player1: players){
                if(player1.getOwned_professor().contains(prof))
                    player1.getOwned_professor().remove(prof);
            }
        }
        // add the professor to the player's owned_professor
        player.getOwned_professor().add(prof);
    }

    /**
     * moves professors according to the number of students in players' dining rooms
     */
    public void checkOwnership(){
        int tmp; // temp variable used to check the maximum
        LinkedList<Integer> tie_checker = new LinkedList<Integer>(); // temp list used to check possible ties
        Player owner = null;
        for(Colour colour: Colour.values()){ // for each color
            // set as greatest number of students the number of students owned by the first player
            tmp = players.get(0).getSchool().getDining_room(colour).getStudents();
            tie_checker.add(tmp);
            if(tmp!=0)
                // if the first player has any student temporarily set them as the next owner
                owner = players.get(0);
            for(Player playerToCheck: players) {
                // for each player
                if(playerToCheck.getSchool().getDining_room(colour).getStudents()>=tmp){
                   // if they own more students the possible next owner is changed
                   owner = playerToCheck;
                   tmp = playerToCheck.getSchool().getDining_room(colour).getStudents();
                   // add the number of students in the tie_checker list
                   tie_checker.add(tmp);
                }
            }
            // tie check
            boolean tie = false;
            for(Integer x: tie_checker){
                // tmp contains the maximum number of students found
                if(tmp == x){
                    // if found more than once the ownership is tied
                    tie = true;
                }
            }
            // professors are moved only if ownership is no tied
            if(!tie){
                moveProfessor(colour, owner);
            }
        }
    }

    //This function returns true if a player has the professor of a specified color
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


    // NOT SURE IF THIS METHOD IS NEEDED (ALREADY PRESENT IN Island CLASS)
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
