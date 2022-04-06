package it.polimi.ingsw.am41;

import java.util.ArrayList;

public class SchoolBoard implements Tile{
    private ArrayList<Student> entrance; // list of students at the entrance section of the School_board
    private ArrayList<DiningRoom> dining_rooms; // list of all the Dining_rooms associated with this School_board
    private int towers; // number of towers currently on the School_board

    // default constructor sets up School_board with 8 towers
    public SchoolBoard(){
        this.entrance = new ArrayList<Student>(9);
        this.dining_rooms = new ArrayList<DiningRoom>(5);
        /* initialize a dining_room for every color */
        for(Colour colour : Colour.values()){
            dining_rooms.add(new DiningRoom(colour));
        }
        this.towers = 8;
    }

    /**
     *  alternative constructor sets up School_board with chosen number of towers
     *  @requires towers >= 0
     */
    public SchoolBoard(int towers) {
        this.entrance = new ArrayList<Student>(9);
        this.dining_rooms = new ArrayList<DiningRoom>(5);
        /* initialize a dining_room for every color */
        for(Colour colour : Colour.values()){
            dining_rooms.add(new DiningRoom(colour));
        }
        this.towers = towers;
    }

    public int getEntranceSize(){
        return entrance.size();
    }

    public int getTowers() {
        return towers;
    }

    public void setTowers(int towers) {
        this.towers = towers;
    }

    public ArrayList<DiningRoom> getDining_rooms() {
        return dining_rooms;
    }

    /**
     * add a student to entrance
     * @signals (Exception e) (entrance.size()>=9)
     */

    public void putStudent(Student student) throws Exception {
        if(entrance.size()<9)
            entrance.add(student);
        else{
            throw new Exception("Entrance board is already full"); // TODO define a better exception
        }
    }

    public void removeStudent(Student student){
        entrance.remove(student);
    }
}