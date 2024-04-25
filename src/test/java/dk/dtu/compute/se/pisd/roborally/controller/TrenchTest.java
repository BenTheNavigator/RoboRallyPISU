package dk.dtu.compute.se.pisd.roborally.controller;

import com.mysql.cj.protocol.x.XMessage;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author s235458
 * This test class is created to test the Trench, which it does in the doAction method.
 * It verifies if the player landing in the Trench, loses a checkpoint, after landing in the Trench.
 * It Checks that a players checkpoint counter has gone down by 1.
 */
class TrenchTest {
    @Test
    void doAction() {
        Board board = LoadBoard.loadBoard("poseidon");
        GameController gameController = new GameController(board);
        Player player = new Player(board, null, "TestPlayer");
        board.addPlayer(player);
        player.setSpace(board.getSpace(1, 0));
        player.setCheckpointCounter((1));
        Space space = player.getSpace();
        for (FieldAction action : space.getActions()) {
            action.doAction(gameController, space);
        }

        Assertions.assertEquals(player.getCheckpointCounter(),0,"The player has lost one checkpoint");
        Assertions.assertEquals(board.getSpace(1,0),player.getSpace(),"The player should still be on the same space");
    }
}