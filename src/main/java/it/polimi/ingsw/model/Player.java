package it.polimi.ingsw.model;

import java.io.*;
import java.util.*;

public class Player implements Comparable{
    private String player_id = "new player"; // player's unique ID
    private final LinkedList<Assistant> assistants; // list of not yet played assistant cards
    private final TowerColour team; // color of the player's towers, determines the team in 4 player mode
    private boolean turn; // if true it's the player's turn
    private final SchoolBoard school; // School_board associated with the player
    private final boolean tower_holder; // used to check winners and the end of the game is set true if the player hold the towers for the team
    private int dining_coins; // number of coins obtained by adding students in the dining rooms
    private int playedCharacterNumber; // index of the character played, it only lasts until the end of the turn. It's set to -1 when there are no active character from this player

    // default constructor, creates the player and gives them 8 towers
    public Player(TowerColour team) {
        this.team = team;
        this.turn = false;
        this.assistants = new LinkedList<>();
        //set up assistants deck
        deck_setup();
        this.school = new SchoolBoard(this);
        setCoins(1);
        this.tower_holder = true;
        this.dining_coins = 0;
        this.playedCharacterNumber = -1;
    }

    // alternative constructor, creates the player and gives them nTowers number of tower
    public Player(TowerColour team, int nTowers, int max_entrance) {
        this.team = team;
        this.turn = false;
        this.assistants = new LinkedList<>();
        //set up assistants deck
        deck_setup();
        this.school = new SchoolBoard(nTowers, this, max_entrance);
        setCoins(1);
        this.tower_holder = nTowers != 0;
        this.dining_coins = 0;
        this.playedCharacterNumber = -1;
    }

    /**
     * read the assistants card stats from assistants_stats.txt and set up the Assistant list
     */
    private void deck_setup() {

            try (InputStream in = getClass().getClassLoader().getResourceAsStream("it/polimi/ingsw/assistants_stats.txt");
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))) {

            //Scanner reader = new Scanner(new File("src/main/resources/it/polimi/ingsw/assistants_stats.txt"));
            Scanner reader = new Scanner(bufferedReader);

            while (reader.hasNextInt()) {
                assistants.add(new Assistant(reader.nextInt(), reader.nextInt()));
            }
        } catch (Exception e) {
            System.out.println("Error: ");
            e.printStackTrace();
        }
    }

    /**Uses the coins of this player, if there are enough
     * @param price coins to deduct
     * @throws Exception when there are not enough coins
     */
    public void spend(int price) throws Exception {
        if(this.getCoins()>=price)
            setCoins(getCoins()-price);
        else {
            throw new Exception("Not enough coins!");
        }
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
        return Integer.compare(school.getFace_up_assistant().getValue(), p.getSchool().getFace_up_assistant().getValue());
    }

    /**Plays an assistant
     * @param assistant assistant to play
     * @throws Exception the selected assistant has already been played or doesn't exist
     */
    public void playAssistant(Assistant assistant) throws Exception {
        if(assistants.contains(assistant)){
            setFace_up_assistant(assistant);
            //assistants.remove(assistant);
            assistants.set(assistants.indexOf(assistant),null);
        }
        else {
            throw new Exception("Can't play this assistant");
        }
    }

    /**Plays an assistant
     * @param index index of the assistant to play
     * @throws Exception the selected assistant has already been played or doesn't exist
     */
    public void playAssistant(int index) throws Exception {
        if((assistants.size()>index)&&(index>=0)) {

            setFace_up_assistant(assistants.get(index));
            assistants.remove(index);

        } else {
            throw new Exception("This assistant doesn't exist");
        }
    }

    /**Returns true if this player has a professor with a specific colour, otherwise returns false
     * @param colour colour of the professor to check
     * @return true if this player owns the professor of that colour
     */
    public boolean hasProfessor(Colour colour){
        for(Professor professorToCheck: school.getOwned_professor()){
            if(professorToCheck.getColour().equals(colour)){
                return true;
            }
        }
        return false;
    }

    public void addProfessor(Professor professor){
        school.getOwned_professor().add(professor);
    }

    public void giveCoins(int numberOfCoins) { this.school.setCoins(getCoins() + numberOfCoins); }

    public void setPlayedCharacterNumber(int playedCharacterNumber){
        this.playedCharacterNumber = playedCharacterNumber;
    }

    public int getPlayedCharacterNumber(){
        return playedCharacterNumber;
    }

    public void setCoins(int coins){
        this.school.setCoins(coins);
    }

    public String getPlayer_id() {
        return player_id;
    }

    public LinkedList<Assistant> getAssistants() {
        return assistants;
    }

    public Assistant getFace_up_assistant() {
        return school.getFace_up_assistant();
    }

    public void setFace_up_assistant(Assistant face_up_assistant) {
        school.setFace_up_assistant(face_up_assistant);
    }

    public TowerColour getTeam() {
        return team;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public List<Professor> getOwned_professor() {
        return school.getOwned_professor();
    }

    public int getCoins() {
        return this.school.getCoins();
    }

    public SchoolBoard getSchool() {
        return school;
    }

    public boolean isTower_holder() {
        return tower_holder;
    }

    public void setPlayer_id(String player_id) {
        this.school.setOwner(player_id);
        this.player_id = player_id;
    }
}
