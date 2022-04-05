package it.polimi.ingsw.am41;

import java.util.ArrayList;
import java.util.List;

public class Player {

    Player player;

    int player_id;
    private Tower_Colour tower_colour;
    private int influence_player = 0;


    public Tower_Colour getTower_colour() {
        return tower_colour;
    }

    public void setInfluence_player(int influence_player) {
        this.influence_player = influence_player;
    }

    public int getInfluence_player() {
        return influence_player;
    }





    public int getPlayer_id() {
        return player_id;
    }

    public List<Professor> owned_professors = new ArrayList<Professor>();

}
