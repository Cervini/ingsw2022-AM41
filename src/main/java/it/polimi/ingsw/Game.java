package it.polimi.ingsw;

import java.util.*;

public class Game {
    private LinkedList<Player> players; // list of all the active players
    private int available_coins; // number of all the coins not owned by any player or placed on character cards
    private LinkedList<Student> bag; // list of all the students
    private List<Professor> professors; // list of all the professors
    private List<Island> archipelago; // list of all the islands
    private LinkedList<Player> turnOrder; // playing order of the turn
    private List<Cloud> clouds; // list of all the clouds
    // private ArrayList<SimpleCharacter> characters; // list of the three character playable in the current match
    private String status; // status of the game

    // constants
    private static final int starting_students = 120;

    /**
     * prepares the game
     * @param numberOfPlayers number of players
     */
    public Game(int numberOfPlayers){
        turnOrder = new LinkedList<>();
        playerSetup(numberOfPlayers);
        coinSetup(numberOfPlayers);
        archipelagoSetup();
        bag.clear();
        bagSetup(starting_students); // generate the other 120 students, 24 for each color
        cloudsSetup(numberOfPlayers);
        professorSetup();
    }

    private void playerSetup(int numberOfPlayers){
        players = new LinkedList<>();
        switch (numberOfPlayers) {
            case 2 -> {
                // default Player constructor (each player will have 8 towers)
                players.add(new Player(TowerColour.WHITE));
                players.add(new Player(TowerColour.BLACK));
            }
            case 3 -> {
                // alternative Player constructor (each player will have 6 towers)
                players.add(new Player(TowerColour.WHITE, 6));
                players.add(new Player(TowerColour.BLACK, 6));
                players.add(new Player(TowerColour.GREY, 6));
            }
            case 4 -> {
                // alternative Player constructor (each team will have 8 towers,
                // with one player holding all the team's towers)
                players.add(new Player(TowerColour.WHITE, 8));
                players.add(new Player(TowerColour.BLACK, 8));
                players.add(new Player(TowerColour.WHITE, 0));
                players.add(new Player(TowerColour.BLACK, 0));
            }
        }
    }

    private void coinSetup(int numberOfPlayers){
        // available coins is set to 20 minus one coin for each player
        available_coins = 20 - numberOfPlayers;
    }

    private void archipelagoSetup(){
        createIslands();
        // as per Eriantys' rule pick 2 students of each color to set up the islands
        bag = new LinkedList<Student>();
        bagSetup(10);
        // place two null "students" in the bag in the positions of the island with Mother Nature anf its opposite
        bag.add(0,null);
        bag.add(6, null);
        placeStudents();
    }

    private void createIslands(){
        // initialize archipelago
        archipelago = new ArrayList<Island>();
        for(int i=0; i<12; i++){
            archipelago.add(new Island());
        }
    }

    /**
     * fills the bag an even number of student for each color, then shuffles the bag
     * @param numberOfStudents total number of students to put in the bag
     */
    private void bagSetup(int numberOfStudents){
        for(Colour color: Colour.values()){
            for(int i=0; i<numberOfStudents/5; i++){
                bag.add(new Student(color));
            }
        }
        // shuffle the students in the bag
        Collections.shuffle(bag);
    }

    private void placeStudents(){
        // place a student on each island from the 10 just created
        for(int i=0; i<12; i++){
            archipelago.get(i).putStudent(bag.get(i));
        }
        // place Mother Nature on the first Island
        archipelago.get(0).setMother_nature(true);
    }

    private void cloudsSetup(int numberOfPlayers){
        // set up the clouds
        clouds = new ArrayList<Cloud>();
        // create clouds
        for(int i=0; i<numberOfPlayers; i++){
            clouds.add(new Cloud());
        }
        // fill clouds
        for(Cloud cloud: clouds){
            fillCloud(cloud, numberOfPlayers);
        }
    }

    private void professorSetup(){
        professors = new ArrayList<Professor>();
        for(Colour colour: Colour.values()){
            professors.add(new Professor(colour));
        }
    }
    
    /**
     * if the cloud given as parameter is empty it gets filled
     * @param cloud cloud to be filled
     */
    public void fillCloud(Cloud cloud, int numberOfPlayers) {
        // if the cloud is empty
        if (cloud.getStudents().size() == 0) {
            // fill the cloud with 3 students picked from bag
            if (numberOfPlayers == 3) {
                for (int i = 0; i < 4; i++) {
                    try {
                        cloud.putStudent(bag.get(0));
                        bag.removeFirst();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    try {
                        cloud.putStudent(bag.get(0));
                        bag.removeFirst();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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
        if(island1.mergeIslands(island2)){
            archipelago.remove(island2);
        }
        return;
    }

    //This function moves mother nature from an island to another by finding the island where it is standing now, changing its boolean value of motherNature to false and setting it to true to another island that is exactly after a number of islands equals to "movement"
    public void moveMotherNature(int movement, Player player) throws Exception {
        if(player.getFace_up_assistant().getMovement_points()>=movement){
            Island fromIsland = motherNaturePosition();
            int from = archipelago.indexOf(fromIsland);
            archipelago.get((from + movement)% archipelago.size()).setMother_nature(true);
            // run influence check and change owner of the island if possible
            islandCheck(archipelago.get((from + movement)% archipelago.size()));
            archipelago.get(from).setMother_nature(false);
        } else {
            throw new Exception("Can't move Mother Nature this far!");
        }
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
     * @param colour color of the professor to remove
     * @param player player who has to get the professor
     */
    public void moveProfessor(Colour colour, Player player){
        Professor prof = new Professor(colour);
        // if the professor is not owned by anyone
        if(professors.contains(prof)){
            professors.remove(prof);
        }else{
            // else look for the professor in players' owned professors
            for(Player player1: players){
                player1.getOwned_professor().remove(prof);
            }
        }
        // add the professor to the player's owned_professor
        player.getOwned_professor().add(prof);
    }

    public void checkOwnership(){
        LinkedList<OwnerInfo> numberOfStudents = new LinkedList<>();
        for(Colour colour: Colour.values()){
            countDiningStudents(colour, numberOfStudents);
            OwnerInfo owner = findMaxValue(numberOfStudents);
            if(goodValue(owner, numberOfStudents))
                moveProfessor(colour, owner.player());
        }
    }

    /**
     *  fills param @numberOfStudents with records holding each player's number of @colour students
     */
    private void countDiningStudents(Colour colour, LinkedList<OwnerInfo> numberOfStudents){
        for(Player player: players){
            OwnerInfo ownerInfo = new OwnerInfo(player.getSchool().getDining_room(colour).getStudents(), player);
            numberOfStudents.addFirst(ownerInfo);
        }
    }

    private OwnerInfo findMaxValue(LinkedList<OwnerInfo> numberOfStudents){
        OwnerInfo ownerInfo = numberOfStudents.getFirst();
        for(OwnerInfo info: numberOfStudents){
            if(info.value()>ownerInfo.value()){
                ownerInfo = info;
            }
        }
        return ownerInfo;
    }

    private boolean goodValue(OwnerInfo owner, LinkedList<OwnerInfo> numberOfStudents){
        boolean isUnique = true;
        numberOfStudents.remove(owner);
        for(OwnerInfo ownerInfo: numberOfStudents){
            if (ownerInfo.value() == owner.value()) {
                isUnique = false;
                break;
            }
        }
        if(owner.value()==0)
            isUnique = false;
        return isUnique;
    }

    /**
     * @param colour
     * @param playerToCheck
     * @return true if playerToCheck owns professor of color colour
     */
    private boolean ownsProfessor(Colour colour, Player playerToCheck) {
        for(Professor professorToCheck : playerToCheck.getOwned_professor()) {
            if (professorToCheck.getColour() == colour) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param colour
     * @param playerToCheck
     * @return professor of specified color from playerToCheck
     */
    private Professor returnProfessor(Colour colour, Player playerToCheck) {
        for(Professor professorToCheck : playerToCheck.getOwned_professor()) {
            if (professorToCheck.getColour() == colour) {
                return professorToCheck;
            }
        }
        return null;
    }

    /**
     * checks if any player can conquer the island, to be called when Mother Nature lands on an island
     * @param island island to be checked
     */
    public void islandCheck(Island island){
        if(!island.getNo_entry()){
            for(Player player: players){
                try {
                    island.conquer(player, players);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            island.setNo_entry(false);
        }

    }

    /**
     * moves students from cloud to player's entrance
     * @param cloud chosen cloud
     * @param player player whose entrance is going to be filled
     */
    public void chooseCloud(Cloud cloud, Player player){
        for(Student s: cloud.getStudents()){
            try {
                player.getSchool().putStudent(s);
                cloud.removeStudent(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * moves student from Tile instance to another
     * @param fromTile tile with student
     * @param toTile tile where the student is going to be moved
     * @param student moved student
     */
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

    /**
     * draws student from bag
     * @return first student in the bag list
     */
    public Student drawStudent(){
        Student drawn;
        drawn = bag.getFirst();
        bag.removeFirst();
        return drawn;
    }

    public LinkedList<Player> getPlayers() {
        return players;
    }

    public int getAvailable_coins() {
        return available_coins;
    }

    public void setAvailable_coins(int available_coins) {
        this.available_coins = available_coins;
    }

    public LinkedList<Student> getBag() {
        return bag;
    }

    public void setBag(LinkedList<Student> bag) {
        this.bag = bag;
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(List<Professor> professors) {
        this.professors = professors;
    }

    public List<Island> getArchipelago() {
        return archipelago;
    }

    public void setArchipelago(List<Island> archipelago) {
        this.archipelago = archipelago;
    }

    public LinkedList<Player> getTurnOrder() {
        return turnOrder;
    }

    public void setTurnOrder(LinkedList<Player> turnOrder) {
        this.turnOrder = turnOrder;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public void setClouds(List<Cloud> clouds) {
        this.clouds = clouds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}