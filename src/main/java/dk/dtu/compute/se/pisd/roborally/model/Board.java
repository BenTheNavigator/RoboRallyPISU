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

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;

/**
 * The board associated with RoboRally
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Board extends Subject {

    /**
     * The width of the board
     */
    public final int width;

    /**
     * The height of the board
     */

    public final int height;

    /**
     * The name of the board
     */
    public final String boardName;

    /**
     * An id that makes every game different, especially important when using databases
     */

    private Integer gameId;

    /**
     * The amount of spaces on the board
     */
    private final Space[][] spaces;

    /**
     * A list of players in the game
     */
    private final List<Player> players = new ArrayList<>();

    /**
     * The player whose turn it is
     */
    private Player current;

    /**
     * The phase set to initialization
     */

    private Phase phase = INITIALISATION;

    /**
     * The number of steps set to zero
     */

    private int step = 0;

    /**
     * Check if we are executing every step one at a time or all at once
     */

    private boolean stepMode;

    /**
     * Constructor to make a board
     * @param width the width of the board
     * @param height the height of the board
     * @param boardName the name of the board
     */
    public Board(int width, int height, @NotNull String boardName) {
        this.boardName = boardName;
        this.width = width;
        this.height = height;
        spaces = new Space[width][height];
        for (int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Space space = new Space(this, x, y);
                spaces[x][y] = space;
            }
        }
        this.stepMode = false;
    }

    /**
     * Alternative constructor with a default name
     * @param width the width of the board
     * @param height the height of the board
     */
    public Board(int width, int height) {
        this(width, height, "defaultboard");
    }

    /**
     * The id of the game
     * @return the primary key of the game
     */
    public Integer getGameId() {
        return gameId;
    }

    /**
     * Set a specific value as the gameid
     * @param gameId the id used as a primary key
     */
    public void setGameId(int gameId) {
        if (this.gameId == null) {
            this.gameId = gameId;
        } else {
            if (!this.gameId.equals(gameId)) {
                throw new IllegalStateException("A game with a set id may not be assigned a new id!");
            }
        }
    }

    /**
     * Find the coordinates of the space
     * @param x what column of the board's spaces
     * @param y what row of the board's spaces
     * @return the space specified
     */
    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    /**
     * How many players are in this game
     * @return an int of players
     */
    public int getPlayersNumber() {
        return players.size();
    }

    /**
     * Add another player to the game
     * @param player another player controlled robot
     */
    public void addPlayer(@NotNull Player player) {
        if (player.board == this && !players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }

    /**
     * Method to check specific player in the list
     * @param i what number of the list that needs to be checked
     * @return the player at i point in list
     */

    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    /**
     * The current player of the game.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return current;
    }

    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    /**
     * The phase of the game
     * @return the phase
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * Set the phase of the game
     * @param phase change to specific phase
     */
    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    /**
     * The number of steps in the game
     * @return the number of steps
     */
    public int getStep() {
        return step;
    }

    /**
     * Set the amount of steps
     * @param step the number of steps wanted
     */
    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    /**
     * Check if executing the program step by step or all at once
     * @return either true of false depending on executing step by step or all at once respectively
     */
    public boolean isStepMode() {
        return stepMode;
    }

    /**
     * Change from step by step to all at once or vice versa
     * @param stepMode true or false
     */
    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }

    /**
     * What place in the list does player have
     * @param player the player whose place in the list we want
     * @return the position of player in the list
     */

    public int getPlayerNumber(@NotNull Player player) {
        if (player.board == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
    }

    /**
     * Returns the neighbour of the given space of the board in the given heading.
     * The neighbour is returned only, if it can be reached from the given space
     * (no walls or obstacles in either of the involved spaces); otherwise,
     * null will be returned.
     *
     * @param space the space for which the neighbour should be computed
     * @param heading the heading of the neighbour
     * @return the space in the given direction; null if there is no (reachable) neighbour
     */
    public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) {
        int x = space.x;
        int y = space.y;
        switch (heading) {
            case SOUTH:
                y = (y + 1) % height;
                break;
            case WEST:
                x = (x + width - 1) % width;
                break;
            case NORTH:
                y = (y + height - 1) % height;
                break;
            case EAST:
                x = (x + 1) % width;
                break;
        }

        return getSpace(x, y);
    }

    /**
     * A String representation of information
     * @return a String that tells information
     */
    public String getStatusMessage() {
        // this is actually a view aspect, but for making assignment V1 easy for
        // the students, this method gives a string representation of the current
        // status of the game

        // XXX: V1 add the move count to the status message
        // XXX: V2 changed the status so that it shows the phase, the current player and the number of steps
        return "Current phase: " + getPhase() +  "  Current player: " + getCurrentPlayer().getName()
                + "  Number of steps: " + getStep() + "  Number of moves: " + getCount();
    }

    /**
     * Counts the number of moves in the game
     */
    private int count;

    /**
     * Number of moves in the game
     * @return the number of moves in the game
     */
    public int getCount(){
        return count;
    }

    /**
     * Sets the number of moves in the game
     * @param count the new amount of moves in the game
     */
    public void setCount(int count){
        if (this.count!= count){
            this.count = count;
            notifyChange();
        }

    }
}
