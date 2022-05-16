package it.polimi.ingsw.model;

import java.util.*;

public class Game {
    private final LinkedList<Player> players; // list of all the active players
    private final int available_coins; // number of all the coins not owned by any player or placed on character cards
    private final LinkedList<Student> bag; // list of all the students
    private final List<Professor> professors; // list of all the professors
    private final List<Island> archipelago; // list of all the islands
    private LinkedList<Player> turnOrder; // playing order of the turn
    private List<Cloud> clouds; // list of all the clouds
    // private ArrayList<SimpleCharacter> characters; // list of the three character playable in the current match
    private String status; // status of the game

    // constants
    private static final int minimumNumberOfIslands = 3;
    private static final int starting_students = 120;

    /**
     * prepares the game
     * @param numberOfPlayers number of players
     */
    public Game(int numberOfPlayers){
        GameSetup gameSetup = new GameSetup();
        turnOrder = new LinkedList<>();
        players = gameSetup.playerSetup(numberOfPlayers);
        available_coins = gameSetup.coinSetup(numberOfPlayers);
        archipelago = gameSetup.archipelagoSetup();
        bag = gameSetup.bagSetup(starting_students); // generate the other 120 students, 24 for each color
        clouds = cloudsSetup(numberOfPlayers);
        professors = gameSetup.professorSetup();
    }

    private List<Cloud> cloudsSetup(int numberOfPlayers){
        clouds = new ArrayList<>();
        for(int i=0; i<numberOfPlayers; i++){   // create clouds
            if(numberOfPlayers == 3)
                clouds.add(new Cloud(4));
            else
                clouds.add(new Cloud());
        }
        for(Cloud cloud: clouds){
            fillCloud(cloud);
        }
        return clouds;
    }

    /**
     * if the cloud given as parameter is empty it gets filled
     * @param cloud cloud to be filled
     */
    public void fillCloud(Cloud cloud) {
        if (cloud.getStudents().size() == 0) {  // if the cloud is empty
            for(int i=0; i<cloud.getMaxStudents(); i++){
                try {
                    cloud.putStudent(drawStudent());
                } catch (Exception e) {
                    System.out.println(e);
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
    }

    /**
     * @return winner team or null if the game has to continue
     */
    public TowerColour endGame(){
        TowerColour winner;
        GameConclusionChecks check = new GameConclusionChecks();
        winner = check.endBecauseOfArchipelagoSize(minimumNumberOfIslands, archipelago, players);
        winner = check.endBecauseAvailableStudentsFinished(bag, archipelago, players, winner);
        winner = check.endBecauseAvailableAssistantsFinished(archipelago, players, winner);
        winner = check.endBecauseAvailableTowersFinished(players, winner);
        return winner;
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
    }

    //This function moves mother nature from an island to another by finding the island where it is standing now, changing its boolean value of motherNature to false and setting it to true to another island that is exactly after a number of islands equals to "movement"
    public void moveMotherNature(int movement, Player player) throws Exception {
        if(player.getFace_up_assistant().getMovement_points()>=movement){
            Island fromIsland = motherNaturePosition();
            int from = archipelago.indexOf(fromIsland);
            archipelago.get((from + movement)% archipelago.size()).setMother_nature(true);
            // run influence check and change owner of the island if possible
            //islandCheck(archipelago.get((from + movement)% archipelago.size()));
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
        // remove the professor from professor in case it is still not owned by anyone
        for(Professor professor: professors){
            if(professor.getColour()==colour){
                player.getOwned_professor().add(professor);
                professors.remove(professor);
                break;
            }
        }
        // remove the professor from the owner and add it to player's owned professors
        for(Player old: players){
            for(Professor professor: old.getOwned_professor()){
                if(professor.getColour()==colour){
                    old.getOwned_professor().remove(professor);
                    player.getOwned_professor().add(professor);
                    break;
                }
            }
        }
    }

    public void checkOwnership(){
        LinkedList<OwnerInfo> numberOfStudents = new LinkedList<>();
        for(Colour colour: Colour.values()){
            countDiningStudents(colour, numberOfStudents);
            OwnerInfo owner = findMaxValue(numberOfStudents);
            if(goodValue(owner, numberOfStudents))
                moveProfessor(colour, owner.player());
            numberOfStudents.clear(); // clear the list for the next cycle
        }
    }

    /**
     *  fills param @numberOfStudents with records holding each player's number of @colour students
     */
    public void countDiningStudents(Colour colour, LinkedList<OwnerInfo> numberOfStudents){
        for(Player player: players){
            OwnerInfo ownerInfo = new OwnerInfo(player.getSchool().getDining_room(colour).getStudents(), player);
            numberOfStudents.addFirst(ownerInfo);
        }
    }

    /**
     * @param numberOfStudents list to check
     * @return OwnerInfo with the maximum value of the list,
     * if all the OwnerInfo have same values returns the first in the list
     */
    public OwnerInfo findMaxValue(LinkedList<OwnerInfo> numberOfStudents){
        OwnerInfo ownerInfo = numberOfStudents.getFirst();
        for(OwnerInfo info: numberOfStudents){
            if(info.value()>ownerInfo.value()){
                ownerInfo = info;
            }
        }
        return ownerInfo;
    }

    /**
     * @param owner
     * @param numberOfStudents
     * @return true if the owner parameter has value > 0 and is not tied with any other owner
     */
    public boolean goodValue(OwnerInfo owner, LinkedList<OwnerInfo> numberOfStudents){
        if(owner.value()==0)
            return false;
        boolean isUnique = true;
        numberOfStudents.remove(owner);
        for(OwnerInfo ownerInfo: numberOfStudents){
            if (ownerInfo.value() == owner.value()) {
                return false;
            }
        }
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
     * checks if any player can conquer the island, to be called when an island is subject to change
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
    public void chooseCloud(Cloud cloud, Player player) throws Exception {
        for(Student s: cloud.getStudents()){
            player.getSchool().putStudent(s);
            cloud.removeStudent(s);
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
            System.out.println(e);
        }
        try {
            toTile.putStudent(student);
        } catch (Exception e) {
            System.out.println(e);
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

    public LinkedList<Student> getBag() {
        return bag;
    }

    public List<Island> getArchipelago() {
        return archipelago;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public List<Professor> getProfessors(){
        return professors;
    }

    public LinkedList<Player> getTurnOrder() {
        return turnOrder;
    }

    /**
     * @return Player instanced with 'username' parameter as player_id
     */
    public  Player getPlayer(String username){
        for(Player player: this.players){
            if(player.getPlayer_id().equals(username))
                return player;
        }
        return null;
    }

    /**
     * method to call when all the players have finished their turn
     */
    public void endTurn(){
        for(Player player: players){
            player.setFace_up_assistant(null);
        }
    }

    /**
     * method to call when all the players have played their Assistant
     */
    public void startTurn(){
        for(Cloud cloud: clouds){
            fillCloud(cloud);
        }
    }
}