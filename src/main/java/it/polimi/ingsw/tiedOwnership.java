package it.polimi.ingsw;

import java.util.ArrayList;

public class tiedOwnership extends CharacterDecorator{

    public tiedOwnership(SimpleCharacter decoratedCharacter) {
        super(decoratedCharacter);
    }

    @Override
    public void effect(ArrayList<Object> args) {
        super.effect(args);
        // parsing
    }
}
