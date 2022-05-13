package it.polimi.ingsw.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import java.util.Scanner;
import java.util.UUID;

public class Player implements Comparable{

    private String player_id = "new player"; // player's unique ID
    private final ArrayList<Assistant> assistants; // list of not yet played assistant cards
    private Assistant face_up_assistant; // last played assistant card
    private final TowerColour team; // color of the player's towers, determines the team in 4 player mode
    private boolean turn; // if true it's the player's turn
    private final ArrayList<Professor> owned_professor; // list of all the currently owned professors
    private int coins; // number of owned coins
    private final SchoolBoard school; // School_board associated with the player
    private final boolean tower_holder; // used to check winners and the end of the game is set true if the player hold the towers for the team
    private int dining_coins; // number of coins obtained by adding students in the dining rooms

    // default constructor, creates the player and gives them 8 towers
    public Player(TowerColour team) {
        this.team = team;
        this.face_up_assistant = null;
        this.owned_professor = new ArrayList<>();
        this.turn = false;
        this.coins = 1;
        this.assistants = new ArrayList<>();
        //set up assistants deck
        deck_setup();
        this.school = new SchoolBoard();
        this.tower_holder = true;
        this.dining_coins = 0;
    }

    // alternative constructor, creates the player and gives them nTowers number of tower
    public Player(TowerColour team, int nTowers) {
        this.team = team;
        this.face_up_assistant = null;
        this.owned_professor = new ArrayList<>();
        this.turn = false;
        this.coins = 1;
        this.assistants = new ArrayList<>();
        //set up assistants deck
        deck_setup();
        this.school = new SchoolBoard(nTowers);
        this.tower_holder = nTowers != 0;
        this.dining_coins = 0;
    }

    /**
     * read the assistants card stats from assistants_stats.txt and set up the Assistant list
     */
    private void deck_setup() {
        try {
            Scanner reader = new Scanner(new File("src/main/resources/it/polimi/ingsw/assistants_stats.txt"));
            while (reader.hasNextInt()) {
                assistants.add(new Assistant(reader.nextInt(), reader.nextInt(), this));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: ");
            e.printStackTrace();
        }
    }


    // TODO implement a way to check available coins from game (maybe this event should be managed by Game)

    /**
     * method called after moving a student from entrance to dining room,
     * checks if the player in entitled to gain a coin
     */
    public void coin_check(){
        int entitled = 0; // debt is the difference between the earned and given coins
        for(DiningRoom room: school.getDining_rooms()){
            entitled += room.getGiven_coins();
        }
        int difference = entitled - this.dining_coins;
        this.coins += difference;
        this.dining_coins = entitled;
    }

    public void spend(int price) throws Exception {
        if(coins>=price)
            coins -= price;
        else {
            throw new Exception("Not enough coins!");
        }
    }

    public String getPlayer_id() {
        return player_id;
    }

    public ArrayList<Assistant> getAssistants() {
        return assistants;
    }

    public Assistant getFace_up_assistant() {
        return face_up_assistant;
    }

    public void setFace_up_assistant(Assistant face_up_assistant) {
        this.face_up_assistant = face_up_assistant;
    }

    public TowerColour getTeam() {
        return team;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public ArrayList<Professor> getOwned_professor() {
        return owned_professor;
    }

    public int getCoins() {
        return coins;
    }

    public SchoolBoard getSchool() {
        return school;
    }

    public boolean isTower_holder() {
        return tower_holder;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    /**
     * @param o player to compare to
     * @return { if the players' played assistants have the same value return 0;
     *     if this player's assistant has a lower value return -1;
     *     else return 1;
     * }
     */
    @Override
    public int compareTo(Object o) {
        Player p = (Player) o;
        return Integer.compare(this.face_up_assistant.getValue(), p.getFace_up_assistant().getValue());
    }

    public void playAssistant(Assistant assistant) throws Exception {
        if(assistants.contains(assistant)){
            setFace_up_assistant(assistant);
            assistants.remove(assistant);
        }
        else {
            throw new Exception("Can't play this assistant");
        }
    }
}
