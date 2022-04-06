package it.polimi.ingsw.am41;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Island implements Tile {

    private Island island;
    private int island_size =1;
    private boolean is_mother_nature;
    private boolean is_deny_card;
    private List <Student> students = new ArrayList<>();
    private TowerColour tower = null;
    private List<Long> player_influence = new ArrayList<Long>();
    private Colour influence_colour;

    public void setTower(TowerColour tower) {
        this.tower = tower;
    }

    int count=0;

    public Island island() { //instantiates only 12 islands
        if(count<12){
            island = new Island();
            count++;
            return island;
        }
        else {
            return null;
        }

    }

    @Override
    public void putStudent(Student Student) {
        students.add(Student);
    }

    @Override
    public void removeStudent(Student Student) {
        throw new UnsupportedOperationException();

    }



    private int influence(Player player) {

        if ( is_mother_nature==false ){
            return 0;
        }

        int player_influence=0;

        long blue = students.stream().filter( s -> s.getColour() == Colour.BLUE ).count();
        long red = students.stream().filter( s -> s.getColour() == Colour.RED ).count();
        long yellow = students.stream().filter( s -> s.getColour() == Colour.YELLOW ).count();
        long pink = students.stream().filter( s -> s.getColour() == Colour.PINK ).count();
        long green = students.stream().filter( s -> s.getColour() == Colour.GREEN ).count();

        for (Professor p: player.getOwned_professor()){

            switch(p.getColour()){
                case BLUE:
                    player_influence+=blue;
                case GREEN:
                    player_influence+=green;
                case PINK:
                    player_influence+=pink;
                case YELLOW:
                    player_influence+=yellow;
                case RED:
                    player_influence+=red;
            }
        }

        if (player.getTeam() == tower){
            player_influence++;
        }

        player.setCurrent_influence(player_influence);
        return player_influence;
    }

    public Player canConquer(Player player1, Player player2, Player player3, Player player4) {

        ArrayList<Player> players_influence = new ArrayList<Player>(Arrays.asList(player1, player2, player3, player4));
        int max_influence =0;
        Player chosen_player = null;

        for(Player p: players_influence){
            if (p.getCurrent_influence()>max_influence) {
                max_influence = p.getCurrent_influence();
                chosen_player= p;
            }
        }

        int count=0;

        for(Player p: players_influence){ //influence tied
            while(count<2){
                if (p.getCurrent_influence()==max_influence){
                    count++;
                }
            }

        }
        if (count>=2){
            return null;
        }

        return chosen_player;
    }

    public Island getIsland() {
        return island;
    }

    public void setIsland(Island island) {
        this.island = island;
    }

    public int getIsland_size() {
        return island_size;
    }

    public void setIsland_size(int island_size) {
        this.island_size = island_size;
    }

    public boolean getIs_mother_nature() {
        return is_mother_nature;
    }

    public void setIs_mother_nature(boolean is_mother_nature) {
        this.is_mother_nature = is_mother_nature;
    }

    public boolean getIs_deny_card() {
        return is_deny_card;
    }

    public void setIs_deny_card(boolean is_deny_card) {
        this.is_deny_card = is_deny_card;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public TowerColour getTower() {
        return tower;
    }

    public List<Long> getPlayer_influence() {
        return player_influence;
    }

    public void setPlayer_influence(List<Long> player_influence) {
        this.player_influence = player_influence;
    }

    public Colour getInfluence_colour() {
        return influence_colour;
    }

    public void setInfluence_colour(Colour influence_colour) {
        this.influence_colour = influence_colour;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
