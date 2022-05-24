package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cloud implements Tile, Serializable {

    private final List<Student> students;
    private final int maxStudents;

    public Cloud() {
        students = new ArrayList<>();
        this.maxStudents = 3;
    }

    public Cloud(int maxStudents){
        students = new ArrayList<>();
        this.maxStudents = maxStudents;
    }

    @Override
    public void putStudent(Student student) throws Exception {
        if (student != null) {
            if (students.size() < maxStudents)
                students.add(student);
            else {
                throw new Exception("Cloud is already full!");
            }
        }
    }

    @Override
    public void removeStudent(Student student) {
        students.remove(student);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void emptyIsland(){
        students.clear();
    }

    public int getMaxStudents() {
        return maxStudents;
    }
}