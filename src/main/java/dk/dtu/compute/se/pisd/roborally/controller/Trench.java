package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;
/**
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

