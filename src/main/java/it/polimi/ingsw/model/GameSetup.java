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

    //Function to set up the players, depending on how many are them
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

    //Function to set up coins
    public int coinSetup(int numberOfPlayers){
        // available coins is set to 20 minus one coin for each player
        return initialNumberOfCoins - numberOfPlayers;
    }

    //Function to srt up the archipelago
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

    private void createIslands(){
        // initialize archipelago
        archipelago = new ArrayList<>();
        for(int i=0; i<numberOfIslands; i++){
            archipelago.add(new Island());
        }
    }

    /**
     * fills the bag an even number of student for each color, then shuffles the bag
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

    private void placeStudents(){
        // place a student on each island from the 10 just created
        for(int i=0; i<numberOfIslands; i++){
            archipelago.get(i).putStudent(bag.get(i));
        }
        // place Mother Nature on the first Island
        archipelago.get(0).setMother_nature(true);
    }

    //Function to set up professors
    public List<Professor> professorSetup(){
        List<Professor> professors = new LinkedList<>();
        for(Colour colour: Colour.values()){
            professors.add(new Professor(colour));
        }
        return professors;
    }

    public void placeStudentEntranceSetUp(Game game) {

            for (Player p : game.getPlayers()) {

                switch (game.getPlayers().size()) {

                    case 3 -> {
                        for (int i = 0; i < 9; i++)  //9 students
                        {
                            try {
                                p.getSchool().putStudent(game.drawStudent());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }

                    default -> {
                        for (int i = 0; i < 7; i++)  //7 students
                            try {
                                p.getSchool().putStudent(game.drawStudent());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                    }

                }
                p.setFace_up_assistant(null);
            }
    }

    //Function to set up characters
    public LinkedList<Character> characterSetup() {
        LinkedList<Character> characters = new LinkedList<>();
        LinkedList<Character> selectedCharacters = new LinkedList<>();
        CharacterFunctions characterFunctions = new CharacterFunctions();
        for(int characterNumber = 0; characterNumber < totalNumberOfCharacters; characterNumber++){
            characters.add(new Character(characterNumber));
        }
        Collections.shuffle(characters);
        while(selectedCharacters.size() < numberOfCharacters){
            selectedCharacters.add(characters.removeFirst());
        }
        return selectedCharacters;
    }

    public List<Character> setAllCharacters() {
        LinkedList<Character> characters = new LinkedList<>();
        for (int i=0; i<totalNumberOfCharacters; i++){
            characters.add(new Character(i));
        }
        return characters;
    }
}

