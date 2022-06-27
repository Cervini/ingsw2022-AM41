package it.polimi.ingsw.model;

import java.util.*;

public class Game{
    private LinkedList<Player> players; // list of all the active players
    private final int available_coins; // number of all the coins not owned by any player or placed on character cards
    private final LinkedList<Student> bag; // list of all the students
    private final List<Professor> professors; // list of all the professors
    private final List<Island> archipelago; // list of all the islands
    private LinkedList<Player> turnOrder; // playing order of the turn
    private List<Cloud> clouds; // list of all the clouds
    private final LinkedList<Character> selectedCharacters;
    private final GameConclusionChecks conclusionChecks;
    private Player activePlayer;
    private final List<Character> characters;

    // constants
    private static final int minimumNumberOfIslands = 3;
    private static final int starting_students = 120;
    private static final int noEntryCharacterNumber = 4;
    private static final int set4Students = 4;
    private static final int set6Students = 6;
    private static final int characterNeeds4Students1 = 0;
    private static final int characterNeeds6Students = 6;
    private static final int characterNeeds4Students2 = 10;
    private static final int[] specialCharacterNumbers = {1, 5, 7, 8};

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
        gameSetup.placeStudentEntranceSetUp(this);
        selectedCharacters = gameSetup.characterSetup();
        characterSetupStudents(selectedCharacters);
        characters=gameSetup.setAllCharacters();
        conclusionChecks = new GameConclusionChecks();
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

    /**
     * Sets up the students on the character card if necessary
     * @param characters
     */
    private void characterSetupStudents(LinkedList<Character> characters){
        for(Character characterToCheck: characters){
            if(characterToCheck.getCharacterNumber() == characterNeeds4Students1 || characterToCheck.getCharacterNumber() == characterNeeds4Students2){
                for (int i = 0; i < set4Students; i++) {
                    characterToCheck.addStudent(bag.removeFirst());
                }
            }else if(characterToCheck.getCharacterNumber() == characterNeeds6Students) {
                for (int i = 0; i < set6Students; i++) {
                    characterToCheck.addStudent(bag.removeFirst());
                }
            }
        }
    }

    /**
     * set up the playing order for the next turn
     */
    public void setTurnOrder(){
        turnOrder = players;
        Collections.sort(turnOrder);
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
     * @param island1 is the island that has to be merged with island2
     * @param island2 is the island that has to be merged with island1
     */
    public void merge(Island island1, Island island2){
        if(island1.mergeIslands(island2)){
            archipelago.remove(island2);
        }
    }

    /**
     * Moves mother nature of 'movement' position
     * @param movement how much mother nature moves
     * @param player player who tries to move mother nature
     * @throws DistanceMotherNatureException checking the player's played assistant throws exception if movement is greater than the movement points of the assistant
     */
    public void moveMotherNature(int movement, Player player) throws DistanceMotherNatureException{
        if ((player.getFace_up_assistant().getMovement_points() >= movement)||(movement<1)) {
            Island fromIsland = motherNaturePosition();
            int from = archipelago.indexOf(fromIsland);
            archipelago.get(from).setMother_nature(false);
            archipelago.get((from + movement)%archipelago.size()).setMother_nature(true);
            if(!specialCharacterActive()) {
                // run influence check and change owner of the island if possible
                islandCheck(archipelago.get((from + movement) % archipelago.size()));
            }else{
                // run influence check with a character effect active and change the owner of the island if possible
                specialCheck();
            }
        } else {
            throw new DistanceMotherNatureException("Can't move Mother Nature this far!");
        }
        mergeCheck();
    }

    /**
     * finds and returns the island on which the attribute mother_nature is true
     * @return island on which the attribute mother_nature is true
     */
    public Island motherNaturePosition(){
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

    /**
     * moves Professor pawns to the right school boards if necessary
     */
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
     * checks if any player can conquer the island, to be called when an island is subject to change
     * @param island island to be checked
     */
    public void islandCheck(Island island) {
        if(!island.getNo_entry()){
            island.conquerCheck(players);
        } else {
            island.setNo_entry(false);
            getSelectedCharacters().get(findNoEntryCharacter()).returnNoEntry();
        }
    }

    /**
     * moves students from cloud to player's entrance
     * @param cloud chosen cloud
     * @param player player whose entrance is going to be filled
     */
    public void chooseCloud(Cloud cloud, Player player)  {
        /*for(Student s: cloud.getStudents()){
            player.getSchool().putStudent(s);
            cloud.removeStudent(s); }*/
        Iterator<Student> students = cloud.getStudents().iterator();
        while(students.hasNext()){
            Student student = students.next();
            player.getSchool().putStudent(student);
            students.remove();
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
        checkOwnership();
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
     * method to call when all the players have played their Assistant
     */
    public void startTurn(){
        for(Cloud cloud: clouds){
            fillCloud(cloud);
        }
    }

    /**This function is used to play the characters available in each game     *
     * @param playedCharacter selected character to play
     * @param player is the player that activated the effect of the character
     * @param studentList1 it's function depends on whether the characterNumber is 0, 6, 9 or 10
     * @param studentList2 it's function depends on whether the characterNumber is 6 or 9
     * @param island is the island on which the character effect will have consequences
     * @param colour is the colour selected by the player that the character will consider differently
     * @throws Exception when the player doesn't have enough coins for the selected character
     */
    public void playCharacter(Character playedCharacter, Player player, LinkedList<Student> studentList1, LinkedList<Student> studentList2, Island island, Colour colour) throws Exception{
        if(player.getCoins() >= playedCharacter.getCost()) {
            player.spend(playedCharacter.getCost());
            player.setPlayedCharacterNumber(playedCharacter.getCharacterNumber());
            selectedCharacters.get(selectedCharacters.indexOf(playedCharacter)).effect(this, player, studentList1, studentList2, island, colour);
        }else{
            throw new Exception("Not enough coins to play Character " + playedCharacter.getCharacterNumber() + ". You need " + (playedCharacter.getCost() - player.getCoins()) + " more to play it!");
        }
        mergeCheck();
    }

    /**Used to find the character that holds the "noEntry" cards, it works only if there's one
     * @ensures characters.size() == \old(characters.size()) &&
     *          (\forall int i; i>=0 && i<characters.size(); characters.get(i) == \old(characters.get(i)) &&
     *          (\result == -1 || \result == 4) &&
     *          \result == 4 <==> (\exists Character character; ; characters.contains(character) &&
     *              character.getCharacterNumber() == 4)
     */
    private int findNoEntryCharacter(){
        for(Character characterToCheck: selectedCharacters){
            if(characterToCheck.getCharacterNumber() == noEntryCharacterNumber){
                return selectedCharacters.indexOf(characterToCheck);
            }
        }
        return -1;
    }

    /**Used to find the player who played the character
     * @ensures players.size() == \old(players.size()) &&
     *          \result != null <==> (\exists Player player; ; players.contains(player) &&
     *              player.getPlayedCharacterNumber())
     */
    private Player characterPlayer() {
        for(Player playerToCheck: players){
            if(playerToCheck.getPlayedCharacterNumber() >= 0){
                return playerToCheck;
            }
        }
        return null;
    }

    //Used to see if any of characters 2, 6, 8, 9 is being played this turn
    private boolean specialCharacterActive(){
        if(characterPlayer() == null){
            return false;
        }
        for(int numberToCheck: specialCharacterNumbers){
            if(numberToCheck == characterPlayer().getPlayedCharacterNumber()){
                return true;
            }
        }
        return false;
    }

    //Used to run influence check with a character effect active and change the owner of the island if possible
    private void specialCheck() {
            for(Character character: selectedCharacters){
                if(character.getCharacterNumber() == characterPlayer().getPlayedCharacterNumber()){
                    character.activateEffect(this);
                    characterPlayer().setPlayedCharacterNumber(-1);
                    return;
                }
            }
    }

    //Used to check if there are two or more islands close to each other with the same tower on them
    private void mergeCheck() {
        for(int i = 0; i < archipelago.size() - 1; i++){
            if(archipelago.get(i).getColour() != null && archipelago.get(i+1).getColour() != null) {
                if (archipelago.get(i).getColour().equals(archipelago.get(i + 1).getColour())) {
                    merge(archipelago.get(i), archipelago.get(i + 1));
                    i--;
                }
            }
        }
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

    public LinkedList<Character> getSelectedCharacters() { return selectedCharacters;}

    public void setPlayers(LinkedList<Player> players) {
        this.players = players;
    }

    public static class DistanceMotherNatureException extends Exception{
        public DistanceMotherNatureException(String msg){
            super(msg);
        }
    }

    public GameConclusionChecks getConclusionChecks() {
        return conclusionChecks;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }
}