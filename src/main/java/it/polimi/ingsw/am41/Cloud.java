package it.polimi.ingsw.am41;

import java.util.ArrayList;
import java.util.List;


public class Cloud implements Tile {

    private Cloud cloud;
    private List<Student> students = new ArrayList<Student>();

    private int count=0;

    public Cloud cloud() {
        if(count<4){
            cloud = new Cloud();
            count++;
            return cloud;
        }
        else{
            return null;
        }


    }

    @Override
    public void PutStudent(Student student) {
        students.add(student);
    }

    @Override
    public void RemoveStudent(Student Student) {
        students.remove(Student);

    }
}
