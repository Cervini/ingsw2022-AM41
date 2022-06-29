package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.model.Player;

public class PlayerAction {
    private final Player player;
    private ActionType action;

    /**
     *constructor of Player Action
     * @param player relevant player
     * @param action relevant action
     */
    public PlayerAction(Player player, ActionType action) {
        this.player = player;
        this.action = action;
    }

    /**
     *
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @return action which has to be performed by the player
     */
    public ActionType getAction() {
        return action;
    }

    /**
     *sets next action for the current player
     */
    public void setNextAction() {//predefined sequence of action during action phase
        this.action = this.action == ActionType.MOVE_STUDENT ?
                ActionType.MOVE_MOTHER_NATURE : ActionType.CHOOSE_CLOUD;
    }

    public enum ActionType {
        MOVE_STUDENT,
        MOVE_MOTHER_NATURE,
        CHOOSE_CLOUD
    }
}
