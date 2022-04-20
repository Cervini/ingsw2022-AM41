package it.polimi.ingsw;

public class SimpleCharacter implements Character{

    private int price;
    private boolean is_increased;

    private void increaseValue(){
        if(is_increased == false){
            price++;
        }
    }

    @Override
    public void effect() {
        increaseValue();
    }
}
