package it.polimi.ingsw.model;

import java.io.Serializable;

public class Student implements Serializable {
    private final Colour colour;

    public Student(Colour colour){
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        Student s = (Student) o;
        return s.getColour() == this.colour;
    }

}
