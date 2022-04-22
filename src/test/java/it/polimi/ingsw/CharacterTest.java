package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class CharacterTest {

    @Test
    public void Character1Test(){
        Game game = new Game(2);
        Player user = game.getPlayers().getFirst();
        Island island = game.getArchipelago().get(3);

        // building character 1
        SimpleCharacter character = new SimpleCharacter(game, 1,4,0);
        Character firstCharacter = new StudentToTile(character);

        Student student = character.getStudents().get(0);

        // using character 1

        //set up arguments
        ArrayList<Object> args = new ArrayList<>();
        args.add(0, student);
        args.add(1, island);
        if(user.getCoins()>=character.getPrice()){
            firstCharacter.effect(args);
            try {
                user.spend(character.getPrice());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        assertEquals(island.getStudents().size(), 2);
        assertEquals(character.getStudents().size(), 4);
    }
}
