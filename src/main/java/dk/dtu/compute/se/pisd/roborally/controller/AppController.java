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

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.dal.GameInDB;
import dk.dtu.compute.se.pisd.roborally.dal.IRepository;
import dk.dtu.compute.se.pisd.roborally.dal.RepositoryAccess;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Generally is the functioning app. Options for saving game, loading game, starting a new games etc.
 *
 *
 *
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class AppController implements Observer {

    /**
     * A list of the amounts of players possible
     */
    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    /**
     * A list of the player colors in order
     */
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");


    /**
     * A list of the board options players can choose from
     * @author s235444
     */
    final private List<String> BOARD_CHOICES = Arrays.asList("defaultboard", "poseidon", "roboloco");


    /**
     * The game associated with this appcontroller
     */
    final private RoboRally roboRally;

    /**
     * The controller that relates to game functionality
     */
    private GameController gameController;

    /**
     * A constructor to create an Appcontroller
     * @param roboRally the game
     */


    //IRepository repository = RepositoryAccess.getRepository();

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    /**
     * A method to create new game
     */
    public void newGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        ChoiceDialog<String> dialog2 = new ChoiceDialog<>(BOARD_CHOICES.get(0), BOARD_CHOICES);
        dialog2.setTitle("Board options");
        dialog2.setHeaderText("Select a board");
        Optional<String> result2 = dialog2.showAndWait();

        if (result.isPresent() && result2.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }

            /** 
             * Changed board to load from LoadBoard, then we can load from JSON files.
             * @author s235444
            */
            Board board = LoadBoard.loadBoard(result2.get());

            gameController = new GameController(board);
            int no = result.get();
            for (int i = 0; i < no; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                board.addPlayer(player);
                player.setSpace(board.getSpace(i % board.width, i));
            }

            // XXX: V2
            // board.setCurrentPlayer(board.getPlayer(0));
            gameController.startProgrammingPhase();
            RepositoryAccess.getRepository().createGameInDB(board);

            roboRally.createBoardView(gameController);
        }
    }

    /**
     * Here we save the game in the database with a game ID. This can be used to load into it later.
     */
    public void saveGame() {
        RepositoryAccess.getRepository().updateGameInDB(gameController.board);
    }

    /**
     * A method to load a game. Fetches saved games from database and puts them in reverse order.
     * Then creates Dialog boxes where user can choose which save to load into. 
     * Afterwards it loads game onto board, creates new gamecontroller and add players to the board.
     * Lastly the View is created.
     * 
     * @author s23544
     */
    public void loadGame() {
        List<GameInDB> list = RepositoryAccess.getRepository().getGames();
        Collections.reverse(list);
        ChoiceDialog<GameInDB> dialog = new ChoiceDialog<>(list.get(0), list);
        dialog.setTitle("Load Game");
        dialog.setHeaderText("Select save to load");
        Optional<GameInDB> result = dialog.showAndWait();

        if (result.isPresent()) {
            int ID = result.get().id;
            Board board = RepositoryAccess.getRepository().loadGameFromDB(ID);
            gameController = new GameController(board);

            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                board.addPlayer(player);
                player.setSpace(board.getPlayer(i).getSpace());
            }
            roboRally.createBoardView(gameController);
        }
    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {

            // here we save the game (without asking the user).
            saveGame();

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }

    /**
     * A method to exit the game, that prompts an "are you sure" message
     */
    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    /**
     * Checks if game is ongoing
     * @return true or false
     */
    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

}
