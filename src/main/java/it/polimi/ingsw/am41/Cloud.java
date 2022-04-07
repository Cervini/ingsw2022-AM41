package it.polimi.ingsw.am41;

import java.util.ArrayList;
import java.util.List;


public class Cloud implements Tile {

    private List<Student> students;

    public Cloud() {
        students = new ArrayList<Student>();
    }

    @Override
    public void putStudent(Student student) {
        students.add(student);
    }

    @Override
    public void removeStudent(Student student) {
        students.remove(student);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
