package it.polimi.ingsw;

import java.util.ArrayList;

public class MotherNatureEffect extends CharacterDecorator{

    public MotherNatureEffect(SimpleCharacter decoratedCharacter) {
        super(decoratedCharacter);
    }

    @Override
    public void effect(ArrayList<Object> args) {
        super.effect(args);

        // parsing
        Island island = (Island) args.get(0);

        getDecoratedCharacter().getGame().islandCheck(island);

        args.remove(0);
    }

    @Override
    public void endEffect(){
        super.endEffect();
    }
}
