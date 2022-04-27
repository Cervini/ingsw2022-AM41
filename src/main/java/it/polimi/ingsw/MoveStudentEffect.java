package it.polimi.ingsw;

import java.util.ArrayList;

public class MoveStudentEffect extends CharacterDecorator{

    public MoveStudentEffect(SimpleCharacter decoratedCharacter) {
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
            // e.printStackTrace(); -- needed?
        }
        getDecoratedCharacter().getGame().moveStudent(getDecoratedCharacter(), destination, student);
        getDecoratedCharacter().reFill();
        // remove the two used arguments
        args.remove(1);
        args.remove(0);
    }
}
