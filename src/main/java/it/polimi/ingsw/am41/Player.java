package it.polimi.ingsw.am41;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;
import java.util.List;

public class Player{

    private final String player_id; // player's unique ID
    private ArrayList<Assistant> assistants; // list of not yet played assistant cards
    private Assistant face_up_assistant; // last played assistant card
    private final TowerColour team; // color of the player's towers, determines the team in 4 player mode
    private boolean turn; // if true it's the player's turn
    private ArrayList<Professor> owned_professor; // list of all the currently owned professors
    private int coins; // number of owned coins
    private SchoolBoard school; // School_board associated with the player
    private int current_influence; // used in Island class to determine if the island can be conquered

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
    }

    // read the assistants card stats from assistants_stats.txt and set up the Assistant list
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

    // increment coins by 1
    public void giveCoin() {
        this.coins++;
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

    public void setOwned_professor(ArrayList<Professor> owned_professor) {
        this.owned_professor = owned_professor;
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

    public void setSchool(SchoolBoard school) {
        this.school = school;
    }

    public int getCurrent_influence() {
        return current_influence;
    }

    public void setCurrent_influence(int current_influence) {
        this.current_influence = current_influence;
    }

}
