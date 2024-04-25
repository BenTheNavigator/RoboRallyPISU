package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConveyorBeltTest {
    @Test
    void doAction() {
        Board board = LoadBoard.loadBoard("roboloco");
        GameController gameController = new GameController(board);
        Player player = new Player(board, null, "TestPlayer");
        board.addPlayer(player);
        player.setSpace(board.getSpace(2, 0));
        Space space = player.getSpace();
        for (FieldAction action : space.getActions()) {
            action.doAction(gameController, space);
        }
        Assertions.assertEquals(board.getSpace(3,0),player.getSpace(),"The player should move one space to the left");
    }
}