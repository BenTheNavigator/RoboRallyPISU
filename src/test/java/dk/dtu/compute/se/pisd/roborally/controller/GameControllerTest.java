package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.CommandCard;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.makeProgramFieldsInvisible;
import dk.dtu.compute.se.pisd.roborally.model.GameController
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
        gameController.startProgrammingPhase();
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
    void FinishProgrammingPhase1() {
        gameController.startProgrammingPhase(); // To set the initial conditions as expected
        gameController.finishProgrammingPhase();
    
        int playerCount = gameController.board.getPlayersNumber(); // Assuming this method exists to get the count of players
        for (int i = 0; i < playerCount; i++) {
            Player player = gameController.board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                Assertions.assertTrue(player.getProgramField(j).isVisible(), "Program fields should be invisible.");
            }
        }
    
        Assertions.assertEquals(Phase.ACTIVATION, gameController.board.getPhase(), "Game phase should be ACTIVATION.");
        Assertions.assertEquals(gameController.board.getPlayer(0), gameController.board.getCurrentPlayer(), "Current player should be the first player.");
        Assertions.assertEquals(0, gameController.board.getStep(), "Step count should be reset to 0.");
    }
    
    @Test
    void FinishProgrammingPhase() {
        gameController.startProgrammingPhase();
        gameController.finishProgrammingPhase();

        // Example of using assertion methods directly because of static import
        Assertions.assertFalse(gameController.board.getPhase() == Phase.ACTIVATION, "Game phase should not be ACTIVATION.");
        Assertions.assertEquals(Phase.ACTIVATION, gameController.board.getPhase(), "Game phase should be ACTIVATION.");
        Assertions.assertEquals(gameController.board.getPlayer(0), gameController.board.getCurrentPlayer(), "Current player should be the first player.");
        Assertions.assertEquals(0, gameController.board.getStep(), "Step count should be reset to 0.");
    }
    

    @Test
    public CommandCard GenerateRandomCommandCard() {
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
    public void GenerateRandomCommandCardPositive() {
        CommandCard commandCard = generateRandomCommandCard();
        Assertions.assertNotNull(commandCard);
        Assertions.assertNotNull(commandCard.getCommand());
        Assertions.assertTrue(commandCard.getCommand() instanceof Command);
    }

    @Test
    void GenerateRandomCommandCardNegative() {
        // Test when the input is null
        CommandCard commandCard = new CommandCard(null);
        assertNull(commandCard.getCommand());
        
        // Test when the input is out of bounds
        CommandCard commandCard2 = new CommandCard(Command.values().length + 1);
        assertNull(commandCard2.getCommand());
    }

@Test
void MakeProgramFieldsVisible() {
    // Testing if the fields is visible for each player.

    gameController.makeProgramFieldsVisible();

    // Now, check that the program field is visible for each player
    gameController.board.getPlayer().forEach(player -> {
        Assertions.assertTrue(player.getProgramField(0).isVisible(), "The first program field should be visible for all players.");
    });
}

@Test
    void ExecutePrograms() {
        gameController.executePrograms(); // This should execute all commands set above

        // Test if the board's phase is still ACTIVATION after execution
        Assertions.assertEquals(Phase.ACTIVATION, gameController.board.getPhase(), "Phase should be ACTIVATION after all programs are executed");

        // Check if all players moved to the expected positions
        for (Player player : gameController.board.getPlayers()) {
            Space expectedSpace = null;
            // Assume FORWARD moves each player one space forward in their heading
            switch (player.getHeading()) {
                case NORTH:
                    expectedSpace = gameController.board.getSpace(player.getSpace().getX(), player.getSpace().getY() - 1);
                    break;
                case SOUTH:
                    expectedSpace = gameController.board.getSpace(player.getSpace().getX(), player.getSpace().getY() + 1);
                    break;
                case EAST:
                    expectedSpace = gameController.board.getSpace(player.getSpace().getX() + 1, player.getSpace().getY());
                    break;
                case WEST:
                    expectedSpace = gameController.board.getSpace(player.getSpace().getX() - 1, player.getSpace().getY());
                    break;
            }
            Assertions.assertSame(expectedSpace, player.getSpace(), "Player should have moved forward to the next space in their heading");
        }
    }

    @Test
    void ExecuteStep() {
    // Initially start the programming phase to initialize the steps
    
        // Initially verify that no steps have been executed
        Assertions.assertEquals(0, gameController.board.getStep(), "Initial step count should be zero.");
    
        // Execute a single step
        gameController.executeStep();
    
        // Verify step mode is true, ensuring we are stepping through commands one at a time
        Assertions.assertTrue(gameController.board.isStepMode(), "Step mode should be enabled after executeStep.");
    
        // Assert that only one step has been executed
        Assertions.assertEquals(1, gameController.board.getStep(), "One step should have been executed after calling executeStep.");
    
        // Check if the game phase remains as ACTIVATION (assuming no player interaction is triggered)
        Assertions.assertEquals(Phase.ACTIVATION, gameController.board.getPhase(), "Game phase should still be ACTIVATION if no interactive command was executed.");
    
        Player currentPlayer = gameController.board.getCurrentPlayer();
        // Check expected changes to currentPlayer state here, which depends on what command they executed
    }

    @Test
    void ExecuteNextStep() {
    // Initially verify that no steps have been executed
    Assertions.assertEquals(0, gameController.board.getStep(), "Initial step count should be zero.");

    // Execute a single step which should internally call executeNextStep()
    gameController.executeStep();

    // Verify step mode is true, ensuring we are stepping through commands one at a time
    Assertions.assertTrue(gameController.board.isStepMode(), "Step mode should be enabled after executeStep.");

    // Assert that only one step has been executed
    Assertions.assertEquals(1, gameController.board.getStep(), "One step should have been executed after calling executeStep.");

    // Check if the game phase remains as ACTIVATION (assuming no player interaction is triggered)
    Assertions.assertEquals(Phase.ACTIVATION, gameController.board.getPhase(), "Game phase should still be ACTIVATION if no interactive command was executed.");

    // Check that the current player's position has changed according to the command.
    Player currentPlayer = gameController.board.getCurrentPlayer();
    Space initialSpace = currentPlayer.getSpace();
    Heading heading = currentPlayer.getHeading();
    Space expectedSpace = gameController.board.getNeighbour(initialSpace, heading);

    Assertions.assertEquals(expectedSpace, currentPlayer.getSpace(), "Current player should move according to their command card.");

    // Optionally, ensure that currentPlayer is set to the next player if that is part of the step logic
    int currentPlayerIndex = gameController.board.getPlayers().indexOf(currentPlayer);
    Player expectedNextPlayer = gameController.board.getPlayers().get((currentPlayerIndex + 1) % gameController.board.getPlayers().size());
    Assertions.assertEquals(expectedNextPlayer, gameController.board.getCurrentPlayer(), "Next player should now be the current player after step execution.");
}


 // Koden er helt gg, skal lige tjekkes op på igen
    @Test
    void ExecuteFieldActions() {
    // Arrange: Prepare the board and a player with a predictable field action
    Player player = gameController.board.getCurrentPlayer();
    Space startingSpace = player.getSpace(); // Use the player's starting space as defined in setUp()

    // Assume there is a specific field action that moves the player to a new space when triggered
    FieldAction mockAction = new FieldAction() {
        @Override
        public void doAction(GameController gameController, Space space) {
            // Define a new target space for the action
            Space targetSpace = gameController.board.getSpace(space.getX() + 1, space.getY()); // Example move right
            gameController.moveCurrentPlayerToSpace(targetSpace);
        }
    };
    startingSpace.addAction(mockAction);

    // Act: Execute a method that results in `executeFieldActions()` being called
    gameController.executeStep(); // This should indirectly trigger `executeFieldActions()`

    // Assert: Verify that the field action was executed properly
    Space expectedSpace = gameController.board.getSpace(startingSpace.getX() + 1, startingSpace.getY());
    Assertions.assertEquals(expectedSpace, player.getSpace(), "Player should have moved to the right by one space due to field action.");

    // Clean up to remove the mock action to prevent side effects in other tests
    startingSpace.removeAction(mockAction);
}
}