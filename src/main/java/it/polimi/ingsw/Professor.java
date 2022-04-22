package it.polimi.ingsw;

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
        if(p.getColour()==this.colour)
            return true;
        return false;
    }
}