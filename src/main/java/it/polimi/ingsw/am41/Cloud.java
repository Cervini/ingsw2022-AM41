package it.polimi.ingsw.am41;

import java.util.ArrayList;
import java.util.List;


public class Cloud implements Tile {

    private List<Student> students;

    public Cloud() {
        students = new ArrayList<Student>();
    }

    @Override
    public void putStudent(Student student) throws Exception {
        if(student != null){
            if(students.size()<3)
                students.add(student);
            else {
                throw new Exception("Cloud is already full"); // TODO improve exception
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
}
