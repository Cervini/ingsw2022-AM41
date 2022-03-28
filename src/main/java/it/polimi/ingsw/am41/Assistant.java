package it.polimi.ingsw.am41;
// TODO comment

public class Assistant {
    private final int value;
    private final int movement_points;

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
}
