package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;


public class Cloud implements Tile {

    private List<Student> students;
    private int maxStudents;

    public Cloud() {
        students = new ArrayList<Student>();
        this.maxStudents = 3;
    }

    public Cloud(int maxStudents){
        students = new ArrayList<Student>();
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
        return;
    }

    public int getMaxStudents() {
        return maxStudents;
    }
}