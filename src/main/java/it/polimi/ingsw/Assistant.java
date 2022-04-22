package it.polimi.ingsw;

public class Assistant implements Comparable<Assistant>{
    private final int value; //determines card's value used for turn order
    private int movement_points; //determines movements Mother Nature may perform with the use of this card
    private final Player player; // player who played the card

    /**
     *  @requires (value >= 1) && (movement_points >= 1)
     */
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

    public Player getPlayer(){
        return player;
    }

    /**
     * Override to set as determinant field 'value'
     * @ensures if(this.getValue() > that.getValue())
     *              return 1;
     *          else
     *              return 0;
     */
    @Override
    public int compareTo(Assistant a) {
        if(a.getValue() > this.value)
            return 0;
        else {
            return 1;
        }
    }

    public void setMovement_points(int movement_points) {
        this.movement_points = movement_points;
    }
}