package it.polimi.ingsw;

import java.util.ArrayList;

public class TakeStudentEffect extends CharacterDecorator{

    public TakeStudentEffect(SimpleCharacter decoratedCharacter) {
        super(decoratedCharacter);
    }

    @Override
    public void effect(ArrayList<Object> args) {
        super.effect(args);

        // parsing
        Player player = null;
        Student student = null;
        try {
            player = (Player) args.get(0);
            student = (Student) args.get(1);
        } catch (IndexOutOfBoundsException e){
            System.out.println("Invalid arguments!");
        }
        getDecoratedCharacter().getGame().moveStudent(player.getSchool(), getDecoratedCharacter(), student );

        args.remove(0);
        args.remove(1);
    }

    @Override
    public void endEffect() {
        super.endEffect();
    }
}
