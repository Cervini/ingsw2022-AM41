package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;


public class Cloud implements Tile {

    private List<Student> students;

    public Cloud() {
        students = new ArrayList<Student>();
    }

    @Override
    public void putStudent(Student student) throws Exception {
        if (student != null) {
            if (students.size() < 3)
                students.add(student);
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
}