package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import java.text.Format;

/**
 * A fieldaction that rotates the player standing on top
 * @author s235436
 * @author s224572
 */
public class Rotator extends FieldAction {

    /**
     * An int value that determines if the rotation is counter clockwise (1) or clockwise(2)
     */
    private int rotation;

    /**
     * A method to find the rotation
     * @return either 1 or 2 (CCW or CW)
     */
    public int getRotation(){
        return rotation;
    }

    /**
     * A method to set the rotation
     * @param rotation the int value of either CCW or CW
     */
    public void setRotation(int rotation){
        this.rotation = rotation;
    }

    /**
     * Implementation of how the rotator works
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     * @return true of false depending on if completed or not
     * @author s235436
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space){
        Player player = space.getPlayer();
        Heading playerHeading = player.getHeading();

        if (rotation==1){
            player.setHeading(playerHeading.prev());
            return true;
        }
        if (rotation==2){
            player.setHeading(playerHeading.next());
            return true;
        }
        return false;

    }
}
