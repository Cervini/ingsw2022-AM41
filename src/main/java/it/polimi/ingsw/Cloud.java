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
        int cloudSize;
        if(student != null){
            try{
                students.add(student);
            }catch (Exception e){
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

    public void emptyIsland(){
        students.clear();
        return;
    }
}
