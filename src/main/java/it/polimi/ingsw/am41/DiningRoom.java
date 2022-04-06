package it.polimi.ingsw.am41;

import java.util.Objects;

public class DiningRoom{
    private final Colour colour; // color of the students stored in the Dining_room
    private int students; // number of students currently in the Dining_room
    private int given_coins; // number of coins already given to the player by this Dining_room

    public DiningRoom(Colour colour) {
        this.colour = colour;
        this.students = 0;
        this.given_coins = 0;
    }

    public Colour getColour() {
        return colour;
    }

    public int getStudents() {
        return students;
    }

    public int getGiven_coins() {
        return given_coins;
    }

    /**
     * increases by one the value of given_coins
     */
    public void giveCoin(){
        given_coins++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiningRoom that)) return false;
        return getStudents() == that.getStudents() && getColour() == that.getColour();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColour());
    }

    /**
     * add one student to the DiningRoom
     * @requires (student != null) && (student.getColor() == this.color)
     */
    public void putStudent(Student student) throws Exception {
        if(student.getColour() == this.colour)
            this.students++;
        else {
            throw new Exception("Student must be put in the dining room of its color"); //TODO define better exception
        }
    }

     /** remove one student from the DiningRoom
      *  returning a Student instance with the color of the DiningRoom
      * @ensures (student.color == this.color)
     */
     public Student removeStudent(){
        this.students--;
        Student student = new Student(this.colour);
        return student;
    }
}
