package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import java.text.Format;

public class Rotator extends FieldAction {

    private int rotation;

    public int getRotation(){
        return rotation;
    }

    public void setRotation(int rotation){
        this.rotation = rotation;
    }

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
