package it.polimi.ingsw;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import java.util.Scanner;
import java.util.UUID;

public class Player implements Comparable{

    private final String player_id; // player's unique ID
    private ArrayList<Assistant> assistants; // list of not yet played assistant cards
    private Assistant face_up_assistant; // last played assistant card
    private final TowerColour team; // color of the player's towers, determines the team in 4 player mode
    private boolean turn; // if true it's the player's turn
    private ArrayList<Professor> owned_professor; // list of all the currently owned professors
    private int coins; // number of owned coins
    private SchoolBoard school; // School_board associated with the player
    private boolean tower_holder; // used to check winners and the end of the game is set true if the player hold the towers for the team
    private int dining_coins; // number of coins obtained by adding students in the dining rooms

    // default constructor, creates the player and gives them 8 towers
    public Player(TowerColour team) {
        player_id = UUID.randomUUID().toString();
        this.team = team;
        this.face_up_assistant = null;
        this.owned_professor = new ArrayList<Professor>();
        this.turn = false;
        this.coins = 1;
        this.assistants = new ArrayList<Assistant>();
        //set up assistants deck
        deck_setup();
        this.school = new SchoolBoard();
        this.tower_holder = true;
        this.dining_coins = 0;
    }

    // alternative constructor, creates the player and gives them nTowers number of tower
    public Player(TowerColour team, int nTowers) {
        player_id = UUID.randomUUID().toString();
        this.team = team;
        this.face_up_assistant = null;
        this.owned_professor = new ArrayList<Professor>();
        this.turn = false;
        this.coins = 1;
        this.assistants = new ArrayList<Assistant>();
        //set up assistants deck
        deck_setup();
        this.school = new SchoolBoard(nTowers);
        if(nTowers != 0)
            this.tower_holder = true;
        else
            this.tower_holder = false;
        this.dining_coins = 0;
    }

    /**
     * read the assistants card stats from assistants_stats.txt and set up the Assistant list
     */
    private void deck_setup() {
        try {
            Scanner reader = new Scanner(new File("src/main/resources/it/polimi/ingsw/am41/assistants_stats.txt"));
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

    public String getPlayer_id() {
        return player_id;
    }

    public ArrayList<Assistant> getAssistants() {
        return assistants;
    }

    public void setAssistants(ArrayList<Assistant> assistants) {
        this.assistants = assistants;
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

    public boolean isTurn() {
        return turn;
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

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public SchoolBoard getSchool() {
        return school;
    }

    public boolean isTower_holder() {
        return tower_holder;
    }

    public void setTower_holder(boolean tower_holder) {
        this.tower_holder = tower_holder;
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
        if(this.face_up_assistant.getValue() == p.getFace_up_assistant().getValue())
            return 0;
        if(this.face_up_assistant.getValue() < p.getFace_up_assistant().getValue())
            return -1;
        else{
            return 1;
        }
    }
}
