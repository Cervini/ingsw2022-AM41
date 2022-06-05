package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Island implements Tile, Serializable {

    private int island_size; // how many base Island form the island
    private boolean mother_nature; // true if Mother Nature is currently on the island
    private boolean no_entry; // true if a No Entry Card is present on the island
    private final List <Student> students; // list of all the students on the island
    private TowerColour tower = null; // color of the team that controls the island, null if it's no one's

    // constructor creates an empty island of size 1
    public Island(){
        this.island_size = 1;
        this.mother_nature = false;
        this.no_entry = false;
        this.students = new ArrayList<>();
    }

    // This function merges this island with the island it gets as a parameter
    public boolean mergeIslands(Island islandToMerge){
        setIsland_size(island_size + islandToMerge.getIsland_size());
        setMother_nature(mother_nature || islandToMerge.isMother_nature());
        setNo_entry(no_entry || islandToMerge.getNo_entry());
        students.addAll(islandToMerge.getStudents());
        return true;
    }

    /**
     * changes the color of the towers on the island if the player can and does conquer the island
     * removing towers from the conquering team and giving towers back to the conquered team
     * @param players list of all the players
     */
    public void conquerCheck(LinkedList<Player> players) {
        int[] influenceArray = new int[players.size()];
        int maxIndex = 0;
        influenceArray[0] = influence(players.get(0));
        for(int i = 1; i < players.size(); i++){
            influenceArray[i] = influence(players.get(i));
            if(influenceArray[maxIndex] < influenceArray[i]){
                maxIndex = i;
                influenceArray[maxIndex] = influenceArray[i];
            }
        }
        if(onlyOne(influenceArray[maxIndex], influenceArray)){
            conquer(players.get(maxIndex), players);
        }
    }

    /**
     * @param player player whose influence is calculated
     * @return influence player has on the island
     */
    public int influence(Player player) {

        // set starting influence at 0
        int player_influence=0;

        int blue = (int) students.stream().filter(s -> s.getColour() == Colour.BLUE ).count();
        int red = (int) students.stream().filter(s -> s.getColour() == Colour.RED ).count();
        int yellow = (int) students.stream().filter(s -> s.getColour() == Colour.YELLOW ).count();
        int pink = (int) students.stream().filter(s -> s.getColour() == Colour.PINK ).count();
        int green = (int) students.stream().filter(s -> s.getColour() == Colour.GREEN ).count();

        // add influence gained from owned professors
        for (Professor p: player.getOwned_professor()){
            switch (p.getColour()) {
                case BLUE -> player_influence += blue;
                case GREEN -> player_influence += green;
                case PINK -> player_influence += pink;
                case YELLOW -> player_influence += yellow;
                case RED -> player_influence += red;
            }
        }

        // add influence gained by the presence of towers
        if (player.getTeam() == tower)
            player_influence += this.island_size;

        return player_influence;
    }


    /**
     * Process the conquer event, change the tower color of the island and gives back the tower to the players if necessary
     * @param player conquering player
     * @param players list of all game players
     */
    private void conquer(Player player, LinkedList<Player> players) {
        TowerColour old_team = tower;
        this.tower = player.getTeam();
        // take the towers needed by the tower_holder of the team
        if(player.isTower_holder()){
            player.getSchool().takeTowers(this.island_size);
        } else {
            for(Player player1: players){
                if((player1.getTeam()==player.getTeam())&&(player1.isTower_holder())){
                    player1.getSchool().takeTowers(this.island_size);
                }
            }
        }
        // give back the towers to the conquered team
        for(Player player1: players){
            if((player1.getTeam()==old_team)&&(player1.isTower_holder())){
                player1.getSchool().giveTowers(this.island_size);
            }
        }
    }

    //Check that there the highest influence is absolute
    private boolean onlyOne(int value, int[] array){
        int count = 0;
        for(int valueToCheck: array){
            if(valueToCheck == value){
                count++;
            }
        }
        return count == 1;
    }

    public TowerColour getColour(){
        return tower;
    }

    @Override
    public void putStudent(Student student) {
        if(student != null)
            students.add(student);
    }

    @Override
    public void removeStudent(Student student) throws Exception {
        throw new Exception("Students can't be removed from islands");
    }

    //Same function as above, but it gets called when the power of character 6 is active
    public void conquerCheck(LinkedList<Player> players, boolean noTowers) {
        int backupTowers = island_size;
        if(noTowers){
            island_size = 0;
        }
        int[] influenceArray = new int[players.size()];
        int maxIndex = 0;
        influenceArray[0] = influence(players.get(0));
        for(int i = 1; i < players.size(); i++){
            influenceArray[i] = influence(players.get(i));
            if(influenceArray[maxIndex] < influenceArray[i]){
                maxIndex = i;
                influenceArray[maxIndex] = influenceArray[i];
            }
        }
        island_size = backupTowers;
        if(onlyOne(influenceArray[maxIndex], influenceArray)){
            conquer(players.get(maxIndex), players);
        }
    }

    //Same function as above, but it gets called when the power of character 8 is active
    public void conquerCheck(LinkedList<Player> players, Player cardPlayer, int additionalInfluence) throws Exception{
        int[] influenceArray = new int[players.size()];
        int cardPlayerIndex;
        int maxIndex = 0;
        cardPlayerIndex = players.indexOf(cardPlayer);
        influenceArray[cardPlayerIndex] = additionalInfluence;
        influenceArray[0] = influenceArray[0] + influence(players.get(0));
        for(int i = 1; i < players.size(); i++){
            influenceArray[i] = influenceArray[i] + influence(players.get(i));
            if(influenceArray[maxIndex] < influenceArray[i]){
                maxIndex = i;
                influenceArray[maxIndex] = influenceArray[i];
            }
        }
        if(onlyOne(influenceArray[maxIndex], influenceArray)){
            conquer(players.get(maxIndex), players);
        }
    }

    //Same function as above, but it gets called when the power of character 9 is active
    public void conquerCheck(LinkedList<Player> players, Colour colourToExclude) throws Exception{
        int[] influenceArray = new int[players.size()];
        int maxIndex = 0;
        influenceArray[0] = influence(players.get(0), colourToExclude);
        for(int i = 1; i < players.size(); i++){
            influenceArray[i] = influence(players.get(i), colourToExclude);
            if(influenceArray[maxIndex] < influenceArray[i]){
                maxIndex = i;
                influenceArray[maxIndex] = influenceArray[i];
            }
        }
        if(onlyOne(influenceArray[maxIndex], influenceArray)){
            conquer(players.get(maxIndex), players);
        }
    }

    //Same function as above, but it gets called when the power of character 9 is active
    public int influence(Player player, Colour colour) {

        // set starting influence at 0
        int player_influence=0;

        int blue = (int) students.stream().filter(s -> s.getColour() == Colour.BLUE ).count();
        int red = (int) students.stream().filter(s -> s.getColour() == Colour.RED ).count();
        int yellow = (int) students.stream().filter(s -> s.getColour() == Colour.YELLOW ).count();
        int pink = (int) students.stream().filter(s -> s.getColour() == Colour.PINK ).count();
        int green = (int) students.stream().filter(s -> s.getColour() == Colour.GREEN ).count();

        // add influence gained from owned professors
        for (Professor p: player.getOwned_professor()){
            if(!p.getColour().equals(colour)){
                switch (p.getColour()) {
                    case BLUE -> player_influence += blue;
                    case GREEN -> player_influence += green;
                    case PINK -> player_influence += pink;
                    case YELLOW -> player_influence += yellow;
                    case RED -> player_influence += red;
                }
            }
        }

        // add influence gained by the presence of towers
        if (player.getTeam() == tower)
            player_influence += this.island_size;

        return player_influence;
    }


    public int getIsland_size() {
        return island_size;
    }

    public void setIsland_size(int island_size) {
        this.island_size = island_size;
    }

    public boolean isMother_nature() {
        return mother_nature;
    }

    public void setMother_nature(boolean mother_nature) {
        this.mother_nature = mother_nature;
    }

    public boolean getNo_entry() {
        return no_entry;
    }

    public void setNo_entry(boolean no_entry) {
        this.no_entry = no_entry;
    }

    public List<Student> getStudents() {
        return students;
    }

    public TowerColour getTower() {
        return tower;
    }

    public void setTower(TowerColour tower) {
        this.tower = tower;
    }
}