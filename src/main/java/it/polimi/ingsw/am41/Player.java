package it.polimi.ingsw.am41;
// TODO comment

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class Player implements ByPlayer {
    private final String player_id; // player's unique ID
    private ArrayList<Assistant> assistants; // list of not yet played assistant cards
    private Assistant face_up_assistant; // last played assistant card
    private final TowerColor team; // color of the player's towers, determines the team in 4 player mode
    private boolean turn; // if true it's the player's turn
    private ArrayList<Professor> owned_professor; // list of all the currently owned professors
    private int coins; // number of owned coins
    private SchoolBoard school; // School_board associated with the player

    // default constructor, creates the player and gives them 8 towers
    public Player(TowerColor team){
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
    public Player(TowerColor team, int nTowers){
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
    private void deck_setup(){
        try{
            Scanner reader = new Scanner(new File("src/main/resources/it/polimi/ingsw/am41/assistants_stats.txt"));
            while(reader.hasNextInt()){
                assistants.add(new Assistant(reader.nextInt(), reader.nextInt(),this));
            }
        } catch (FileNotFoundException e){
            System.out.println("Error: ");
            e.printStackTrace();
        }
    }

    public ArrayList<Assistant> getAssistants() {
        return assistants;
    }

    // increment coins by 1
    public void giveCoin(){
        this.coins++;
    }
}