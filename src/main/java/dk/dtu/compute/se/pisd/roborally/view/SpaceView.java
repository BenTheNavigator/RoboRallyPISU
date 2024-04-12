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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.*;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 60; // 60; // 75;
    final public static int SPACE_WIDTH = 60;  // 60; // 75;

    public final Space space;


    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        } else {
            this.setStyle("-fx-background-color: black;");
        }

        // updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    private void updatePlayer() {


         //Trying to move this to updateview instead
        //this.getChildren().clear();

        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0 );
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90*player.getHeading().ordinal())%360);
            this.getChildren().add(arrow);
        }
    }

    @Override
    public void updateView(Subject subject) {
        this.getChildren().clear();
        if (subject == this.space) {

            Pane pane = new Pane();
            Rectangle rectangle = new Rectangle(0.0,0.0,SPACE_WIDTH,SPACE_HEIGHT);
            rectangle.setFill(Color.TRANSPARENT);
            pane.getChildren().add(rectangle);

            for (FieldAction action : space.getActions()){
                if (action instanceof ConveyorBelt){
                    ConveyorBelt conveyorBelt = (ConveyorBelt) action;
                    Heading conveyorHeading = conveyorBelt.getHeading();

                    switch(conveyorHeading){
                        case SOUTH:
                            Polygon southArrow = new Polygon(0.0, 0.0,
                                    20.0, 40.0,
                                    40.0, 0.0 );
                            southArrow.setTranslateX((SPACE_WIDTH-40.0)/2);
                            southArrow.setTranslateY((SPACE_HEIGHT-40.0)/2);
                            pane.getChildren().add(southArrow);
                            southArrow.setFill(Color.LIGHTGRAY);
                            break;

                        case EAST:
                            Polygon eastArrow = new Polygon(0.0, 0.0,
                                    20.0, 40.0,
                                    40.0, 0.0 );
                            eastArrow.setTranslateX((SPACE_WIDTH-40.0)/2);
                            eastArrow.setTranslateY((SPACE_HEIGHT-40.0)/2);
                            pane.getChildren().add(eastArrow);
                            eastArrow.setFill(Color.LIGHTGRAY);
                            eastArrow.setRotate(270);
                            break;


                        case WEST:
                            Polygon westArrow = new Polygon(0.0, 0.0,
                                    20.0, 40.0,
                                    40.0, 0.0 );
                            westArrow.setTranslateX((SPACE_WIDTH-40.0)/2);
                            westArrow.setTranslateY((SPACE_HEIGHT-40.0)/2);
                            pane.getChildren().add(westArrow);
                            westArrow.setFill(Color.LIGHTGRAY);
                            westArrow.setRotate(90);
                            break;

                        case NORTH:
                            Polygon northArrow = new Polygon(0.0, 0.0,
                                    20.0, 40.0,
                                    40.0, 0.0 );
                            northArrow.setTranslateX((SPACE_WIDTH-40.0)/2);
                            northArrow.setTranslateY((SPACE_HEIGHT-40.0)/2);
                            pane.getChildren().add(northArrow);
                            northArrow.setFill(Color.LIGHTGRAY);
                            northArrow.setRotate(180);
                            break;

                    }
                }
                if (action instanceof Rotator){
                    Rotator rotator = (Rotator) action;
                    int rotation = rotator.getRotation();

                    Circle rotatorCircle = new Circle(20);
                    rotatorCircle.setCenterX(SPACE_WIDTH/2.0);
                    rotatorCircle.setCenterY(SPACE_HEIGHT/2.0);
                    pane.getChildren().add(rotatorCircle);
                    rotatorCircle.setFill(Color.LIGHTGRAY);

                    Text rotatorText = new Text();
                    rotatorText.setFont(Font.font("Arial", FontWeight.BOLD,12));
                    rotatorText.setFill(Color.PURPLE);
                    rotatorText.setTextAlignment(TextAlignment.CENTER);
                    //rotatorText.setX(rotatorCircle.getCenterX() - rotatorText.getBoundsInLocal().getWidth() / 2);
                    rotatorText.setX(SPACE_WIDTH/3.0);
                    rotatorText.setY(rotatorCircle.getCenterY() + rotatorText.getBoundsInLocal().getHeight() / 2);

                    pane.getChildren().add(rotatorText);



                    switch (rotation){
                        //Turn counterclockwise
                        case 1:
                            rotatorText.setText("CCW");

                            break;

                        //Turn clockwise
                        case 2:
                            rotatorText.setText("CW");

                            break;
                    }

                }
                /**
                 * @author s235458
                 */
                if (action instanceof Trench) {
                    Trench trench = (Trench) action;


                    Rectangle trenchSquare = new Rectangle(SPACE_WIDTH, SPACE_HEIGHT);
                    //trenchSquare.setTranslateX(SPACE_WIDTH / 2.0);
                    //trenchSquare.setTranslateY(SPACE_HEIGHT / 2.0);


                    pane.getChildren().add(trenchSquare);


                    trenchSquare.setFill(Color.CYAN);
                }
                if (action instanceof Checkpoint){
                    Checkpoint checkpoint = (Checkpoint) action;

                    Rectangle checkpointSquare = new Rectangle(SPACE_WIDTH/2.0, SPACE_HEIGHT/2.0);

                    double topLeftX = SPACE_WIDTH/2.0 - SPACE_WIDTH/4.0;
                    double topLeftY = SPACE_HEIGHT/2.0 - SPACE_HEIGHT/4.0;

                    checkpointSquare.setX(topLeftX);
                    checkpointSquare.setY(topLeftY);


                    pane.getChildren().add(checkpointSquare);

                    checkpointSquare.setFill(Color.GOLD);


                    Text checkpointNumber = new Text(String.valueOf(checkpoint.getCheckpointNumber()));
                    checkpointNumber.setFont(Font.font("Arial", FontWeight.BOLD,12));
                    checkpointNumber.setFill(Color.BLACK);

                    checkpointNumber.setX(topLeftX + (SPACE_WIDTH/4.0 - checkpointNumber.getLayoutBounds().getWidth() / 2));
                    checkpointNumber.setY(topLeftY + (SPACE_HEIGHT/4.0 + checkpointNumber.getLayoutBounds().getHeight() / 2));


                    pane.getChildren().add(checkpointNumber);

                }

            }
            for (Heading heading : space.getWalls()){
                switch (heading) {
                    case SOUTH:
                        Line southLine = new Line(2,SPACE_HEIGHT-2,SPACE_WIDTH-2,SPACE_HEIGHT-2);
                        southLine.setStroke(Color.RED);
                        southLine.setStrokeWidth(5);
                        pane.getChildren().add(southLine);
                        break;
                    case EAST:
                        Line eastLine = new Line(SPACE_WIDTH-2,2,SPACE_WIDTH-2,SPACE_HEIGHT-2);
                        eastLine.setStroke(Color.RED);
                        eastLine.setStrokeWidth(5);
                        pane.getChildren().add(eastLine);
                        break;
                    case WEST:
                        Line westLine = new Line(2,2,2,SPACE_HEIGHT-2);
                        westLine.setStroke(Color.RED);
                        westLine.setStrokeWidth(5);
                        pane.getChildren().add(westLine);
                        break;

                    case NORTH:
                        Line northLine = new Line(2,2,SPACE_WIDTH-2,2);
                        northLine.setStroke(Color.RED);
                        northLine.setStrokeWidth(5);
                        pane.getChildren().add(northLine);
                        break;
                }
            }



            this.getChildren().add(pane);
            updatePlayer();
        }
    }

}
