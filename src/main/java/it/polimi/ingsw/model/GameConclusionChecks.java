package it.polimi.ingsw.model;

import java.util.LinkedList;
import java.util.List;

public class GameConclusionChecks {

    public GameConclusionChecks() {
    }

    /**Checks if the game has to end because the size of the archipelago is too small
     * @param minSize minimum size of the archipelago
     * @param archipelago current list of islands in this game
     * @param players list of players participating this game
     * @return tower colour of the winner or null if the conditions to end the game are not met
     */
    public TowerColour endBecauseOfArchipelagoSize(int minSize, List<Island> archipelago, List<Player> players){
        Player winner;
        if(archipelago.size()<=minSize){
            winner = checkWinner(players, archipelago);
            return winner.getTeam();
        }
        return null;
    }

    /**Checks if the game has to end because there are no more students available
     * @param bag list of students available
     * @param archipelago current list of islands in this game
     * @param players list of players participating this game
     * @param winner tower colour of a player that already won in another way, null if it doesn't exist
     * @return tower colour of the winner or null if the conditions to end the game are not met
     */
    public TowerColour endBecauseAvailableStudentsFinished(LinkedList<Student> bag, List<Island> archipelago, List<Player> players, TowerColour winner){
        Player winnerPlayer;
        if(winner == null) {
            if (bag.size() == 0) {
                winnerPlayer = checkWinner(players, archipelago);
                return winnerPlayer.getTeam();
            }
        }
        return winner;
    }

    /**Checks if the game has to end because there are no more assistants available
     * @param archipelago current list of islands in this game
     * @param players list of players participating this game
     * @param winner winner tower colour of a player that already won in another way, null if it doesn't exist
     * @return tower colour of the winner or null if the conditions to end the game are not met
     */
    public TowerColour endBecauseAvailableAssistantsFinished(List<Island> archipelago, List<Player> players, TowerColour winner){
        if(winner == null) {
            Player winnerPlayer;
            for (Player playerToCheck : players) {
                if (playerToCheck.getAssistants().size() == 0) {
                    winnerPlayer = checkWinner(players, archipelago);
                    return winnerPlayer.getTeam();
                }
            }
        }
        return winner;
    }

    /**Checks if the game has to end because there are no more towers available
     * @param players list of players participating this game
     * @param winner winner tower colour of a player that already won in another way, null if it doesn't exist
     * @return tower colour of the winner or null if the conditions to end the game are not met
     */
    public TowerColour endBecauseAvailableTowersFinished(List<Player> players, TowerColour winner){
        if(winner == null) {
            for (Player playerToCheck : players) {
                if ((playerToCheck.isTower_holder()) && (playerToCheck.getSchool().getTowers() == 0)) {
                    return playerToCheck.getTeam();
                }
            }
        }
        return winner;
    }

    /**Method used in endGame to check which player wins
     * @return the player who meets the winning conditions
     */
    public Player checkWinner(List<Player> players, List<Island> archipelago){
        Player winner = null;
        int towers = 0;
        int tmp;
        boolean tie = false;
        for(Player playerToCheck : players){
            tmp = 0;
            for(Island islandToCheck : archipelago){
                if(playerToCheck.getTeam().equals(islandToCheck.getTower())){
                    tmp = tmp + islandToCheck.getIsland_size();
                }
            }
            if(towers < tmp){
                winner = playerToCheck;
                towers = tmp;
            }
            else if(towers == tmp){
                tie = true;
            }
        }
        if (tie) {
            for (Player playerToCheck : players) {
                if (winner == null) {
                    winner = playerToCheck;
                } else {
                    if (playerToCheck.getOwned_professor().size() > winner.getOwned_professor().size()) {
                        winner = playerToCheck;
                    }
                }
            }
        }
        return winner;
    }
}


