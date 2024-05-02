/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 * All the necessities to make the functioning GAME work. Moving, playing etc.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController {

    /**
     * The board/game associated with RoboRally
     */
    final public Board board;

    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     * @author s235436
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space)  {
        Player current = board.getCurrentPlayer();
        current.setSpace(space);
        int playernumber = board.getPlayerNumber(current);
        Player next = board.getPlayer((playernumber+1) % board.getPlayersNumber());
        board.setCurrentPlayer(next);
        board.setCount(board.getCount()+1);
    }


    /**
     * A method that sets the phase to Programming
     */
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    /**
     * A method that generates a new card for programming
     */
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    /**
     * A method that changes the phase to Activation
     */
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
        board.setCount(board.getCount()+1);
    }

    /**
     * A method that makes it possible to touch the programming field
     * @param register the number programming card
     */

    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    /**
     * A method that makes it impossible to use the programming field
     */
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    /**
    A method that makes the program execute at once
     */
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    /**
    A method that makes the program execute one step at a time
     */
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    /**
     * The method that follows the previous ones, that actually executes whats been told
     */
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    /**
     * This goes to the next step in the programming phase. From Player1's first program to Player2's first
     * and so forth, before going to Player1's second program. We have in total a max of 5 steps in the game, as
     * we can only place a maximum of 5 cards.
     * It checks if the phase has been changed to WINNER, and if that is the case,
     * it displays the popup declaring the end of the game and the winner.
     * @author s235444
     */
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    if (command.isInteractive()) {
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return;
                    }
                    executeCommand(currentPlayer, command);
                }
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {

                    executeFieldActions();

                    if (board.getPhase() != Phase.ACTIVATION) {
                        if (board.getPhase() == Phase.WINNER) {
                            displayPopup(board.getWinnerStatusMessage());
                        }
                        return;
                    }

                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    } else {
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            assert false;
        }
    }

    /**
     * Method to complete the action of a fieldaction like a conveyorbelt etc.
     * @author s235436
     */
    private void executeFieldActions() {
        for (int p = 0; p < board.getPlayersNumber(); p++ ) {
            Player player = board.getPlayer(p);
            Space space = player.getSpace();
            for (FieldAction action: space.getActions()) {
                action.doAction(this, space);
                if (board.getPhase() != Phase.ACTIVATION){
                    break;
                }
            }
        }

    }

    /**
     * A method used when the player has a choice that executes the choice
     * @param option
     */
    public void executeCommandOptionAndContinue(@NotNull Command option) {
        Player currentPlayer = board.getCurrentPlayer();
        if (currentPlayer != null && board.getPhase() == Phase.PLAYER_INTERACTION && option !=null) {
            board.setPhase(Phase.ACTIVATION);
            executeCommand(currentPlayer,option);

            int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
            if (nextPlayerNumber < board.getPlayersNumber()) {
                board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
            } else {
                int step = board.getStep()+1;
                if (step < Player.NO_REGISTERS) {
                    makeProgramFieldsVisible(step);
                    board.setStep(step);
                    board.setCurrentPlayer(board.getPlayer(0));
                } else {
                    startProgrammingPhase();
                }
            }

        }
    }

    /**
     * A method that calls the basic movement options
     * @param player the player whose turn it is
     * @param command the card type. Does it move forward or turn right etc.
     */
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case FORWARD:
                    this.moveForward(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player);
                    break;
                case BACKWARD:
                    this.moveBackward(player);
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }

    /**
     * Method to move forward
     * @param player the player whose turn it is
     * @author s235436
     */
    public void moveForward(@NotNull Player player) {
        if (player.board == board) {
            Space space = player.getSpace();
            Heading heading = player.getHeading();

            if (!space.getWalls().contains(heading)) {


                Space target = board.getNeighbour(space, heading);
                Heading reverseHeading = heading.next().next();
                if (!target.getWalls().contains(reverseHeading)){
                    if (target != null) {
                        try {
                            moveToSpace(player, target, heading);
                        } catch (ImpossibleMoveException e) {
                            // we don't do anything here  for now; we just catch the
                            // exception so that we do no pass it on to the caller
                            // (which would be very bad style).
                        }
                    }
                }
            }
        }
    }

    /**
     * Method to move the player but checking and pushing if object is in the way
     * @param player the player that needs to move
     * @param space the space the player wants to move to
     * @param heading the heading of the player
     * @throws ImpossibleMoveException if its impossible to move to this space because of walls or similar
     */
    void moveToSpace(@NotNull Player player, @NotNull Space space, @NotNull Heading heading) throws ImpossibleMoveException {
        assert board.getNeighbour(player.getSpace(), heading) == space; // make sure the move to here is possible in principle
        Player other = space.getPlayer();
        if (other != null){
            Space target = board.getNeighbour(space, heading);
            if (target != null && !target.getWalls().contains(heading.next().next()) && !space.getWalls().contains(heading)) {
                // XXX Note that there might be additional problems with
                //     infinite recursion here (in some special cases)!
                //     We will come back to that!
                moveToSpace(other, target, heading);

                // Note that we do NOT embed the above statement in a try catch block, since
                // the thrown exception is supposed to be passed on to the caller

                assert target.getPlayer() == null : target; // make sure target is free now
            } else {
                throw new ImpossibleMoveException(player, space, heading);
            }
        }
        player.setSpace(space);
    }

    class ImpossibleMoveException extends Exception {

        private Player player;
        private Space space;
        private Heading heading;

        public ImpossibleMoveException(Player player, Space space, Heading heading) {
            super("Move impossible");
            this.player = player;
            this.space = space;
            this.heading = heading;
        }
    }

    /**
     * Method to move forwards 2 spaces
     * @param player the player whose turn it is
     * @author s235436
     */
    public void fastForward(@NotNull Player player) {
        moveForward(player);
        moveForward(player);

    }

    /**
     * Method to change heading clockwise
     * @param player the player whose turn it is
     * @author s235436
     */
    public void turnRight(@NotNull Player player) {
        Heading heading = player.getHeading();
        Heading nextHeading = heading.next();
        player.setHeading(nextHeading);
    }

    /**
     * Method to change heading counterclockwise
     * @param player the player whose turn it is
     * @author s235436
     */
    public void turnLeft(@NotNull Player player) {
        Heading heading = player.getHeading();
        Heading prevHeading = heading.prev();
        player.setHeading(prevHeading);
    }

    /**
     * Method to move backward
     * @param player the player that has to move
     * @author s235436
     */
    public void moveBackward(@NotNull Player player) {
        turnLeft(player);
        turnLeft(player);
        moveForward(player);
        turnLeft(player);
        turnLeft(player);
    }

    /**
     * A method to move the card from the hand to the field
     * @param source the card in the hand
     * @param target the open spot in the field
     * @return
     */
    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }


    /**
     * This method displays a popup declaring the winner of the game.
     * @author s235444
     * @param message string declaring the winner
     */
    private void displayPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("There is a winner!!!");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);

        alert.showAndWait();
    }



}
