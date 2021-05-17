package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.client.controller.MapController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

import java.net.URL;

public class UnitPane {
    private GridPane root;
    private MapController mc;

    /**
     * Constructor for the UnitPane
     * @param root is the root node
     * @param mc is the mapcontroller
     */
    public UnitPane(GridPane root, MapController mc) {
        this.root = root;
        root.getStylesheets().add(this.getClass().getResource("/ui/unit.css").toString());
        this.mc = mc;
    }

    /**
     * draws the unit pane
     */
    public void drawUnitPane() {
        setConstraints();

        createLabel("Level","levelLabel", 0,1);
        createLabel("0","0Label", 0,2);
        createLabel("1","1Label", 0,3);
        createLabel("2","2Label", 0,4);
        createLabel("3","3Label", 0,5);
        createLabel("4","4Label", 0,6);
        createLabel("5","5Label", 0,7);
        createLabel("6","6Label", 0,8);
        createLabel("Spy", "spyLabel", 0, 9);

        createLabel("Placement Phase", "phaseLabel", 1,0);
        createLabel("Units to send", "unitsToSendLabel", 1, 1);
        createSlider("lvl0Slider", 50, 1, 2);
        createSlider("lvl1Slider", 50, 1, 3);
        createSlider("lvl2Slider",50, 1, 4);
        createSlider("lvl3Slider",50, 1, 5);
        createSlider("lvl4Slider",50, 1, 6);
        createSlider("lvl5Slider",50, 1, 7);
        createSlider("lvl6Slider",50, 1, 8);
        createSlider("spySlider", 50, 1, 9);

        createLabel("# Selected", "selectedLabel", 2,1);
        createLabel("0", "selected0Label", 2,2);
        createLabel("0", "selected1Label", 2,3);
        createLabel("0", "selected2Label", 2,4);
        createLabel("0", "selected3Label", 2,5);
        createLabel("0", "selected4Label", 2,6);
        createLabel("0", "selected5Label", 2,7);
        createLabel("0", "selected6Label", 2,8);
        createLabel("0", "selectedSpyLabel", 2, 9);

        GridPane bottom = new GridPane();
        for (int i = 0; i < 2; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(50);
            bottom.getColumnConstraints().add(cc);
        }
        root.add(bottom, 1, 10);

        Button confirmButton = createButton("Confirm", "confirmButton", 0,0,100, 50,null);
        Button cancelButton = createButton("Cancel", "cancelButton", 200, 0, 100, 50, null);
        confirmButton.setDisable(true);
        cancelButton.setDisable(true);

        bottom.add(confirmButton, 0,0);
        bottom.add(cancelButton,1,0);

        Label errorLabel = createLabel("", "errorMessageLabel", 1, 11);
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    /**
     * sets the constraints
     */
    private void setConstraints() {
        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setPercentWidth(20);
        root.getColumnConstraints().add(cc1);

        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setPercentWidth(60);
        root.getColumnConstraints().add(cc2);

        ColumnConstraints cc3 = new ColumnConstraints();
        cc3.setPercentWidth(20);
        root.getColumnConstraints().add(cc3);

        RowConstraints rc1 = new RowConstraints();
        rc1.setPercentHeight(20);
        root.getRowConstraints().add(rc1);

        for (int i = 0; i < 8; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(60.0/8.0);
            root.getRowConstraints().add(rc);
        }

        RowConstraints rc2 = new RowConstraints();
        rc2.setPercentHeight(10);
        root.getRowConstraints().add(rc2);

        RowConstraints rc3 = new RowConstraints();
        rc3.setPercentHeight(10);
        root.getRowConstraints().add(rc3);
    }

    /**
     * convenience label creator
     * @param text is the text
     * @param id is the CSS id
     * @param column is the col
     * @param row is the row
     * @return the label
     */
    private Label createLabel(String text, String id, int column, int row) {
        Label l = new Label(text);
        l.setId(id);
        root.add(l, column, row);
        l.setAlignment(Pos.CENTER);
        return l;
    }

    /**
     * convenience slider creator
     * @param id is the CSS id
     * @param max is the slider max value
     * @param column is the col
     * @param row is the row
     * @return the slider
     */
    private Slider createSlider(String id, int max, int column, int row) {
        Slider s = new Slider();
        s.setId(id);
        s.setMin(0);
        s.setMax(max);
        s.setOnMouseDragged(mc::onSlide);
        s.setOnMouseClicked(mc::onSlide);
        s.valueProperty().addListener((obs, oldval, newVal) -> s.setValue(newVal.intValue()));
        s.setDisable(true);
        root.add(s, column, row);
        return s;
    }

    /**
     * convenience Button creator
     * @param text is the text
     * @param id is the CSS id
     * @param x is the x
     * @param y is the y
     * @param width is the width
     * @param height is the height
     * @param action is the action
     * @return the button
     */
    private Button createButton(String text, String id, int x, int y, int width, int height, EventHandler<ActionEvent> action) {
        Button b = new Button(text);
        b.setOnAction(action);
        b.setId(id);
        b.setLayoutX(x); b.setLayoutY(y);
        b.setMaxWidth(width); b.setMaxHeight(height);
        b.setMinWidth(width); b.setMinHeight(height);
        return b;
    }

    /**
     * @return the root node of the UnitPane
     */
    public GridPane getRoot() {
        return root;
    }
}
