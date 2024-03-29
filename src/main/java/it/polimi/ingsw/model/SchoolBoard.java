package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SchoolBoard implements Tile, Serializable {

    private final LinkedList<Student> entrance; // list of students at the entrance section of the School_board
    private final ArrayList<DiningRoom> dining_rooms; // list of all the Dining_rooms associated with this School_board
    private int towers; // number of towers currently on the School_board
    private String owner;
    private final TowerColour team;
    private Assistant face_up_assistant;
    private List<Professor> owned_professor; // list of all the currently owned professors
    private final int entrance_max;
    private int coins; // number of owned coins by the owner

    /**
     * default constructor sets up School_board with 8 towers
     */
    public SchoolBoard(Player owner){
        this.entrance = new LinkedList<>();
        this.dining_rooms = new ArrayList<>(5);
        this.owned_professor = new ArrayList<>();
        /* initialize a dining_room for every color */
        for(Colour colour : Colour.values()){
            dining_rooms.add(new DiningRoom(colour, this));
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
            dining_rooms.add(new DiningRoom(colour, this));
        }
        this.towers = towers;
        this.owner = owner.getPlayer_id();
        this.team = owner.getTeam();
        this.face_up_assistant = null;
        this.entrance_max = entrance_max;
    }

    /**Removes towers from school board
     * @param towers number of towers to be removed
     */
    public void takeTowers(int towers)  {
        if(this.towers >= towers)
            this.towers -= towers;
    }

    /**Adds tower to the school board
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

    /**Removes some students from the entrance
     * @param studentsToRemove list of students to be removed from the entrance
     */
    public void removeStudents(LinkedList<Student> studentsToRemove) {
        for(Student studentToRemove: studentsToRemove){
            entrance.remove(studentToRemove);
        }
    }

    /**Places some students in the entrance
     * @param studentsToAdd list of students to be added in the entrance
     */
    public void putStudents(LinkedList<Student> studentsToAdd) {
        entrance.addAll(studentsToAdd);
    }

    /**Removes one professor from this school board
     * @param colour colour of the professor to remove
     * @return the removed professor
     */
    public Professor takeProfessor(Colour colour) {
        for(Professor professorToCheck: owned_professor){
            if(professorToCheck.getColour().equals(colour)){
                owned_professor.remove(professorToCheck);
                return professorToCheck;
            }
        }
        return null;
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

    public void setOwned_professor(List<Professor> owned_professor) {
        this.owned_professor = owned_professor;
    }

    public List<Professor> getOwned_professor() {
        return owned_professor;
    }

    public void putProfessor(Professor professor){ this.owned_professor.add(professor); }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getEntranceSize(){
        return entrance.size();
    }

    public int getTowers() {
        return towers;
    }

    public void giveCoin(){ coins = coins + 1;}
}