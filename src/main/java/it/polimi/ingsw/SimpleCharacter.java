package it.polimi.ingsw;

import java.util.ArrayList;

public class SimpleCharacter implements Character, Tile{

    private ArrayList<Student> students;
    private int no_entry;
    private Game game;
    boolean increased;
    int space;
    int price;

    public SimpleCharacter(Game game, int price, int space, int no_entry){
        this.game = game;
        this.no_entry = no_entry;
        this.price = price;
        this.increased = false;
        this.space = space;
        students = new ArrayList<>();
        for(int i=0; i<space; i++){
            students.add(game.drawStudent());
        }
    }

    @Override
    public void effect(ArrayList<Object> args) {
        if(!increased){
            price++;
        }
    }

    @Override
    public void endEffect() {
    }

    @Override
    public void putStudent(Student student) throws Exception {
        if(student != null)
            students.add(student);
    }

    @Override
    public void removeStudent(Student student) throws Exception {
        students.remove(student);
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public int getNo_entry() {
        return no_entry;
    }

    public void setNo_entry(int no_entry) {
        this.no_entry = no_entry;
    }

    public Game getGame() {
        return game;
    }

    public int getPrice(){
        return price;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void reFill(){
        while(students.size()<space){
            students.add(game.drawStudent());
        }
    }
}
