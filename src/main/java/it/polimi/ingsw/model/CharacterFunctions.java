package it.polimi.ingsw.model;

import java.util.LinkedList;
import java.util.List;

public class CharacterFunctions {

    private static final int additionalInfluence = 2;
    private static final int noEntryCharacterNumber = 4;

    //Function of character 3
    public Game checkInfluenceOnSpecificIsland(Game game, Island island) throws Exception{
        int islandIndex;
        islandIndex = game.getArchipelago().indexOf(island);
        if(!game.getArchipelago().get(islandIndex).getNo_entry()){
            game.getArchipelago().get(islandIndex).conquerCheck(game.getPlayers());
        } else {
            game.getArchipelago().get(islandIndex).setNo_entry(false);
            game.getCharacters().get(findNoEntryCharacter(game.getCharacters())).returnNoEntry();
        }
        return game;
    }

    //Function of character 2
    //TODO fix
    public Game checkInfluenceWithModifiedBoard(Game game, Player player) throws Exception{
        LinkedList<Player> originalPlayers = new LinkedList<>();
        int islandIndex;
        originalPlayers.addAll(game.getPlayers());
        game = changeOwnership(game, player);
        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        if(!game.getArchipelago().get(islandIndex).getNo_entry()){
            game.getArchipelago().get(islandIndex).conquerCheck(game.getPlayers());
        } else {
            game.getArchipelago().get(islandIndex).setNo_entry(false);
            game.getCharacters().get(findNoEntryCharacter(game.getCharacters())).returnNoEntry();
        }
        for(Player playerToCheck: originalPlayers){
            playerToCheck.getSchool().resetTowers(game.getPlayers().get(game.getPlayers().indexOf(playerToCheck)).getSchool().getTowers());
        }
        game.setPlayers(originalPlayers);
        return game;
    }

    //Function of character 6
    //TODO fix
    public Game checkInfluenceWithoutTowers(Game game) throws Exception{
        int islandIndex;
        List<Island> originalArchipelago = game.getArchipelago();
        for(Island islandToCheck: game.getArchipelago()){
            islandToCheck.setIsland_size(0);
        }
        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        if(!game.getArchipelago().get(islandIndex).getNo_entry()){
            game.getArchipelago().get(islandIndex).conquerCheck(game.getPlayers());
        } else {
            game.getArchipelago().get(islandIndex).setNo_entry(false);
            game.getCharacters().get(findNoEntryCharacter(game.getCharacters())).returnNoEntry();
        }
        game.setArchipelago(originalArchipelago);
        return game;
    }

    //Function of character 8
    public Game checkInfluenceWithBonus(Game game, Player player) throws Exception{
        int islandIndex;
        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        if(!game.getArchipelago().get(islandIndex).getNo_entry()){
            game.getArchipelago().get(islandIndex).conquerCheck(game.getPlayers(), player, additionalInfluence);
        } else {
            game.getArchipelago().get(islandIndex).setNo_entry(false);
            game.getCharacters().get(findNoEntryCharacter(game.getCharacters())).returnNoEntry();
        }
        return game;
    }

    //Function of character 9
    public Game checkInfluenceWithoutColour(Game game, Colour colourToExclude) throws Exception{
        int islandIndex;
        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        if(!game.getArchipelago().get(islandIndex).getNo_entry()){
            game.getArchipelago().get(islandIndex).conquerCheck(game.getPlayers(), colourToExclude);
        } else {
            game.getArchipelago().get(islandIndex).setNo_entry(false);
            game.getCharacters().get(findNoEntryCharacter(game.getCharacters())).returnNoEntry();
        }
        return game;
    }

    //Used by checkInfluenceWithModifiedBoard to edit the SchoolBoards as Character 2 requires
    private Game changeOwnership(Game game, Player cardPlayer){
        int cardPlayerIndex;
        Professor professorToRemove;
        cardPlayerIndex = game.getPlayers().indexOf(cardPlayer);
        for(Player playerToCheck: game.getPlayers()){
            if(!playerToCheck.equals(cardPlayer)){
                for(Colour colour: Colour.values()){
                    if(playerToCheck.hasProfessor(colour) && !game.getPlayers().get(cardPlayerIndex).hasProfessor(colour) && playerToCheck.getSchool().getDining_room(colour).getStudents() <= cardPlayer.getSchool().getDining_room(colour).getStudents()){
                        professorToRemove = playerToCheck.getSchool().takeProfessor(colour);
                        game.getPlayers().get(cardPlayerIndex).addProfessor(professorToRemove);
                    }
                }
            }
        }
        return game;
    }

    //Used to find the character that holds the "NoEntry" cards, it works only if there's one
    private int findNoEntryCharacter(LinkedList<Character> characters){
        for(Character characterToCheck: characters){
            if(characterToCheck.getCharacterNumber() == noEntryCharacterNumber){
                return characters.indexOf(characterToCheck);
            }
        }
        return -1;
    }
}
