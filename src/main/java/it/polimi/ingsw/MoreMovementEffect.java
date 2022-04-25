package it.polimi.ingsw;

import java.util.ArrayList;

public class MoreMovementEffect extends CharacterDecorator{

    public MoreMovementEffect(SimpleCharacter decoratedCharacter) {
        super(decoratedCharacter);
    }

    @Override
    public void effect(ArrayList<Object> args) {
        super.effect(args);
         // parsing arguments
        Player user = (Player) args.get(0);

        int x = user.getFace_up_assistant().getMovement_points();
        user.getFace_up_assistant().setMovement_points(x+2);

        args.remove(0);
    }

    @Override
    public void endEffect(){
        super.endEffect();
    }
}
