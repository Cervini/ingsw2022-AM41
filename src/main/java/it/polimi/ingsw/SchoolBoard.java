package it.polimi.ingsw;

import java.util.ArrayList;

public class SchoolBoard implements Tile{
    private ArrayList<Student> entrance; // list of students at the entrance section of the School_board
    private ArrayList<DiningRoom> dining_rooms; // list of all the Dining_rooms associated with this School_board
    private int towers; // number of towers currently on the School_board

    /**
     * default constructor sets up School_board with 8 towers
     */
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

    /**
     * removes towers from school board
     * @param towers number of towers to be removed
     * @throws Exception thrown when tha players has fewer towers than 'towers' param
     */
    public void takeTowers(int towers) throws Exception {
        if(this.towers >= towers)
            this.towers -= towers;
        else {
            throw new Exception("Player has not enough towers"); // TODO define better exception
        }
    }

    /**
     * adds tower to the school board
     * @param towers number of towers to be added
     */
    public void giveTowers(int towers){
        this.towers += towers;
    }

    public ArrayList<DiningRoom> getDining_rooms() {
        return dining_rooms;
    }

    /**
     * @param colour color of dining room
     * @return dining room of the input color
     */
    public DiningRoom getDining_room(Colour colour){
        switch(colour){
            case BLUE:{
                return dining_rooms.get(0);
            }
            case RED:{
                return dining_rooms.get(1);
            }
            case GREEN:{
                return dining_rooms.get(2);
            }
            case YELLOW:{
                return dining_rooms.get(3);
            }
            case PINK:{
                return dining_rooms.get(4);
            }
            default: return null;
        }
    }

    /**
     * add a student to entrance
     * @signals (Exception e) (entrance.size()>=9)
     */
    @Override
    public void putStudent(Student student) throws Exception {
        if(entrance.size()<9)
            entrance.add(student);
        else{
            throw new Exception("Entrance board is already full"); // TODO define a better exception
        }
    }

    @Override
    public void removeStudent(Student student) {
        entrance.remove(student);
    }
}