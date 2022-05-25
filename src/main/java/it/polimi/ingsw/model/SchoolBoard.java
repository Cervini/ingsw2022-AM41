package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class SchoolBoard implements Tile, Serializable {

    private LinkedList<Student> entrance; // list of students at the entrance section of the School_board
    private ArrayList<DiningRoom> dining_rooms; // list of all the Dining_rooms associated with this School_board
    private int towers; // number of towers currently on the School_board
    private String owner;
    private TowerColour team;
    private Assistant face_up_assistant;
    private List<Professor> owned_professor; // list of all the currently owned professors
    private final int entrance_max;
    /**
     * default constructor sets up School_board with 8 towers
     */
    public SchoolBoard(Player owner){
        this.entrance = new LinkedList<>();
        this.dining_rooms = new ArrayList<>(5);
        this.owned_professor = new ArrayList<>();
        /* initialize a dining_room for every color */
        for(Colour colour : Colour.values()){
            dining_rooms.add(new DiningRoom(colour));
        }
        this.towers = 8;
        this.owner = owner.getPlayer_id();
        this.team = owner.getTeam();
        this.face_up_assistant = null;

        this.entrance_max = 7;
    }

    /**
     *  alternative constructor sets up School_board with chosen number of towers
     *  @requires towers >= 0
     */
    public SchoolBoard(int towers, Player owner, int entrance_max) {
        this.entrance = new LinkedList<>();
        this.owned_professor = new ArrayList<>();
        this.dining_rooms = new ArrayList<>(5);
        /* initialize a dining_room for every color */
        for(Colour colour : Colour.values()){
            dining_rooms.add(new DiningRoom(colour));
        }
        this.towers = towers;
        this.owner = owner.getPlayer_id();
        this.team = owner.getTeam();
        this.face_up_assistant = null;
        this.entrance_max = entrance_max;
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
     */
    @Override
    public void putStudent(Student student) {
        if(entrance.size()<entrance_max)
            entrance.add(student);
        //else throw new Exception("Entrance is full");
    }

    @Override
    public void removeStudent (Student student) {
        entrance.remove(student);
    }

    public void resetTowers(int newTowerNumber) {towers = newTowerNumber;}

    public LinkedList<Student> getEntrance() {
        return entrance;
    }

    public String getOwner() {
        return owner;
    }

    public TowerColour getTeam() {
        return team;
    }

    public Assistant getFace_up_assistant() {
        return face_up_assistant;
    }

    public void setFace_up_assistant(Assistant face_up_assistant) {
        this.face_up_assistant = face_up_assistant;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void removeStudents(LinkedList<Student> studentsToRemove) {
        entrance.removeAll(studentsToRemove);
    }

    public void putStudents(LinkedList<Student> studentsToAdd) {
        entrance.addAll(studentsToAdd);
    }

    public List<Professor> getOwned_professor() {
        return owned_professor;
    }

    public void setOwned_professor(List<Professor> owned_professor) {
        this.owned_professor = owned_professor;
    }

    public void setOwned_professor(LinkedList<Professor> owned_professor) {
        this.owned_professor = owned_professor;
    }
}