package it.polimi.ingsw.am41;

import java.util.Objects;

public class Student {
    private Colour colour;

    public Student(Colour colour){
        this.colour = colour;
    }

    public void setColour() {
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
        if(s.getColour() == this.colour)
            return true;
        return false;
    }

}
