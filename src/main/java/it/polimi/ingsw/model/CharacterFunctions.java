package it.polimi.ingsw.model;

import java.util.*;

public class CharacterFunctions {

    private static final int additionalInfluence = 2;
    private static final int noEntryCharacterNumber = 4;

    /**Function of character 2
     * @requires game != null && player != null
     * @ensures \forall int i; i>= 0 && i < game.getPlayers().size();
     *              \old(game.getPlayers().get(i).getOwned_professor()).equals(game.getPlayers().get(i).getOwned_professor()) &&
     *          game.getPlayers().size() == \old(game.getPlayers().size()) &&
     *          game.getBag().size() == \old(game.getBag().size())
     * @param game is the current situation of the match that is going to be modified by the character
     * @param player is the player that activated the effect of the character
     * @return value is  the modified situation of the game
     */
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

    /**Function of character 3
     * @requires game != null && island != null
     * @ensures game.getPlayers().size() == \old(game.getPlayers().size()) &&
     *          game.getBag().size() == \old(game.getBag().size()) &&
     *          game.getArchipelago().size() <= \old(game.getArchipelago().size())
     * @param game is the current situation of the match that is going to be modified by the character
     * @param island on which it has to be calculated the influence, even if mother nature is not there
     * @return value is  the modified situation of the game
     */
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

    /**Function of character 6
     * @requires game != null
     * @ensures game.getPlayers().size() == \old(game.getPlayers().size()) &&
     *          game.getBag().size() == \old(game.getBag().size()) &&
     *          game.getArchipelago().size() <= \old(game.getArchipelago().size())
     * @param game is the current situation of the match that is going to be modified by the character
     * @return value is  the modified situation of the game
     */
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

    /**Function of character 8
     * @requires game != null && player != null
     * @ensures game.getPlayers().size() == \old(game.getPlayers().size()) &&
     *          game.getBag().size() == \old(game.getBag().size()) &&
     *          game.getArchipelago().size() <= \old(game.getArchipelago().size())
     * @param game is the current situation of the match that is going to be modified by the character
     * @param player is the player that activated the effect of the character
     * @return value is  the modified situation of the game
     */
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

    /**Function of character 9
     * @requires game != null && colourToExclude != null
     * @ensures game.getPlayers().size() == \old(game.getPlayers().size()) &&
     *          game.getBag().size() == \old(game.getBag().size()) &&
     *          game.getArchipelago().size() <= \old(game.getArchipelago().size())
     * @param game is the current situation of the match that is going to be modified by the character
     * @param colourToExclude is the colour selected by the player that will not affect the influence calculations
     *                        during this round
     * @return value is  the modified situation of the game
     */
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

    /**Used by checkInfluenceWithModifiedBoard to edit the SchoolBoards as Character 2 requires
     * @requires game != null && cardPlayer != null
     * @ensures \forall Player player; game.getPlayers().contains(player);
     *              !player.equals(cardPlayer) => \forall Colour colour; ;
     *                  (playerToCheck.hasProfessor(colour) && !game.getPlayers().get(cardPlayerIndex).hasProfessor(colour)
     *                  && playerToCheck.getSchool().getDining_room(colour).getStudents() <= cardPlayer.getSchool().getDining_room(colour).getStudents())
     *                  => cardPlayer.hasProfessor(colour)
     * @param game is the current situation of the match that is going to be modified by the character
     * @param cardPlayer is the player that activated the effect of the character
     * @return value is  the modified situation of the game
     */
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

    /**Used to find the character that holds the "noEntry" cards, it works only if there's one
     * @requires characters != null
     * @ensures characters.equals(\old(characters.size()))
     * @param characters is the list of selected characters for this game
     * @return value is the index of the character that has the power to place no entry cards
     */
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
