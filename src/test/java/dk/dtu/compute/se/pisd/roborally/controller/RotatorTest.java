package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import dk.dtu.compute.se.pisd.roborally.model.Board;

import static org.junit.jupiter.api.Assertions.*;

class RotatorTest {

    @Test
    void doAction(){
        Board board = LoadBoard.loadBoard("roboloco");
        GameController gameController = new GameController(board);
        Player player = new Player(board,null, "TestPlayer");
        board.addPlayer(player);
        player.setSpace(board.getSpace(2,2));
        Space space = player.getSpace();
        for (FieldAction action : space.getActions()){
            action.doAction(gameController,space);
        }
        Assertions.assertEquals(Heading.WEST, player.getHeading(), "The player should be looking west");
        Assertions.assertNotEquals(Heading.SOUTH, player.getHeading(),"The player should no longer be looking south");
        Assertions.assertEquals(board.getSpace(2,2),player.getSpace(),"The player should still be on the same space");
        Assertions.assertNotEquals(Heading.EAST, player.getHeading(), "The player should not be looking east");

        for (FieldAction action : space.getActions()){
            action.doAction(gameController,space);
        }
        Assertions.assertEquals(Heading.NORTH, player.getHeading(), "The player should be looking north");
        Assertions.assertNotEquals(Heading.SOUTH, player.getHeading(),"The player should no longer be looking west");



    }



}