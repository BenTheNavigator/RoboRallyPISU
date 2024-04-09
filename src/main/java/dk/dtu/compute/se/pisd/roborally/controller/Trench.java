package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;
/**
 * This class represents the Action field trench that a robot can land on.
 * The class Trench is a subclass of FieldAction.
 * The class Trench contains a variable of the type space which is the space associated with the trench
 * The doAction method contains the code that does the action when i robot falls into the trench
 * Here it will take the player that shares a space with the trench and subtract 1 from the players checkpoint counter
 * @author s235458
 */
public class Trench extends FieldAction{
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        Player player = space.getPlayer();

        // Decrease checkpoint value
        int currentCheckpointCounter = player.getCheckpointCounter();
        if (currentCheckpointCounter > 0) {
            player.setCheckpointCounter(currentCheckpointCounter - 1);
        }

        return true;
    }
}

