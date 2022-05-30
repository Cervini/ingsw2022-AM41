package it.polimi.ingsw.model;

import java.util.LinkedList;
import java.util.List;

public class GameConclusionChecks {

    public GameConclusionChecks() {
    }

    public TowerColour endBecauseOfArchipelagoSize(int minSize, List<Island> archipelago, List<Player> players){
        Player winner;
        if(archipelago.size()<=minSize){
            winner = checkWinner(players, archipelago);
            return winner.getTeam();
        }
        return null;
    }

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

    /**
     * method used in endGame
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


