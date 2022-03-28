package it.polimi.ingsw.am41;
// TODO comment

import java.util.ArrayList;
import java.util.Objects;

public class Dining_room implements Tile{
    private Color color;
    private int students;
    private int given_coins;

    public Dining_room(Color color) {
        this.color = color;
        this.students = 0;
        this.given_coins = 0;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getStudents() {
        return students;
    }

    public void putStudent(int students) {
        this.students = students;
    }

    public int getGiven_coins() {
        return given_coins;
    }

    /**
     * giveCoin() increases by one the value of given_coins
     */
    public void giveCoin(){
        given_coins++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dining_room that)) return false;
        return getStudents() == that.getStudents() && getColor() == that.getColor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColor());
    }

    public void putStudent(){
        this.students++;
    }

    public void removeStudent(){
        this.students--;
    }
}
