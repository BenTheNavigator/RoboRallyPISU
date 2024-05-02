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

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a conveyor belt on a space.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author s235436
 */
// XXX A3
public class ConveyorBelt extends FieldAction {

    /**
     * The direction that the conveyorbelt pushes the player
     */
    private Heading heading;

    /**
     * A method to get the heading
     * @return the direction, that the conveyor belt pushes the player
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * A method to set the heading
     * @param heading the new direction the conveyorbelt pushes the player
     */
    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    /**
     * The functionality of the conveyorbelt, that also checks if move is possible
     * @author s235436
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        // TODO A3: needs to be implemented
        // ...
        Player player = space.getPlayer();
        Heading heading = getHeading();
        try {
            Space nextSpace = gameController.board.getNeighbour(space,heading);
            if(nextSpace!=null){
                gameController.moveToSpace(player, nextSpace, heading);
            }
                return true;
        } catch (GameController.ImpossibleMoveException e){
            return false;
        }
    }

}
