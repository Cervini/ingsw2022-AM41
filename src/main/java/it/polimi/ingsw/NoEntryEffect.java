package it.polimi.ingsw;

import java.util.ArrayList;

public class NoEntryEffect extends CharacterDecorator{

    public NoEntryEffect(SimpleCharacter decoratedCharacter) {
        super(decoratedCharacter);
    }

    @Override
    public void effect(ArrayList<Object> args) {
        super.effect(args);
        // parsing
        Island island = (Island) args.get(0);

        if(!island.getNo_entry()){
            island.setNo_entry(true);
        }

        int x = getDecoratedCharacter().getNo_entry();
        getDecoratedCharacter().setNo_entry(x-1);
    }
}
