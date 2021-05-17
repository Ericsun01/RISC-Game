package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.client.controller.MapController;
import edu.duke.ece651.risc.shared.Map;
import edu.duke.ece651.risc.shared.Player;
import edu.duke.ece651.risc.shared.Territory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class InfoPane {
    private GridPane root;
    private Territory currTerritory;
    private int unitsToPlace;
    private Player player;
    private MapController mc;

    /**
     * Constructor for InfoPane
     * contains player info and units in territories
     * contains upgrade panel
     * @param root
     * @param mc
     * @param player
     * @param currTerritory
     * @param unitsToPlace
     */
    public InfoPane(GridPane root, MapController mc, Player player, Territory currTerritory, int unitsToPlace) {
        this.root = root;
        root.getStylesheets().add(this.getClass().getResource("/ui/info.css").toString());
        this.unitsToPlace = unitsToPlace;
        this.currTerritory = currTerritory;
        this.player = player;
        this.mc = mc;
    }

    /**
     * sets constraints and draws components of info pane
     */
    public void drawInfoPane() {
        //root.setGridLinesVisible(true);
        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0/4.0);
            root.getColumnConstraints().add(cc);
        }

        GridPane playerInfo = new GridPane();
        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(25);
            playerInfo.getColumnConstraints().add(cc);
        }

        GridPane playerPane = new GridPane();
        for (int i = 0; i < 8; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0/8.0);
            playerPane.getRowConstraints().add(rc);
        }
        Label playerName = createLabel(player.getName(), "playerNameLabel", 0,0, playerPane, 1, 2);
        playerName.setAlignment(Pos.CENTER);
        createLabel("Units to place:\t\t" + unitsToPlace, "unitsToPlaceLabel", 0, 2, playerPane);
        createLabel("Food:\t\t\t" + player.getFoodResources(), "foodLabel", 0,3, playerPane);
        createLabel("Technology:\t\t" + player.getTechResources(), "techLabel", 0, 4, playerPane);
        createLabel("Tech Level:\t\t" + player.getMaxTechLevel(), "techLevelLabel", 0, 6, playerPane);
        Button upgradeButton = createButton("Upgrade", "upgradeButton", mc::onUpgradeLevel);
        upgradeButton.setDisable(true);
        playerPane.add(upgradeButton, 0,7);
        playerPane.setAlignment(Pos.CENTER);
        playerPane.setPadding(new Insets(10, 0, 10, 0));
        root.add(playerPane, 0, 0);

        GridPane territoryPane = new GridPane();
        for (int i = 0; i < 8; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0/8.0);
            territoryPane.getRowConstraints().add(rc);
        }
        createLabel("Territory Name:\t" + currTerritory.getTerritoryName(), "territoryNameLabel", 0, 0, territoryPane);
        createLabel("Owner:\t\t\t" + currTerritory.getOwner().getName(), "ownerLabel", 0, 2, territoryPane);
        createLabel("Size:\t\t\t\t" + currTerritory.getSize(), "sizeLabel", 0, 3, territoryPane);
        createLabel("Food Production:\t" + currTerritory.getFoodProduction(), "foodProductionLabel", 0, 4, territoryPane);
        createLabel("Tech Production:\t" + currTerritory.getTechProduction(), "techProductionLabel", 0, 5, territoryPane);
        territoryPane.setAlignment(Pos.CENTER);
        territoryPane.setPadding(new Insets(10, 0, 10, 0));
        root.add(territoryPane, 1, 0);

        GridPane territoryUnitsPane = new GridPane();
        for (int i = 0; i < 8; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0/9.0);
            territoryUnitsPane.getRowConstraints().add(rc);
        }
        createLabel("Territory Units:", "territoryUnitsLabel", 2, 0, territoryUnitsPane);
        createLabel("Lvl 0:\t" + currTerritory.getNumUnits(), "lvl0Label", 2, 1, territoryUnitsPane);
        createLabel("Lvl 1:\t" + currTerritory.getNumUnits(), "lvl1Label", 2, 2, territoryUnitsPane);
        createLabel("Lvl 2:\t" + currTerritory.getNumUnits(), "lvl2Label", 2, 3, territoryUnitsPane);
        createLabel("Lvl 3:\t" + currTerritory.getNumUnits(), "lvl3Label", 2, 4, territoryUnitsPane);
        createLabel("Lvl 4:\t" + currTerritory.getNumUnits(), "lvl4Label", 2, 5, territoryUnitsPane);
        createLabel("Lvl 5:\t" + currTerritory.getNumUnits(), "lvl5Label", 2, 6, territoryUnitsPane);
        createLabel("Lvl 6:\t" + currTerritory.getNumUnits(), "lvl6Label", 2, 7, territoryUnitsPane);
        createLabel("Spies:\t" + currTerritory.getNumSpies(), "numSpyUnitLabel", 2, 8, territoryUnitsPane);
        territoryUnitsPane.setAlignment(Pos.CENTER);
        territoryUnitsPane.setPadding(new Insets(10, 0, 10, 0));
        root.add(territoryUnitsPane, 2, 0);

        UpgradePane upgradePane = new UpgradePane(new GridPane(), mc);
        upgradePane.drawUpgradePane();
        upgradePane.getRoot().setAlignment(Pos.CENTER);
        upgradePane.getRoot().setPadding(new Insets(10, 0, 10, 0));
        root.add(upgradePane.getRoot(), 3, 0);
    }

    /**
     * convenience label creator
     * @param text is the text of the label
     * @param id is the id
     * @param column is the column of root
     * @param row is the row of root
     * @param gp is the root
     * @param colspan is the colspan
     * @param rowspan is the rowspan
     * @return the label
     */
    private Label createLabel(String text, String id, int column, int row, GridPane gp, int colspan, int rowspan) {
        Label l = new Label(text);
        l.setId(id);
        gp.add(l, column, row, colspan, rowspan);
        return l;
    }

    /**
     * convenience label creator
     * @param text is the text
     * @param id is the id
     * @param column is column of root
     * @param row is row of root
     * @param gp is the root
     * @return the label
     */
    private Label createLabel(String text, String id, int column, int row, GridPane gp) {
        return createLabel(text, id, column, row, gp, 1, 1);
    }

    /**
     * convenience button
     * @param text is the text
     * @param id is the id
     * @param action is the action
     * @return the button
     */
    private Button createButton(String text, String id, EventHandler<ActionEvent> action) {
        Button b = new Button(text);
        b.setOnAction(action);
        b.setId(id);
        return b;
    }

    /**
     * return the infopane root
     * @return
     */
    public GridPane getRoot() {
        return root;
    }
}
