package it.polimi.ingsw.am41;

public abstract class CharacterDecorator implements Character{

    protected Character decoratedCharacter;

    public CharacterDecorator(Character decoratedCharacter){
        this.decoratedCharacter = decoratedCharacter;
    }

    @Override
    public void effect() {
        decoratedCharacter.effect();
    }
}
