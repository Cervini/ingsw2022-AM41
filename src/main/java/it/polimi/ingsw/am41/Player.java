package it.polimi.ingsw.am41;
// TODO comment

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class Player implements By_Player{
    private static String player_id;
    private ArrayList<Assistant> assistants;
    private Assistant face_up_assistant;
    private Tower_color team;
    private boolean turn;
    private ArrayList<Professor> owned_professor;
    private int coins;
    private School_board school;

    public Player(Tower_color team){
        player_id = UUID.randomUUID().toString();
        this.team = team;
        this.face_up_assistant = null;
        this.owned_professor = new ArrayList<Professor>();
        this.turn = false;
        this.coins = 1;
        this.assistants = new ArrayList<Assistant>();
        //set up assistants deck
        deck_setup();
        this.school = new School_board();
    }

    public Player(Tower_color team, int nTowers){
        player_id = UUID.randomUUID().toString();
        this.team = team;
        this.face_up_assistant = null;
        this.owned_professor = new ArrayList<Professor>();
        this.turn = false;
        this.coins = 1;
        this.assistants = new ArrayList<Assistant>();
        //set up assistants deck
        deck_setup();
        this.school = new School_board(nTowers);
    }

    //read the assistants card stats from file and set up the Assistant list
    private void deck_setup(){
        try{
            Scanner reader = new Scanner(new File("src/main/resources/it/polimi/ingsw/am41/assistants_stats.txt"));
            while(reader.hasNextInt()){
                assistants.add(new Assistant(reader.nextInt(), reader.nextInt()));
            }
        } catch (FileNotFoundException e){
            System.out.println("Error: ");
            e.printStackTrace();
        }
    }

    public ArrayList<Assistant> getAssistants() {
        return assistants;
    }

    public void giveCoin(){
        this.coins++;
    }
}