package it.polimi.ingsw.model;

import java.util.LinkedList;
import java.util.List;

public class CharacterFunctions {

    private static final int additionalInfluence = 2;
    private static final int noEntryCharacterNumber = 4;

    //Function of character 3
    public Game checkInfluenceOnSpecificIsland(Game game, Island island){
        int islandIndex;
        islandIndex = game.getArchipelago().indexOf(island);
        if(!game.getArchipelago().get(islandIndex).getNo_entry()){
            for(Player player: game.getPlayers()){
                try {
                    game.getArchipelago().get(islandIndex).conquer(player, game.getPlayers());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            game.getArchipelago().get(islandIndex).setNo_entry(false);
            game.getCharacters().get(findNoEntryCharacter(game.getCharacters())).returnNoEntry();
        }
        return game;
    }

    //Function of character 2
    public Game checkInfluenceWithModifiedBoard(Game game, Player player, Island island) {
        LinkedList<Player> originalPlayerList = game.getPlayers();
        int islandIndex;
        islandIndex = game.getArchipelago().indexOf(island);
        game = modifyBoard(game, player);
        if(!game.getArchipelago().get(islandIndex).getNo_entry()){
            for(Player playerToCheck: game.getPlayers()){
                try {
                    game.getArchipelago().get(islandIndex).conquer(playerToCheck, game.getPlayers());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            game.getArchipelago().get(islandIndex).setNo_entry(false);
        }
        for(int playerIndex = 0; playerIndex < game.getPlayers().size(); playerIndex++){
            originalPlayerList.get(playerIndex).getSchool().resetTowers(game.getPlayers().get(playerIndex).getSchool().getTowers());
        }
        game.setPlayers(originalPlayerList);
        return game;
    }

    //Function of character 6
    public Game checkInfluenceWithoutTowers(Game game){
        int islandIndex;
        List<Island> originalArchipelago = game.getArchipelago();
        for(Island islandToCheck: game.getArchipelago()){
            islandToCheck.setIsland_size(0);
        }
        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        if(!game.getArchipelago().get(islandIndex).getNo_entry()){
            for(Player player: game.getPlayers()){
                try {
                    game.getArchipelago().get(islandIndex).conquer(player, game.getPlayers());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            game.getArchipelago().get(islandIndex).setNo_entry(false);
            game.getCharacters().get(findNoEntryCharacter(game.getCharacters())).returnNoEntry();
        }
        game.setArchipelago(originalArchipelago);
        return game;
    }

    //Function of character 8
    public Game checkInfluenceWithBonus(Game game, Player player){
        int islandIndex;
        int influenceToAdd;
        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        if(!game.getArchipelago().get(islandIndex).getNo_entry()){
            for(Player playerToCheck: game.getPlayers()){
                try {
                    if(playerToCheck.equals(player)){
                        influenceToAdd = additionalInfluence;
                    }else{
                        influenceToAdd = 0;
                    }
                    game.getArchipelago().get(islandIndex).conquer(playerToCheck, game.getPlayers(), influenceToAdd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            game.getArchipelago().get(islandIndex).setNo_entry(false);
            game.getCharacters().get(findNoEntryCharacter(game.getCharacters())).returnNoEntry();
        }
        return game;
    }

    //Function of character 9
    public Game checkInfluenceWithoutColour(Game game, Colour colour){
        int islandIndex;
        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        if(!game.getArchipelago().get(islandIndex).getNo_entry()){
            for(Player playerToCheck: game.getPlayers()){
                try {
                    game.getArchipelago().get(islandIndex).conquer(playerToCheck, game.getPlayers(), colour);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            game.getArchipelago().get(islandIndex).setNo_entry(false);
            game.getCharacters().get(findNoEntryCharacter(game.getCharacters())).returnNoEntry();
        }
        return game;
    }

    private Game modifyBoard(Game game, Player player) {
        for(Colour colour: Colour.values()){
            for(Player playerToCheck: game.getPlayers()){
                if(!player.equals(playerToCheck) && playerToCheck.getSchool().getDining_room(colour).getStudents()==player.getSchool().getDining_room(colour).getStudents()){
                    playerToCheck.getOwned_professor().remove(new Professor(colour));
                    player.getOwned_professor().add(new Professor(colour));
                }
            }
        }
        return game;
    }

    private int findNoEntryCharacter(LinkedList<Character> characters){
        for(Character characterToCheck: characters){
            if(characterToCheck.getCharacterNumber() == noEntryCharacterNumber){
                return characters.indexOf(characterToCheck);
            }
        }
        return -1;
    }
}
