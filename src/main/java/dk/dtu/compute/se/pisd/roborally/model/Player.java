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
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import org.jetbrains.annotations.NotNull;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * The class player revolves around the robot and cards, and are specific for each player.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Player extends Subject {

    /**
     * Number of cards that can be played in 1 turn
     */
    final public static int NO_REGISTERS = 5;
    /**
     * Number of cards on hand each turn
     */
    final public static int NO_CARDS = 8;

    /**
     * The board associated with this player
     */
    final public Board board;
    /**
     * The name associated with this player
     */
    private String name;
    /**
     * The color of this players robot
     */
    private String color;
    /**
     * The space the player is on
     */
    private Space space;
    /**
     * The players heading, starts with south
     */
    private Heading heading = SOUTH;

    /**
     *An array of cards in the programming field
     */
    private CommandCardField[] program;
    /**
     * An array of cards on hand
     */
    private CommandCardField[] cards;



    /**
     * An integer for players number of checkpoints collected
     */
    private int checkpointCounter;

    /**
     * a boolean to distinguish if a player is a winner
     */
    private boolean winner;

    /**
     * A constructor for new player
     * @param board the board the player is on
     * @param color the color of the player
     * @param name the name of the player
     * @param checkpointCounter the sum of the checkpoints collected
     * @param winner boolean if the player is a winner
     */
    public Player(@NotNull Board board, String color, @NotNull String name) {
        this.board = board;
        this.name = name;
        this.color = color;
        checkpointCounter = 0;
        winner = false;


        this.space = null;

        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        cards = new CommandCardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CommandCardField(this);
        }
    }

    /**
     * A getmethod for the player checkpointCounter
     * @return int checkpointCounter
     */
    public int getCheckpointCounter () {
        return checkpointCounter;
    }


    /**
     * A setter method for the checkpointCounter
     * @return the new checkpointCounter value
     */
    public void setCheckpointCounter (int checkpointCounter) {
        this.checkpointCounter = checkpointCounter;
        notifyChange();
    }

    /**
     * a setter for the player's winner status
     * @param player
     */

    public static void setWinner(Player player) {
        player.winner = true;
    }


    /**
     * A getmethod for the player name
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * A setmethod for the player name
     * @param name the new name for the player
     */
    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * A getmethod for the player color
     * @return the player color
     */
    public String getColor() {
        return color;
    }

    /**
     * A setmethod for the player color
     * @param color the new color
     */
    public void setColor(String color) {
        this.color = color;
        notifyChange();
        if (space != null) {
            space.playerChanged();
        }
    }

    /**
     * A getmethod for player space
     * @return the space of the player
     */
    public Space getSpace() {
        return space;
    }

    /**
     * A setmethod for the player space
     * @param space the new player space
     */
    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace &&
                (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setPlayer(null);
            }
            if (space != null) {
                space.setPlayer(this);
            }
            notifyChange();
        }
    }

    /**
     * A getmethod for the player heading
     * @return the player heading
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * A getmethod for the player heading
     * @param heading the player heading
     */
    public void setHeading(@NotNull Heading heading) {
        if (heading != this.heading) {
            this.heading = heading;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * A getmethod for the players programfield
     * @param i an int for the specific card in the programming fields array
     * @return The programming field card for i
     */
    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    /**
     * A getmethod for the players cardfield
     * @param i an int for the specific card in the card fields array
     * @return The card field card for i
     */
    public CommandCardField getCardField(int i) {
        return cards[i];
    }

}
