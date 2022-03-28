package it.polimi.ingsw.am41;
// TODO comment

import java.lang.reflect.Array;
import java.security.DigestInputStream;
import java.util.ArrayList;

public class School_board implements Tile{
    private ArrayList<Student> entrance;
    private ArrayList<Dining_room> dining_rooms;
    private int towers;

    public School_board(int towers) {
        this.entrance = new ArrayList<Student>(9);
        this.dining_rooms = new ArrayList<Dining_room>(5);
        /* initialize a dining_room for every color */
        for(Color color : Color.values()){
            dining_rooms.add(new Dining_room(color));
        }
        this.towers = towers;
    }

    public School_board(){
        this.entrance = new ArrayList<Student>(9);
        this.dining_rooms = new ArrayList<Dining_room>(5);
        /* initialize a dining_room for every color */
        for(Color color : Color.values()){
            dining_rooms.add(new Dining_room(color));
        }
        this.towers = 8;
    }

    public int getTowers() {
        return towers;
    }

    public void setTowers(int towers) {
        this.towers = towers;
    }

    public ArrayList<Dining_room> getDining_rooms() {
        return dining_rooms;
    }

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
