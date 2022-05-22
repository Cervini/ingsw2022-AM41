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

    public LinkedList<Player> playerSetup(int numberOfPlayers){
        LinkedList<Player> players = new LinkedList<>();
        switch (numberOfPlayers) {
            case 2 -> {
                // default Player constructor (each player will have 8 towers)
                players.add(new Player(TowerColour.WHITE, twoOrFourPlayerGameTowers));
                players.add(new Player(TowerColour.BLACK, twoOrFourPlayerGameTowers));
            }
            case 3 -> {
                // alternative Player constructor (each player will have 6 towers)
                players.add(new Player(TowerColour.WHITE, threePlayerGameTowers));
                players.add(new Player(TowerColour.BLACK, threePlayerGameTowers));
                players.add(new Player(TowerColour.GREY, threePlayerGameTowers));
            }
            case 4 -> {
                // alternative Player constructor (each team will have 8 towers,
                // with one player holding all the team's towers)
                players.add(new Player(TowerColour.WHITE, twoOrFourPlayerGameTowers));
                players.add(new Player(TowerColour.BLACK, twoOrFourPlayerGameTowers));
                players.add(new Player(TowerColour.WHITE, 0)); //0 because all the towers have already been added to the first player
                players.add(new Player(TowerColour.BLACK, 0)); //0 because all the towers have already been added to the second player
            }
        }
        return players;
    }

    public int coinSetup(int numberOfPlayers){
        // available coins is set to 20 minus one coin for each player
        return initialNumberOfCoins - numberOfPlayers;
    }

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
        bag = new LinkedList<Student>();
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
                        for (int i = 0; i < 10; i++)  //9 students
                            p.getSchool().putStudent(bag.get(i));
                    }

                    default -> {
                        for (int i = 0; i < 7; i++)  //7 students
                            p.getSchool().putStudent(bag.get(i));
                    }

                }
            }
    }



}

