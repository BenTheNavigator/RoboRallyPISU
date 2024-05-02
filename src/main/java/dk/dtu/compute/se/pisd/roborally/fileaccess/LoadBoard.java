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
package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dk.dtu.compute.se.pisd.roborally.controller.*;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.*;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author s235444
 */
public class LoadBoard {

    private static final String BOARDSFOLDER = "boards";
    private static final String DEFAULTBOARD = "defaultboard";
    private static final String JSON_EXT = "json";

    public static Board loadBoard(String boardname) {
        if (boardname == null) {
            boardname = DEFAULTBOARD;
        }

        ClassLoader classLoader = LoadBoard.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(BOARDSFOLDER + "/" + boardname + "." + JSON_EXT);
        if (inputStream == null) {
            return new Board(8,8);
        }

		// In simple cases, we can create a Gson object with new Gson():
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(ActionTemplate.class, new Adapter<ActionTemplate>());
        Gson gson = simpleBuilder.create();

		Board result;
        JsonReader reader = null;
		try {
			reader = gson.newJsonReader(new InputStreamReader(inputStream));
			BoardTemplate template = gson.fromJson(reader, BoardTemplate.class);
			result = convert(template, boardname);
			reader.close();
			return result;
		} catch (IOException e1) {
            if (reader != null) {
                try {
                    reader.close();
                    inputStream = null;
                } catch (IOException e2) {}
            }
            if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e2) {}
			}
		}
		return null;
    }

    private static Board convert(BoardTemplate template, String boardname) {
        Board result = new Board(template.width, template.height, boardname);
        for (SpaceTemplate spaceTemplate: template.spaces) {
            Space space = result.getSpace(spaceTemplate.x, spaceTemplate.y);
            if (space != null) {
                space.getActions().addAll(convert(spaceTemplate.actions));
                space.getWalls().addAll(spaceTemplate.walls);
            }
        }
        return result;
    }

    private static List<FieldAction> convert(List<ActionTemplate> actionTemplates) {
        List<FieldAction> result = new ArrayList<>();

        for (ActionTemplate template: actionTemplates) {
            FieldAction fieldAction = convert(template);
            if (fieldAction != null) {
                result.add(fieldAction);
            }
        }

        return result;
    }

    /**
     *
     * @param actionTemplate
     * @return
     * @author s235436
     * @author s224572
     */
    private static FieldAction convert(ActionTemplate actionTemplate) {
        if (actionTemplate instanceof ConveyorBeltTemplate) {
            ConveyorBeltTemplate template = (ConveyorBeltTemplate) actionTemplate;
            ConveyorBelt conveyorBelt = new ConveyorBelt();
            conveyorBelt.setHeading(template.heading);
            return conveyorBelt;
        } else if (actionTemplate instanceof CheckPointTemplate) {
            CheckPointTemplate template = (CheckPointTemplate) actionTemplate;
            Checkpoint checkpoint = new Checkpoint();
            checkpoint.setCheckpointNumber(template.number);
            checkpoint.setLastCheckpoint(template.last);
            return checkpoint;
        } else if (actionTemplate instanceof RotatorTemplate) {
            RotatorTemplate template = (RotatorTemplate) actionTemplate;
            Rotator rotator = new Rotator();
            rotator.setRotation(template.rotation);
            return rotator;
        } else if (actionTemplate instanceof TrenchTemplate) {
            TrenchTemplate template = (TrenchTemplate) actionTemplate;
            Trench trench = new Trench();
            return trench;
        }

        return null;
    }


    public static void saveBoard(Board board, String name) {
        BoardTemplate template = convertToTemplate(board);
        template.width = board.width;
        template.height = board.height;

        ClassLoader classLoader = AppController.class.getClassLoader();
        // FIXME: this is not very defensive and will result in a NullPointerException
        //         when the folder BOARDSFOLDER does not exist! But, the file does not
        //         need to exist at this point!
        String filename =
                classLoader.getResource(BOARDSFOLDER).getPath() + "/" + name + "." + JSON_EXT;

        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(ActionTemplate.class, new Adapter<ActionTemplate>()).
                setPrettyPrinting();
        Gson gson = simpleBuilder.create();

        FileWriter fileWriter = null;
        JsonWriter writer = null;
        try {
            fileWriter = new FileWriter(filename);
            writer = gson.newJsonWriter(fileWriter);
            gson.toJson(template, template.getClass(), writer);
            writer.close();
        } catch (IOException e1) {
            if (writer != null) {
                try {
                    writer.close();
                    fileWriter = null;
                } catch (IOException e2) {}
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e2) {}
            }
        }
    }

    private static BoardTemplate convertToTemplate(Board board) {
        BoardTemplate result = new BoardTemplate();
        result.width = board.width;
        result.height = board.height;

        for (int x = 0; x < board.width; x++) {
            for (int y= 0; y < board.height; y++) {
                Space space = board.getSpace(x,y);
                if (space != null && ( !space.getActions().isEmpty() || !space.getWalls().isEmpty()) ) {
                    SpaceTemplate spaceTemplate = new SpaceTemplate();
                    spaceTemplate.x = x;
                    spaceTemplate.y = y;
                    spaceTemplate.actions = convertToTemplate(space.getActions());
                    spaceTemplate.walls = new ArrayList<>(space.getWalls());
                }
            }
        }
        return result;
    }

    private static List<ActionTemplate> convertToTemplate(List<FieldAction> actions) {
        List<ActionTemplate> result = new ArrayList<>();

        for (FieldAction action: actions) {
            ActionTemplate template = convertToTemplate(action);
            if (template != null) {
                result.add(template);
            }
        }

        return result;
    }

    /**
     *
     * @param action
     * @return
     * @author s235436
     * @author s224572
     */
    private static ActionTemplate convertToTemplate(FieldAction action) {
        if (action instanceof ConveyorBelt) {
            ConveyorBelt conveyorBelt = (ConveyorBelt) action;
            ConveyorBeltTemplate conveyorBeltTemplate = new ConveyorBeltTemplate();
            conveyorBeltTemplate.heading = conveyorBelt.getHeading();
            return conveyorBeltTemplate;
        } else if (action instanceof Checkpoint) {
            Checkpoint checkpoint = (Checkpoint) action;
            CheckPointTemplate checkPointTemplate = new CheckPointTemplate();
            checkPointTemplate.number = checkpoint.getCheckpointNumber();
            checkPointTemplate.last = checkpoint.getLastCheckpoint();
            return checkPointTemplate;
        } else if (action instanceof Rotator){
            Rotator rotator = (Rotator) action;
            RotatorTemplate rotatorTemplate = new RotatorTemplate();
            rotatorTemplate.rotation = rotator.getRotation();
            return rotatorTemplate;
        } else if (action instanceof Trench){
            Trench trench = (Trench) action;
            TrenchTemplate trenchTemplate = new TrenchTemplate();
            return trenchTemplate;
        }


        return null;
    }

}
