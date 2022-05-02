package it.polimi.ingsw.model;

public class Student {
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
