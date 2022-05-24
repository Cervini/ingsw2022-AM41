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

    public TowerColour getColour(){
        return tower;
    }

    // constructor creates an empty island of size 1
    public Island(){
        this.island_size = 1;
        this.mother_nature = false;
        this.no_entry = false;
        this.students = new ArrayList<>();
    }

    @Override
    public void putStudent(Student student) {
        if(student != null)
            students.add(student);
    }

    @Override
    public void removeStudent(Student student) throws Exception {
        throw new Exception("Students can't be removed from islands"); // TODO better exception
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
     * @param player player whose capability of conquering the island is measured
     * @param players list of all the players
     * @return true if the player has the most influence on the island
     */
    public boolean canConquer(Player player, LinkedList<Player> players) {
        // get the influence of the conquering player
        int player_influence = influence(player);
        for(Player p: players){
            // if any of the other players has greater or equal influence
            if((p!=player)&&(influence(p)>=player_influence))
                // return false: the player can't conquer the island
                return false;
        }
        // if no one has greater or equal influence the player can conquer the island
        return true;
    }

    /**
     * changes the color of the towers on the island if the player can and does conquer the island
     * removing towers from the conquering team and giving towers back to the conquered team
     * @param player player conquering the island
     * @param players list of all the players
     * @throws Exception Player can't conquer the island
     */
    public void conquer(Player player, LinkedList<Player> players) throws Exception {
        TowerColour old_team = tower;
        // if the player can conquer the island
        if(canConquer(player, players)){
            // change the colour of the towers
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
        else {
            throw  new Exception("Player can't conquer the island"); // TODO define better exception
        }
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

    // This function merges this island with the island it gets as a parameter
    public boolean mergeIslands(Island islandToMerge){
        setIsland_size(island_size + islandToMerge.getIsland_size());
        setMother_nature(mother_nature || islandToMerge.isMother_nature());
        setNo_entry(no_entry || islandToMerge.getNo_entry());
        students.addAll(islandToMerge.getStudents());
        return true;
    }

    //Same function as above, but it gets called when the power of character 8 is active
    public void conquer(Player player, LinkedList<Player> players, int additionalInfluence) throws Exception {
        TowerColour old_team = tower;
        // if the player can conquer the island
        if(canConquer(player, players, additionalInfluence)){
            // change the colour of the towers
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
        else {
            throw  new Exception("Player can't conquer the island"); // TODO define better exception
        }
    }

    //Same function as above, but it gets called when the power of character 8 is active
    public boolean canConquer(Player player, LinkedList<Player> players, int additionalInfluence) {
        // get the influence of the conquering player
        int player_influence = influence(player) + additionalInfluence;
        for(Player p: players){
            // if any of the other players has greater or equal influence
            if((p!=player)&&(influence(p)>=player_influence))
                // return false: the player can't conquer the island
                return false;
        }
        // if no one has greater or equal influence the player can conquer the island
        return true;
    }

    //Same function as above, but it gets called when the power of character 9 is active
    public void conquer(Player player, LinkedList<Player> players, Colour colourToExclude) throws Exception {
        TowerColour old_team = tower;
        // if the player can conquer the island
        if(canConquer(player, players, colourToExclude)){
            // change the colour of the towers
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
        else {
            throw  new Exception("Player can't conquer the island"); // TODO define better exception
        }
    }

    //Same function as above, but it gets called when the power of character 9 is active
    public boolean canConquer(Player player, LinkedList<Player> players, Colour colour) {
        // get the influence of the conquering player
        int player_influence = influence(player, colour);
        for(Player p: players){
            // if any of the other players has greater or equal influence
            if((p!=player)&&(influence(p)>=player_influence))
                // return false: the player can't conquer the island
                return false;
        }
        // if no one has greater or equal influence the player can conquer the island
        return true;
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
}
