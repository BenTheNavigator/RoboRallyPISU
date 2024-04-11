package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import dk.dtu.compute.se.pisd.roborally.model.CommandCard;
import dk.dtu.compute.se.pisd.roborally.model.makeProgramFieldsInvisible
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameControllerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null,"Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }

    /**
     * Test for Assignment V1 (can be delete later once V1 was shown to the teacher)
     */
    @Test
    void testV1() {
        Board board = gameController.board;

        Player player = board.getCurrentPlayer();
        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        Assertions.assertEquals(player, board.getSpace(0, 4).getPlayer(), "Player " + player.getName() + " should beSpace (0,4)!");
    }

        /*
        The following tests should be used later for assignment V2
         */

    @Test
    void moveCurrentPlayerToSpace() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);

        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        Assertions.assertEquals(player1, board.getSpace(0, 4).getPlayer(), "Player " + player1.getName() + " should beSpace (0,4)!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertEquals(player2, board.getCurrentPlayer(), "Current player should be " + player2.getName() +"!");
    }

    @Test
    void moveForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }

    @Test
    void fastForward(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.fastForward(current);

        Assertions.assertEquals(current, board.getSpace(0,2).getPlayer());
        Assertions.assertEquals(Heading.SOUTH,current.getHeading());
        Assertions.assertNull(board.getSpace(4,2).getPlayer());
        Assertions.assertNull(board.getSpace(2,0).getPlayer());
    }

    @Test
    void turnRight(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.turnRight(current);

        Assertions.assertEquals(current, board.getSpace(0,0).getPlayer());
        Assertions.assertEquals(Heading.WEST,current.getHeading());
        Assertions.assertNotEquals(Heading.EAST,current.getHeading());
        Assertions.assertNotEquals(Heading.SOUTH,current.getHeading());
        Assertions.assertNotEquals(Heading.NORTH,current.getHeading());
        Assertions.assertNull(board.getSpace(0,1).getPlayer());
        Assertions.assertNull(board.getSpace(1,0).getPlayer());

    }

    @Test
    void turnLeft() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.turnLeft(current);

        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer());
        Assertions.assertEquals(Heading.EAST, current.getHeading());
        Assertions.assertNotEquals(Heading.WEST, current.getHeading());
        Assertions.assertNotEquals(Heading.SOUTH, current.getHeading());
        Assertions.assertNotEquals(Heading.NORTH, current.getHeading());
        Assertions.assertNull(board.getSpace(0, 1).getPlayer());
        Assertions.assertNull(board.getSpace(1, 0).getPlayer());
    }


    @Test
    void testFinishProgrammingPhase() {
        // Initialize the board for the test
        this.setUp(); // If board is not initialized as a field, make sure it is before calling this test
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        
        gameController.startProgrammingPhase(); // To set the initial conditions as expected
        gameController.finishProgrammingPhase();
    
        // Verify that all program fields are invisible
        for (Player player : gameController.board.getPlayers()) {
            for (int i = 0; i < Player.NO_REGISTERS; i++) {
                assertFalse(player.getProgramField(i).isVisible(), "Program fields should be invisible.");
            }
        }
    
        // Verify that the game phase is now ACTIVATION
        assertEquals(Phase.ACTIVATION, gameController.board.getPhase(), "Game phase should be ACTIVATION.");
    
        // Verify that the current player is set to the first player
        assertEquals(gameController.board.getPlayer(0), gameController.board.getCurrentPlayer(), "Current player should be the first player.");
    
        // Verify that the step count is reset to 0
        assertEquals(0, gameController.board.getStep(), "Step count should be reset to 0.");
    }
    

    @Test
    public void testGenerateRandomCommandCard() {
        Set<CommandCard> drawnCards = new HashSet<>();
        int numberOfDraws = 100; // Decide on a sufficiently large number of draws

        for (int i = 0; i < numberOfDraws; i++) {
            try {
                CommandCard card = (CommandCard) generateRandomCommandCardMethod.invoke(gameController);
                Assertions.assertNotNull(card, "The generated CommandCard should not be null");

                // Add to set to ensure uniqueness
                drawnCards.add(card);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                Assertions.fail("Invocation of generateRandomCommandCard failed.");
            }
        }

        // Check if the drawnCards set has multiple unique cards
        System.out.println("Unique cards generated: " + drawnCards.size());
        Assertions.assertTrue(drawnCards.size() > 1, "A variety of cards should be generated after multiple draws.");
    }

    @Test
    public void testGenerateRandomCommandCardPositive() {
        CommandCard commandCard = generateRandomCommandCard();
        assertNotNull(commandCard);
        assertNotNull(commandCard.getCommand());
        assertTrue(commandCard.getCommand() instanceof Command);
    }

    @Test
    void testGenerateRandomCommandCardNegative() {
        // Test when the input is null
        CommandCard commandCard = new CommandCard(null);
        assertNull(commandCard.getCommand());
        
        // Test when the input is out of bounds
        CommandCard commandCard2 = new CommandCard(Command.values().length + 1);
        assertNull(commandCard2.getCommand());
    }
}
@Test
void testMakeProgramFieldsVisible() {
    // Assuming you have a method that eventually calls makeProgramFieldsVisible
    // For this example, let's assume `startProgrammingPhase()` makes the first program field visible

    gameController.makeProgramFieldsVisible();

    // Now, check that the program field is visible for each player
    gameController.board.getPlayers().forEach(player -> {
        Assertions.assertTrue(player.getProgramField(0).isVisible(), "The first program field should be visible for all players.");
        // Add more assertions if you want to check other program fields based on your test conditions
    });
}



}