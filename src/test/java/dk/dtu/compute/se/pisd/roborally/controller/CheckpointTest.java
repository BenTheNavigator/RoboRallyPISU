package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckpointTest {
    @Test
    void doAction() {
        Board board = LoadBoard.loadBoard("roboloco");
        GameController gameController = new GameController(board);
        Player player = new Player(board, null, "TestPlayer");
        board.addPlayer(player);
        player.setSpace(board.getSpace(4, 7));
        Space space = player.getSpace();
        for (FieldAction action : space.getActions()) {
            action.doAction(gameController, space);
        }
        Assertions.assertEquals(player.getCheckpointCounter(), 1, "Player should have 1 checkpoint");
        Assertions.assertNotEquals(Phase.WINNER, board.getPhase(), "Board phase should not be winner");

        player.setCheckpointCounter(2);
        player.setSpace(board.getSpace(0,0));
        space = player.getSpace();
        for (FieldAction action : space.getActions()) {
            action.doAction(gameController, space);
        }
        Assertions.assertEquals(player.getCheckpointCounter(),3,"Player should have 3 checkpoints");
        Assertions.assertEquals(Phase.WINNER, board.getPhase(),"Board phase should be winner");
    }
}