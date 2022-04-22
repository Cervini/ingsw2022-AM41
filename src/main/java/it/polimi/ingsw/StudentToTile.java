package it.polimi.ingsw;

import java.util.ArrayList;

public class StudentToTile extends CharacterDecorator{

    public StudentToTile(SimpleCharacter decoratedCharacter) {
        super(decoratedCharacter);
    }

    @Override
    public void effect(ArrayList<Object> args) {
        super.effect(args);
        // parsing of arguments
        Tile destination = null;
        Student student = null;
        try {
            student = (Student) args.get(0);
            destination = (Tile) args.get(1);
        } catch (IndexOutOfBoundsException e){
            System.out.println("Invalid arguments!");
            e.printStackTrace();
        }
        getDecoratedCharacter().getGame().moveStudent(getDecoratedCharacter(), destination, student);
        getDecoratedCharacter().reFill();
    }
}
