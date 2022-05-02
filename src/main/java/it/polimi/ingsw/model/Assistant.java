package it.polimi.ingsw.model;

public class Assistant implements Comparable<Assistant>{
    private final int value; //determines card's value used for turn order
    private final int movement_points; //determines movements Mother Nature may perform with the use of this card
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
     * @return  if(this.getValue() > that.getValue())
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

    @Override
    public boolean equals(Object o){
        Assistant a = (Assistant) o;
        if((a.getMovement_points()==this.movement_points)&&(a.getValue()==this.value))
            return true;
        else
            return false;
    }
}