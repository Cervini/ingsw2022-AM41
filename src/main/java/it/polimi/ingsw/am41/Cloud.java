package it.polimi.ingsw.am41;

import java.util.ArrayList;
import java.util.List;


public class Cloud implements Tile {

    private Cloud cloud;
    private List<Student> students = new ArrayList<Student>();

    private int count=0;

    public Cloud Cloud() {
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
    public void putStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student Student) {
        students.remove(Student);
    }

    public Cloud getCloud() {
        return cloud;
    }

    public void setCloud(Cloud cloud) {
        this.cloud = cloud;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
