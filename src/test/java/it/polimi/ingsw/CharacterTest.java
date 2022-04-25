package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class CharacterTest {

    @Test
    public void Character1SimpleTest(){
        Game game = new Game(2);
        Player user = game.getPlayers().getFirst();
        Island island = game.getArchipelago().get(3);

        // building character 1
        SimpleCharacter character = new SimpleCharacter(game, 1,4,0);
        Character firstCharacter = new MoveStudentEffect(character);

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

    @Test
    public void Character2SimpleTest(){
        Game game = new Game(2);
        Player user = game.getPlayers().getFirst();

        // building character 2
        SimpleCharacter character = new SimpleCharacter(game, 2,0,0);
        Character secondCharacter = new IgnoreTiedEffect(character);

        // set 2 students in each dining room to every player
        for(Colour color: Colour.values()){
            Student student = new Student(color);
            for(Player player: game.getPlayers()){
                for(int i=0; i<2; i++){
                    try {
                        player.getSchool().getDining_room(color).putStudent(student);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // using character 2

        // set up arguments
        ArrayList<Object> args = new ArrayList<>();
        args.add(0, user);

        secondCharacter.effect(args);

        // user should have all the professor despite the ties
        assertEquals(user.getOwned_professor().size(), 5);
    }

    @Test
    public void Character3SimpleTest(){
        Game game = new Game(2);

        Island island = game.getArchipelago().get(5);

        // give professors to the players

        game.getPlayers().get(0).getOwned_professor().add(new Professor(Colour.GREEN));
        game.getPlayers().get(0).getOwned_professor().add(new Professor(Colour.RED));
        game.getPlayers().get(0).getOwned_professor().add(new Professor(Colour.PINK));
        game.getPlayers().get(1).getOwned_professor().add(new Professor(Colour.BLUE));
        game.getPlayers().get(1).getOwned_professor().add(new Professor(Colour.YELLOW));

        for(int i=0; i<2; i++)
            island.putStudent(new Student(Colour.RED));
        island.putStudent(new Student(Colour.GREEN));
        island.putStudent(new Student(Colour.PINK));

        for(int i=0; i<2; i++)
            island.putStudent(new Student(Colour.BLUE));

        island.setTower(TowerColour.BLACK);

        // if influence is calculated island owner should change

        // building character 3
        SimpleCharacter character = new SimpleCharacter(game, 3,0,0);
        Character thirdCharacter = new MotherNatureEffect(character);

        // using character 3

        // set up arguments
        ArrayList<Object> args = new ArrayList<>();

        args.add(island);

        thirdCharacter.effect(args);

        assertEquals(island.getTower(), TowerColour.WHITE);
    }

    @Test
    public void Character4SimpleTest(){
        Game game = new Game(2);

        Player player = game.getPlayers().getFirst();

        try {
            player.playAssistant(player.getAssistants().get(6));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            game.moveMotherNature(5, player);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // building character 3
        SimpleCharacter character = new SimpleCharacter(game, 1,0,0);
        Character fourthCharacter = new MoreMovementEffect(character);

        // using character 3

        // set up arguments
        ArrayList<Object> args = new ArrayList<>();

        args.add(player);

        fourthCharacter.effect(args);

        try {
            game.moveMotherNature(5, player);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(game.getArchipelago().get(5).isMother_nature());
    }

    @Test
    public void Character5SimpleTest(){
        Game game = new Game(2);
        Player user = game.getPlayers().getFirst();

        // building character 2
        SimpleCharacter character = new SimpleCharacter(game, 2,0,0);
        Character fifthCharacter = new IgnoreTowersEffect(character);

        fifthCharacter.effect(new ArrayList<>());

        int count = 0;
        for(Island island: game.getArchipelago()){
            count += island.getIsland_size();
        }

        assertEquals(count, 0);

        fifthCharacter.endEffect();

        count = 0;
        for(Island island: game.getArchipelago()){
            count += island.getIsland_size();
        }

        assertEquals(count, 12);
    }

}
