package it.polimi.ingsw.model;

import java.util.*;

public class CharacterFunctions {

    private static final int additionalInfluence = 2;
    private static final int noEntryCharacterNumber = 4;


    //Function of character 2
    public Game checkInfluenceWithModifiedBoard(Game game, Player player) {
        ArrayList<SchoolBoard> schoolBoardsBackup = new ArrayList<>();
        int islandIndex;
        doSchoolBoardsBackup(game.getPlayers(), schoolBoardsBackup);
        game = changeOwnership(game, player);
        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        if(!game.getArchipelago().get(islandIndex).getNo_entry()){
            game.getArchipelago().get(islandIndex).conquerCheck(game.getPlayers());
        } else {
            game.getArchipelago().get(islandIndex).setNo_entry(false);
            game.getSelectedCharacters().get(findNoEntryCharacter(game.getSelectedCharacters())).returnNoEntry();
        }
        restoreSchoolBoard(game.getPlayers(), schoolBoardsBackup);
        return game;
    }

    //Function of character 3
    public Game checkInfluenceOnSpecificIsland(Game game, Island island) {
        int islandIndex;
        islandIndex = game.getArchipelago().indexOf(island);
        if(!game.getArchipelago().get(islandIndex).getNo_entry()){
            game.getArchipelago().get(islandIndex).conquerCheck(game.getPlayers());
        } else {
            game.getArchipelago().get(islandIndex).setNo_entry(false);
            game.getSelectedCharacters().get(findNoEntryCharacter(game.getSelectedCharacters())).returnNoEntry();
        }
        return game;
    }

    //Function of character 6
    public Game checkInfluenceWithoutTowers(Game game) {
        int islandIndex;
        boolean noTowers = true;
        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        if(!game.getArchipelago().get(islandIndex).getNo_entry()){
            game.getArchipelago().get(islandIndex).conquerCheck(game.getPlayers(), noTowers);
        } else {
            game.getArchipelago().get(islandIndex).setNo_entry(false);
            game.getSelectedCharacters().get(findNoEntryCharacter(game.getSelectedCharacters())).returnNoEntry();
        }
        return game;
    }

    //Function of character 8
    public Game checkInfluenceWithBonus(Game game, Player player) {
        int islandIndex;
        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        if(!game.getArchipelago().get(islandIndex).getNo_entry()){
            game.getArchipelago().get(islandIndex).conquerCheck(game.getPlayers(), player, additionalInfluence);
        } else {
            game.getArchipelago().get(islandIndex).setNo_entry(false);
            game.getSelectedCharacters().get(findNoEntryCharacter(game.getSelectedCharacters())).returnNoEntry();
        }
        return game;
    }

    //Function of character 9
    public Game checkInfluenceWithoutColour(Game game, Colour colourToExclude) {
        int islandIndex;
        islandIndex = game.getArchipelago().indexOf(game.motherNaturePosition());
        if(!game.getArchipelago().get(islandIndex).getNo_entry()){
            game.getArchipelago().get(islandIndex).conquerCheck(game.getPlayers(), colourToExclude);
        } else {
            game.getArchipelago().get(islandIndex).setNo_entry(false);
            game.getSelectedCharacters().get(findNoEntryCharacter(game.getSelectedCharacters())).returnNoEntry();
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

    //Used to find the character that holds the "noEntry" cards, it works only if there's one
    private int findNoEntryCharacter(LinkedList<Character> characters){
        for(Character characterToCheck: characters){
            if(characterToCheck.getCharacterNumber() == noEntryCharacterNumber){
                return characters.indexOf(characterToCheck);
            }
        }
        return -1;
    }

    private void doSchoolBoardsBackup(LinkedList<Player> players, ArrayList<SchoolBoard> schoolBoards){
        for(int i = 0; i < players.size(); i++){
            schoolBoards.add(new SchoolBoard(players.get(i)));
            for(Professor ownedProfessor: players.get(i).getOwned_professor()){
                schoolBoards.get(i).putProfessor(new Professor(ownedProfessor.getColour()));
            }
        }
    }

    private void restoreSchoolBoard(LinkedList<Player> players, ArrayList<SchoolBoard> schoolBoards){
        for(int i = 0; i < players.size(); i++){
            players.get(i).getSchool().setOwned_professor(schoolBoards.get(i).getOwned_professor());
        }
    }
}
