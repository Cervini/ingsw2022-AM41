package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.LinkedList;

public class IgnoreTiedEffect extends CharacterDecorator{

    public IgnoreTiedEffect(SimpleCharacter decoratedCharacter) {
        super(decoratedCharacter);
    }

    @Override
    public void effect(ArrayList<Object> args) {
        super.effect(args);
        // parsing
        Player user = (Player) args.get(0);

        int tmp; // temp variable used to check the maximum
        LinkedList<Integer> tie_checker = new LinkedList<Integer>(); // temp list used to check possible ties
        Player owner = null;
        for(Colour colour: Colour.values()) { // for each color
            // set as greatest number of students the number of students owned by the first player
            tmp = getDecoratedCharacter().getGame().getPlayers().get(0).getSchool().getDining_room(colour).getStudents();
            tie_checker.add(tmp);
            if (tmp != 0)
                // if the first player has any student temporarily set them as the next owner
                owner = getDecoratedCharacter().getGame().getPlayers().get(0);
            for (Player playerToCheck : getDecoratedCharacter().getGame().getPlayers()) {
                // for each player
                if (playerToCheck.getSchool().getDining_room(colour).getStudents() > tmp) {
                    // if they own more students the next owner is changed
                    owner = playerToCheck;
                    tmp = playerToCheck.getSchool().getDining_room(colour).getStudents();
                    // add the number of students in the tie_checker list
                    tie_checker.add(tmp);
                }
            }

            if (!owner.equals(user)) {
                // tie check only if the owner is not the user
                boolean tie = false;
                for (Integer x : tie_checker) {
                    // tmp contains the maximum number of students found
                    if (tmp == x) {
                        // if found more than once the ownership is tied
                        tie = true;
                    }
                }
                // professors are moved only if ownership is no tied
                if (!tie) {
                    getDecoratedCharacter().getGame().moveProfessor(colour, owner);
                }
            } else {
                // if user ties still move professor
                getDecoratedCharacter().getGame().moveProfessor(colour, owner);
            }
        }
        args.remove(0);
    }

    @Override
    public void endEffect() {
        super.endEffect();
    }
}
