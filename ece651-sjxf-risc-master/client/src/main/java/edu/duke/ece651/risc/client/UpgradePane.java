package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.client.controller.MapController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class UpgradePane {
    GridPane root;
    MapController mc;

    /**
     * constructor for the UpgradePane
     * @param root is the root node
     * @param mc is the map controller
     */
    public UpgradePane(GridPane root, MapController mc) {
        this.root = root;
        root.getStylesheets().add(this.getClass().getResource("/ui/info.css").toString());
        this.mc = mc;
    }

    /**
     * draws the upgrade pane
     */
    public void drawUpgradePane() {
        setConstraints();

        Label titleLabel = createLabel("Upgrades", "upgradeTitle", 1, 0);
        titleLabel.setAlignment(Pos.CENTER);

        Label territoryLabel = createLabel("Territory:\t", "upgradeTerritoryLabel", 1, 2);
        territoryLabel.setAlignment(Pos.CENTER_RIGHT);

        Label fromLabel = createLabel("From:  ", "fromLevelLabel", 1, 3);
        fromLabel.setAlignment(Pos.CENTER_RIGHT);
        ComboBox<Integer> fromComboBox = createComboBox("fromComboBox", 2, 3, 0, 1, 2, 3, 4, 5);
        fromComboBox.setOnAction(mc::onFromUpgradeLevel);

        Label toLabel = createLabel("To:  ", "toLevelLabel", 1, 4);
        toLabel.setAlignment(Pos.CENTER_RIGHT);
        createComboBox("toComboBox", 2, 4, 0, 1, 2, 3, 4, 5, 6);

        Label numUnits = createLabel("# ", "numUnitsUpgradeLabel", 0,5);
        numUnits.setAlignment(Pos.CENTER_RIGHT);
        createSlider("numUnitsUpgradeSlider", 50, 1, 5);
        Label sliderValueLabel = createLabel("0", "numUnitsUpgradeSliderValueLabel", 2, 5);
        sliderValueLabel.setAlignment(Pos.CENTER);

        Button promoteSpyButton = createButton("Promote Spy", "promoteSpyButton", mc::onPromoteSpy);
        root.add(promoteSpyButton, 1, 7, 2, 1);

        Button confirmUpgradeButton = createButton("Confirm", "confirmUpgradeButton", mc::onConfirmUpgrade);
        root.add(confirmUpgradeButton, 1, 8, 2, 1);

        Button cancelButton = createButton("Cancel", "cancelUpgradeButton", mc::onCancelUpgrade);
        root.add(cancelButton, 1, 9, 2, 1);

        Label errorLabel = createLabel("\n", "upgradeErrorLabel", 1, 10, 2, 1);
        errorLabel.setAlignment(Pos.TOP_CENTER);
        errorLabel.setWrapText(true);
    }

    /**
     * sets the constraints
     */
    private void setConstraints() {

        ColumnConstraints cc0 = new ColumnConstraints();
        cc0.setPercentWidth(10.0);
        root.getColumnConstraints().add(cc0);

        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setMaxWidth(200.0);
        root.getColumnConstraints().add(cc1);

        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setPercentWidth(20.0);
        root.getColumnConstraints().add(cc2);

        for (int i = 0; i < 10; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0/11.0);
            root.getRowConstraints().add(rc);
        }

    }

    /**
     * convenience label creator
     * @param text is the text
     * @param id is the id
     * @param column is the col
     * @param row is the row
     * @return the label
     */
    private Label createLabel(String text, String id, int column, int row) {
        return createLabel(text, id, column, row, 1, 1);
    }

    /**
     * creates label
     * @param text is the text
     * @param id is the id
     * @param column is the col
     * @param row is the row
     * @param colspan is the colspan
     * @param rowspan is the rowspan
     * @return the label
     */
    private Label createLabel(String text, String id, int column, int row, int colspan, int rowspan) {
        Label l = new Label(text);
        l.setId(id);
        root.add(l, column, row, colspan, rowspan);
        return l;
    }

    /**
     * slider creator
     * @param id is the id
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
        s.setOnMouseDragged(mc::onUpgradeSlider);
        s.setOnMouseClicked(mc::onUpgradeSlider);
        s.valueProperty().addListener((obs, oldval, newVal) -> s.setValue(newVal.intValue()));
        s.setDisable(true);
        root.add(s, column, row);
        return s;
    }

    /**
     * combobox creator
     * @param id is the id
     * @param column is the col
     * @param row is the row
     * @param options are the list of options
     * @return the combobox
     */
    private ComboBox<Integer> createComboBox(String id, int column, int row, int ... options) {
        ComboBox<Integer> comboBox = new ComboBox<>();
        comboBox.setId(id);
        for (int option: options) {
            comboBox.getItems().add(Integer.valueOf(option));
        }
        comboBox.setDisable(true);
        root.add(comboBox, column, row);
        return comboBox;
    }

    /**
     * button creator
     * @param text is the text
     * @param id is the id
     * @param action is the action
     * @return the Button
     */
    private Button createButton(String text, String id, EventHandler<ActionEvent> action) {
        Button b = new Button(text);
        b.setOnAction(action);
        b.setId(id);
        b.setDisable(true);
        b.setMaxSize(200, 200);
        return b;
    }

    /**
     * @return the root node of the UpgradePane
     */
    public GridPane getRoot() {
        return root;
    }
}
