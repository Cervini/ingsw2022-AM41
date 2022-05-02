package it.polimi.ingsw.model;

public class Professor{

    private final Colour colour;

    public Professor(Colour colour){
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }

    @Override
    public boolean equals(Object o) {
        Professor p = (Professor) o;
        return p.getColour() == this.colour;
    }
}
