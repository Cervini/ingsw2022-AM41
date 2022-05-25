package it.polimi.ingsw.model;

import java.io.Serializable;

public interface Tile extends Serializable {

    void putStudent(Student student) throws Exception;

    void removeStudent(Student student) throws Exception;
}
