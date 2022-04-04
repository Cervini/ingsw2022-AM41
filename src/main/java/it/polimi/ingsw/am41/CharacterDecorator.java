package it.polimi.ingsw.am41;

public abstract class CharacterDecorator implements Character{
    private final Character decoratedCharacter;

    public CharacterDecorator(Character decoratedCharacter){
        this.decoratedCharacter = decoratedCharacter;
    }


}
