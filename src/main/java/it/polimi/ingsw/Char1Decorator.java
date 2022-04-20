package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Char1Decorator extends CharacterDecorator{

    private List<Student> students;

    public Char1Decorator(Character decoratedCharacter, LinkedList<Student> bag) {
        super(decoratedCharacter);
        students = new ArrayList<Student>();
        for(int i=0; i<4; i++){
            students.add(bag.getFirst());
            bag.removeFirst();
        }
    }

    public void effect(Student student, Island island, LinkedList<Student> bag){
        decoratedCharacter.effect();
        try {
            moveToIsland(student, island);
            students.add(bag.getFirst());
            bag.removeFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveToIsland(Student student, Island island) throws Exception {
        if(students.contains(student)){
            students.remove(student);
            island.putStudent(student);
        }
        else {
            throw new Exception("Student not existing");
        }
    }

    public List<Student> getStudents() {
        return students;
    }
}
