package it.polimi.ingsw.model;

import java.util.*;

public class GameSetup {
    private LinkedList<Student> bag;
    private List<Island> archipelago;

    private static final int twoOrFourPlayerGameTowers = 8;
    private static final int threePlayerGameTowers = 6;
    private static final int initialNumberOfCoins = 20;
    private static final int islandSetupStudentNumber = 10;
    private static final int numberOfIslands = 12;
    private static final int numberOfColours = 5;
    private static final int numberOfCharacters = 3;
    private static final int totalNumberOfCharacters = 12;

    /**Function to set up the players, depending on how many are them
     * @requires numberOfPlayers >= 2 && numberOfPlayers <= 4
     * @param numberOfPlayers int that indicates how many players are taking part in this game
     * @return list of player with a correct entrance size, team and towers
     */
    public LinkedList<Player> playerSetup(int numberOfPlayers){
        LinkedList<Player> players = new LinkedList<>();
        switch (numberOfPlayers) {
            case 2 -> {
                // default Player constructor (each player will have 8 towers)
                players.add(new Player(TowerColour.WHITE, twoOrFourPlayerGameTowers, 7));
                players.add(new Player(TowerColour.BLACK, twoOrFourPlayerGameTowers, 7));
            }
            case 3 -> {
                // alternative Player constructor (each player will have 6 towers)
                players.add(new Player(TowerColour.WHITE, threePlayerGameTowers, 9));
                players.add(new Player(TowerColour.BLACK, threePlayerGameTowers, 9));
                players.add(new Player(TowerColour.GREY, threePlayerGameTowers, 9));
            }
            case 4 -> {
                // alternative Player constructor (each team will have 8 towers,
                // with one player holding all the team's towers)
                players.add(new Player(TowerColour.WHITE, twoOrFourPlayerGameTowers, 7));
                players.add(new Player(TowerColour.BLACK, twoOrFourPlayerGameTowers,7));
                players.add(new Player(TowerColour.WHITE, 0, 7)); //0 because all the towers have already been added to the first player
                players.add(new Player(TowerColour.BLACK, 0, 7)); //0 because all the towers have already been added to the second player
            }
        }
        return players;
    }

    /**Function to set up coins
     * @param numberOfPlayers int that indicates how many players are taking part in this game
     * @return number of coins available
     */
    public int coinSetup(int numberOfPlayers){
        // available coins is set to 20 minus one coin for each player
        return initialNumberOfCoins - numberOfPlayers;
    }

    /**Function to set up the archipelago
     * @return list of islands with the correct number of students on each one
     */
    public List<Island> archipelagoSetup(){
        createIslands();
        // as per Eriantys' rule pick 2 students of each color to set up the islands
        bag = new LinkedList<>();
        bagSetup(islandSetupStudentNumber);
        // place two null "students" in the bag in the positions of the island with Mother Nature anf its opposite
        bag.add(0,null);
        bag.add(6, null);
        placeStudents();
        return archipelago;
    }

    //Creates all the islands
    private void createIslands(){
        // initialize archipelago
        archipelago = new ArrayList<>();
        for(int i=0; i<numberOfIslands; i++){
            archipelago.add(new Island());
        }
    }

    /**Fills the bag an even number of student for each color, then shuffles the bag
     * @param numberOfStudents total number of students to put in the bag
     */
    public LinkedList<Student> bagSetup(int numberOfStudents){
        bag = new LinkedList<>();
        for(Colour color: Colour.values()){
            for(int i=0; i<numberOfStudents/numberOfColours; i++){
                bag.add(new Student(color));
            }
        }
        // shuffle the students in the bag
        Collections.shuffle(bag);
        return bag;
    }

    //Places students on every island except island 0 and island 6, it also places mother nature on island 0
    private void placeStudents(){
        // place a student on each island from the 10 just created
        for(int i=0; i<numberOfIslands; i++){
            archipelago.get(i).putStudent(bag.get(i));
        }
        // place Mother Nature on the first Island
        archipelago.get(0).setMother_nature(true);
    }

    /**Function to set up professors
     * @return list of professors, one for each colour
     */
    public List<Professor> professorSetup(){
        List<Professor> professors = new LinkedList<>();
        for(Colour colour: Colour.values()){
            professors.add(new Professor(colour));
        }
        return professors;
    }

    /**Sets up the entrances for each player
     * @param game current situations of the game
     */
    public void placeStudentEntranceSetUp(Game game) {

            for (Player p : game.getPlayers()) {

                switch (game.getPlayers().size()) {

                    case 3 -> {
                        for (int i = 0; i < 9; i++)  //9 students
                        {

                                p.getSchool().putStudent(game.drawStudent());
                        }

                    }

                    default -> {
                        for (int i = 0; i < 7; i++)  //7 students
                                p.getSchool().putStudent(game.drawStudent());

                    }

                }
                p.setFace_up_assistant(null);
            }
    }

    /**Function to set up characters
     * @return list of characters that are going to be available to play during this game
     */
    public LinkedList<Character> characterSetup() {
        LinkedList<Character> characters = new LinkedList<>();
        LinkedList<Character> selectedCharacters = new LinkedList<>();
        for(int characterNumber = 0; characterNumber < totalNumberOfCharacters; characterNumber++){
            characters.add(new Character(characterNumber));
        }
        Collections.shuffle(characters);
        while(selectedCharacters.size() < numberOfCharacters){
            selectedCharacters.add(characters.removeFirst());
        }
        return selectedCharacters;
    }

    /**Function that generates all 12 characters
     * @return list containing all characters
     */
    public List<Character> setAllCharacters() {
        LinkedList<Character> characters = new LinkedList<>();
        for (int i=0; i<totalNumberOfCharacters; i++){
            characters.add(new Character(i));
        }
        return characters;
    }
}

