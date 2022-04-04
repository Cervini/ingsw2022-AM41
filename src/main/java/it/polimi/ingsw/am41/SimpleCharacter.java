package it.polimi.ingsw.am41;

public class SimpleCharacter implements Character, ByCharacter, Tile{
    private int price;
    private boolean is_increased;

    private void increaseValue(){
        if(is_increased == false){
            price++;
        }
    }

    @Override
    public void effect() {

    }
}
