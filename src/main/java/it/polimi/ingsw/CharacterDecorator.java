package it.polimi.ingsw;

import java.util.ArrayList;

public abstract class CharacterDecorator implements Character{
    private final SimpleCharacter decoratedCharacter;

    public CharacterDecorator(SimpleCharacter decoratedCharacter) {
        this.decoratedCharacter = decoratedCharacter;
    }

    @Override
    public void effect(ArrayList<Object> args) {

    }

    @Override
    public void endEffect(){

    }

    public SimpleCharacter getDecoratedCharacter() {
        return decoratedCharacter;
    }
}
