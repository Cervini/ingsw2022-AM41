package it.polimi.ingsw.am41;

public class Assistant {
    private final int value; //determines card's value used for turn order
    private final int movement_points; //determines movements Mother Nature may perform with the use of this card
    private final Player player;

    /* @requires (value >= 1) && (movement_points >= 1) */
    public Assistant(int value, int movement_points, Player player) {
        this.value = value;
        this.movement_points = movement_points;
        this.player = player;
    }

    public int getValue() {
        return value;
    }

    public int getMovement_points() {
        return movement_points;
    }
}
