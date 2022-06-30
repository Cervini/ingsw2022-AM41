package it.polimi.ingsw.model;

import java.io.Serializable;

public class DiningRoom implements Tile, Comparable, Serializable {
    private final Colour colour; // color of the students stored in the Dining_room
    private int students; // number of students currently in the Dining_room
    private int given_coins; // number of coins already given to the player by this Dining_room
    private SchoolBoard schoolBoard; // schoolBoard that includes this Dining Room

    public DiningRoom(Colour colour, SchoolBoard schoolBoard) {
        this.colour = colour;
        this.students = 0;
        this.given_coins = 0;
        this.schoolBoard = schoolBoard;
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

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof DiningRoom that)) return false;
        return getStudents() == that.getStudents() && getColour() == that.getColour();
    }

    /**
     * add one student to the DiningRoom
     * @throws Exception student.getColour()!=this.colour
     */
    public void putStudent(Student student) throws Exception {
        if (student.getColour() != this.colour) {
            throw new Exception("Student can't be placed in this dining room");
        } else {
            if (this.students < 10) {
                this.students++;
                if (this.students % 3 == 0) {
                    given_coins = students / 3;
                    schoolBoard.giveCoin();
                }
            } else {
                throw new Exception("Dining room is already full!");
            }
        }
    }

    /**Removes the student from this dining room
     * @param student student to remove
     * @throws Exception if the student to remove is the wrong colour or there are no students in this dining room
     */
    @Override
    public void removeStudent(Student student) throws Exception {
        if((student.getColour()==this.colour)&&(students>0)){
            this.students--;
        } else {
            throw new Exception("Impossible to remove student");
        }
    }

    @Override
    public int compareTo(Object o) {
        DiningRoom d = (DiningRoom) o;
        return colour.compareTo(d.getColour());
    }
}