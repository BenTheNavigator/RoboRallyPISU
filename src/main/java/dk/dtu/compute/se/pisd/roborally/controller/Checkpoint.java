package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * A space on the board that serves as the goal. Obtaining the last checkpoint gives the player the win.
 * @author s235444
 * @author s235436
 */
public class Checkpoint extends FieldAction {
    private int checkpointNumber;

    private boolean lastCheckpoint;

    /**
     * A get method for a checkpoint number
     * @return returns a checkpoints number
     */
    public int getCheckpointNumber () {
        return checkpointNumber;
    }

    /**
     * A setter method for the checkpointnumber.
     * @param checkpointNumber is the int value of the checkpointNumber you wish to set.
     * @author s235444
     */
    public void setCheckpointNumber (int checkpointNumber) {
        this.checkpointNumber = checkpointNumber;
    }

    /**
     * A method to check if player checkpoints are correct and in accordance with checkpoint order. 
     * If they are, then the players checkpointCounter is increased to the checkpointNumber of the Checkpoint.
     * It checks if the final checkpoints has been reached, and if that is the case the phase is changed to winner and the status is updated.
     * @author 235444
     */
    public boolean getLastCheckpoint(){
        return lastCheckpoint;
    }

    public void setLastCheckpoint(boolean lastCheckpoint){
        this.lastCheckpoint = lastCheckpoint;
    }
    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player currentPlayer = space.getPlayer();
        if (currentPlayer.getCheckpointCounter() + 1 == checkpointNumber) {
            currentPlayer.setCheckpointCounter(checkpointNumber);
            if (lastCheckpoint) {
                gameController.board.setPhase(Phase.WINNER);
            }
            return true;
        }
        return false;
    }
}
