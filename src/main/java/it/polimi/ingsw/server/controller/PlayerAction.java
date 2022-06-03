package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.model.Player;

public class PlayerAction {
    private final Player player;
    private ActionType action;

    public PlayerAction(Player player, ActionType action) {
        this.player = player;
        this.action = action;
    }

    public Player getPlayer() {
        return player;
    }

    public ActionType getAction() {
        return action;
    }

    public void setNextAction() {
        this.action = this.action == ActionType.MOVE_STUDENT ?
                ActionType.MOVE_MOTHER_NATURE : ActionType.CHOOSE_CLOUD;
    }

    public enum ActionType {
        MOVE_STUDENT,
        MOVE_MOTHER_NATURE,
        CHOOSE_CLOUD
    }
}
