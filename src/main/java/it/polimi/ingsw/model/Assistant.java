package it.polimi.ingsw.model;

import java.io.Serializable;

public class Assistant implements Comparable<Assistant>, Serializable {
    private final int value; //determines card's value used for turn order
    private int movement_points; //determines movements Mother Nature may perform with the use of this card

    /**
     *  @requirements (value >= 1) && (movement_points >= 1)
     */
    public Assistant(int value, int movement_points) {
        this.value = value;
        this.movement_points = movement_points;
    }

    public int getValue() {
        return value;
    }

    public int getMovement_points() {
        return movement_points;
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
        return (a.getMovement_points() == this.movement_points) && (a.getValue() == this.value);
    }

    public boolean equals(Assistant o){
        return (o.getMovement_points() == this.movement_points) && (o.getValue() == this.value);
    }

    //Function to increase the max movement points by two when character 4 is played
    public void add2MovementPoints(){
        movement_points = movement_points + 2;
    }
}