package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.client.controller.MapController;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MapPane {
    private Group root;
    private MapController mc;

    private final int territorySize = 125;
    private final int BOTTOM_X = territorySize/2;
    private final int BOTTOM_Y = territorySize;
    private final int TOP_X = territorySize/2;
    private final int TOP_Y = 0;
    private final int RIGHT_X = territorySize;
    private final int RIGHT_Y = territorySize/2;
    private final int LEFT_X = 0;
    private final int LEFT_Y = territorySize/2;

    /**
     * Constructor for MapPane
     * @param root is the root node
     * @param mc is the MapController
     */
    public MapPane(Group root, MapController mc) {
        this.root = root;
        this.mc = mc;
    }

    /**
     * draws a 5 player map
     * @param width is the width
     * @param height is the height
     * @param territorySize is the territory size
     * @param padding is the padding
     * @param territoryAction is the event for clicking on territory
     * @param commitAction is the event for clicking on commit
     */
    public void draw15TerritoryMap(int width, int height, int territorySize, int padding, EventHandler<ActionEvent> territoryAction, EventHandler<ActionEvent> commitAction) {
        int ROW_1_Y = height/13 * 2;
        int ROW_2_Y = height/13 * 4;
        int ROW_3_Y = height/13 * 6;
        int ROW_4_Y = height/13 * 8;
        int ROW_5_Y = height/13 * 10;

        int COL_1_X = width/9 * 2;
        int COL_2_X = width/9 * 4;
        int COL_3_X = width/9 * 6;

        ArrayList<URL> cssResources = new ArrayList<>(Arrays.asList(mc.p1CssResource, mc.p2CssResource, mc.p3CssResource, mc.p4CssResource, mc.p5CssResource));
        ArrayList<Integer> rows = new ArrayList<Integer>(Arrays.asList(ROW_1_Y, ROW_2_Y, ROW_3_Y, ROW_4_Y, ROW_5_Y));
        ArrayList<Integer> cols = new ArrayList<Integer>(Arrays.asList(COL_1_X, COL_2_X, COL_3_X));

        drawTerritories(cssResources, rows, cols, territorySize, territoryAction);

        drawLines(rows, cols);

        drawButtons(width, height, padding, mc.mapCssResource, commitAction);
    }

    /**
     * draws a 4 player map
     * @param width is the width
     * @param height is the height
     * @param territorySize is the territory size
     * @param padding is the padding
     * @param territoryAction is the event for clicking on territory
     * @param commitAction is the event for clicking on commit
     */
    public void draw12TerritoryMap(int width, int height, int territorySize, int padding, EventHandler<ActionEvent> territoryAction, EventHandler<ActionEvent> commitAction) {
        int ROW_1_Y = height/12 * 2;
        int ROW_2_Y = height/12 * 4;
        int ROW_3_Y = height/12 * 6;
        int ROW_4_Y = height/12 * 8;

        int COL_1_X = width/9 * 2;
        int COL_2_X = width/9 * 4;
        int COL_3_X = width/9 * 6;

        ArrayList<URL> cssResources = new ArrayList<>(Arrays.asList(mc.p1CssResource, mc.p2CssResource, mc.p3CssResource, mc.p4CssResource));
        ArrayList<Integer> rows = new ArrayList<Integer>(Arrays.asList(ROW_1_Y, ROW_2_Y, ROW_3_Y, ROW_4_Y));
        ArrayList<Integer> cols = new ArrayList<Integer>(Arrays.asList(COL_1_X, COL_2_X, COL_3_X));

        drawTerritories(cssResources, rows, cols, territorySize, territoryAction);

        drawLines(rows, cols);

        drawButtons(width, height, padding, mc.mapCssResource, commitAction);
    }

    /**
     * draws a 3 player map
     * @param width is the width
     * @param height is the height
     * @param territorySize is the territory size
     * @param padding is the padding
     * @param territoryAction is the event for clicking on territory
     * @param commitAction is the event for clicking on commit
     */
    public void draw9TerritoryMap(int width, int height, int territorySize, int padding, EventHandler<ActionEvent> territoryAction, EventHandler<ActionEvent> commitAction) {
        int ROW_1_Y = height/9 * 2;
        int ROW_2_Y = height/9 * 4;
        int ROW_3_Y = height/9 * 6;

        int COL_1_X = width/9 * 2;
        int COL_2_X = width/9 * 4;
        int COL_3_X = width/9 * 6;

        ArrayList<URL> cssResources = new ArrayList<>(Arrays.asList(mc.p1CssResource, mc.p2CssResource, mc.p3CssResource));
        ArrayList<Integer> rows = new ArrayList<Integer>(Arrays.asList(ROW_1_Y, ROW_2_Y, ROW_3_Y));
        ArrayList<Integer> cols = new ArrayList<Integer>(Arrays.asList(COL_1_X, COL_2_X, COL_3_X));

        drawTerritories(cssResources, rows, cols, territorySize, territoryAction);

        drawLines(rows, cols);

        drawButtons(width, height, padding, mc.mapCssResource, commitAction);
    }

    /**
     * draws a 2 player map
     * @param width is the width
     * @param height is the height
     * @param territorySize is the territory size
     * @param padding is the padding
     * @param territoryAction is the event for clicking on territory
     * @param commitAction is the event for clicking on commit
     */
    public void draw6TerritoryMap(int width, int height, int territorySize, int padding, EventHandler<ActionEvent> territoryAction, EventHandler<ActionEvent> commitAction) {
        int ROW_1_Y = height/6 * 2;
        int ROW_2_Y = height/6 * 4;

        int COL_1_X = width/9 * 2;
        int COL_2_X = width/9 * 4;
        int COL_3_X = width/9 * 6;

        ArrayList<URL> cssResources = new ArrayList<>(Arrays.asList(mc.p1CssResource, mc.p2CssResource));
        ArrayList<Integer> rows = new ArrayList<Integer>(Arrays.asList(ROW_1_Y, ROW_2_Y));
        ArrayList<Integer> cols = new ArrayList<Integer>(Arrays.asList(COL_1_X, COL_2_X, COL_3_X));

        drawTerritories(cssResources, rows, cols, territorySize, territoryAction);

        drawLines(rows, cols);

        drawButtons(width, height, padding, mc.mapCssResource, commitAction);
    }

    /**
     * Convenience creator for button
     * @param text is the text
     * @param styleSheet is the stylesheet
     * @param styleClass is the styleclass
     * @param id is the style id
     * @param x is the coord x
     * @param y is the coord y
     * @param width is the width
     * @param height is the height
     * @param action is the action
     * @return the button
     */
    private Button createButton(String text, URL styleSheet, String styleClass, String id, int x, int y, int width, int height, EventHandler<ActionEvent> action) {
        Button b = new Button(text);
        b.getStylesheets().add(styleSheet.toString());
        b.getStyleClass().add(styleClass);
        b.setOnAction(action);
        b.setId(id);
        b.setLayoutX(x); b.setLayoutY(y);
        b.setMaxWidth(width); b.setMaxHeight(height);
        b.setMinWidth(width); b.setMinHeight(height);
        return b;
    }

    /**
     * creates a line
     * @param id is the style id
     * @param x0 is the coord x
     * @param y0 is the coord y
     * @param x1 is the other coord x
     * @param y1 is the other coord y
     * @param color is the line color
     * @return the line
     */
    private Line createLine(String id, int x0, int y0, int x1, int y1, Color color) {
        Line line = new Line();
        line.setStartX(x0);
        line.setStartY(y0);
        line.setEndX(x1);
        line.setEndY(y1);
        line.setStroke(color);
        line.setStrokeWidth(1);
        line.setId(id);
        return line;
    }

    /**
     * Draws the territory buttons
     * @param cssResources is the CSS
     * @param row_y is the row
     * @param col_x is the col
     * @param territorySize is the territory size
     * @param territoryAction is the action
     */
    private void drawTerritories(ArrayList<URL> cssResources, ArrayList<Integer> row_y, ArrayList<Integer> col_x, int territorySize, EventHandler<ActionEvent> territoryAction) {

        int currTerritory = 1;
        for (int i = 0; i < row_y.size(); i++) {
            for (int j = 0; j < col_x.size(); j++) {
                URL css = cssResources.get(i);
                int row = row_y.get(i);
                int col = col_x.get(j);

                Button t = createButton("0\nt" + currTerritory, css, "territory", "t" + currTerritory,
                        col, row, territorySize, territorySize,
                        territoryAction);
                mc.onTerritoryHover(t);
                root.getChildren().add(t);
                currTerritory++;
            }
        }
    }

    /**
     * draws the lines
     * @param row_y is the row
     * @param col_x is the col
     */
    private void drawLines(ArrayList<Integer> row_y, ArrayList<Integer> col_x) {

        for (int i = 0; i < col_x.size() - 1; i++) {
            for (int j = 0; j < row_y.size(); j++) {
                int y0 = row_y.get(j) + RIGHT_Y;
                int y1 = row_y.get(j) + LEFT_Y;
                int x0 = col_x.get(i) + RIGHT_X;
                int x1 = col_x.get(i+1) + LEFT_X;

                Line l = createLine("", x0, y0, x1, y1, Color.ANTIQUEWHITE);
                root.getChildren().add(l);
            }
        }

        for (int i = 0; i < col_x.size(); i++) {
            for (int j = 0; j < row_y.size() - 1; j++) {
                int y0 = row_y.get(j) + BOTTOM_Y;
                int y1 = row_y.get(j+1) + TOP_Y;
                int x0 = col_x.get(i) + BOTTOM_X;
                int x1 = col_x.get(i) +TOP_X;

                Line l = createLine("", x0, y0, x1, y1, Color.ANTIQUEWHITE);
                root.getChildren().add(l);
            }
        }
    }

    /**
     * draws the buttons
     * @param width is the width
     * @param height is the height
     * @param padding is the padding
     * @param mapCssResource is the CSS
     * @param commitAction is the action
     */
    private void drawButtons(int width, int height, int padding, URL mapCssResource, EventHandler<ActionEvent> commitAction) {
        //COMMIT BUTTON
        root.getChildren().add(createButton(
                "Commit", mapCssResource,"commit", "commit",
                width - padding - 60, height - padding - 60, 100,50,
                commitAction
        ));

        //ACTION PHASE INITIALIZER
        Button actionButton = createButton(
                "Action Phase", mapCssResource, "commit", "action",
                width - padding - 60, height - padding - 120, 100, 50,
                mc::onActionButton
        );
        actionButton.setDisable(true);
        root.getChildren().add(actionButton);
    }

    /**
     * @return the root of the map pane
     */
    public Group getRoot() {
        return root;
    }
}
