package it.polimi.ingsw.am41;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Island implements Tile {

    private Island island;
    private int size_island=1;
    private boolean is_mother_nature;
    private boolean is_deny_card;
    private List <Student> students = new ArrayList<>();
    private Tower_Colour tower = null;
    private List<Long> player_influence = new ArrayList<Long>();
    private Colour influence_colour;

    public void setTower(Tower_Colour tower) {
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
    public void PutStudent(Student Student) {
        students.add(Student);
    }

    @Override
    public void RemoveStudent(Student Student) {
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

        for (Professor p: player.owned_professors){

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

        if (player.getTower_colour() == tower){
            player_influence++;
        }


        player.setInfluence_player(player_influence);
        return player.getInfluence_player();


    }

    public Player canConquer(Player player1, Player player2, Player player3, Player player4) {

        ArrayList<Player> players_influence = new ArrayList<Player>(Arrays.asList(player1, player2, player3, player4));
        int max_influence =0;
        Player chosen_player = new Player();

        for(Player p: players_influence){

            if (p.getInfluence_player()>max_influence) {
                max_influence = p.getInfluence_player();
                chosen_player= p;

            }
        }

        int count=0;

        for(Player p: players_influence){ //influence tied
            while(count<2){
                if (p.getInfluence_player()==max_influence){
                    count++;
                }
            }

        }
        if (count>=2){
            return null;
        }

        return chosen_player;

    }

}
