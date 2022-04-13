package it.polimi.ingsw.am41;

public class DiningRoom implements Tile, Comparable{
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
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof DiningRoom that)) return false;
        return getStudents() == that.getStudents() && getColour() == that.getColour();
    }

    /**
     * add one student to the DiningRoom
     * @requires (student != null) && (student.getColor() == this.color)
     */
    public void putStudent(Student student) throws Exception {
        if (student.getColour() != this.colour) {
            throw new Exception("Student must be put in the dining room of its color"); // TODO define better exception
        } else {
            if (this.students < 10) {
                this.students++;
                if (this.students % 3 == 0) {
                    given_coins = students / 3;
                }
            } else {
                throw new Exception("Dining room is already full!"); // TODO better exception
            }
        }
    }

    @Override
    public void removeStudent(Student student) {
        if(student.getColour()==this.colour){
            this.students--;
        }
    }

    @Override
    public int compareTo(Object o) {
        DiningRoom d = (DiningRoom) o;
        return colour.compareTo(d.getColour());
    }
}