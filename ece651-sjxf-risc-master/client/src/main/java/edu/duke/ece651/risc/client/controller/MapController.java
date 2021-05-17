package edu.duke.ece651.risc.client.controller;
import edu.duke.ece651.risc.shared.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapController {
    //CSS resources
    public final URL p1CssResource = this.getClass().getResource("/ui/player1.css");
    public final URL p2CssResource = this.getClass().getResource("/ui/player2.css");
    public final URL p3CssResource = this.getClass().getResource("/ui/player3.css");
    public final URL p4CssResource = this.getClass().getResource("/ui/player4.css");
    public final URL p5CssResource = this.getClass().getResource("/ui/player5.css");
    public final URL p1ValidCssResource = this.getClass().getResource("/ui/player1Valid.css");
    public final URL p2ValidCssResource = this.getClass().getResource("/ui/player2Valid.css");
    public final URL p3ValidCssResource = this.getClass().getResource("/ui/player3Valid.css");
    public final URL p4ValidCssResource = this.getClass().getResource("/ui/player4Valid.css");
    public final URL p5ValidCssResource = this.getClass().getResource("/ui/player5Valid.css");
    public final URL p1fogCssResource = this.getClass().getResource("/ui/player1Fog.css");
    public final URL p2fogCssResource = this.getClass().getResource("/ui/player2Fog.css");
    public final URL p3fogCssResource = this.getClass().getResource("/ui/player3Fog.css");
    public final URL p4fogCssResource = this.getClass().getResource("/ui/player4Fog.css");
    public final URL p5fogCssResource = this.getClass().getResource("/ui/player5Fog.css");
    public final URL p1fogValidCssResource = this.getClass().getResource("/ui/player1FogValid.css");
    public final URL p2fogValidCssResource = this.getClass().getResource("/ui/player2FogValid.css");
    public final URL p3fogValidCssResource = this.getClass().getResource("/ui/player3FogValid.css");
    public final URL p4fogValidCssResource = this.getClass().getResource("/ui/player4FogValid.css");
    public final URL p5fogValidCssResource = this.getClass().getResource("/ui/player5FogValid.css");
    public final URL hiddenTerritory = this.getClass().getResource("/ui/hiddenTerritory.css");
    public final URL hiddenValidTerritory = this.getClass().getResource("/ui/hiddenValid.css");
    public final URL bombedTerritory = this.getClass().getResource("/ui/bombed.css");
    public final URL mapCssResource = this.getClass().getResource("/ui/map.css");

    private Scene scene;
    private Map map;
    private Map fogMap;
    private Player player;
    private boolean isCommitted;
    private boolean canUpgradeUnits;
    private boolean hasUpgradedTechLevel;
    private Territory currSource;
    private Territory currTarget;
    private MapMessage mapMessage;
    private ActionMessage actionMessage;
    private RoundMessage roundMessage;
    private List<Action> roundActions;

    private UpgradeController upgradeController;
    private ActionPhaseController actionPhaseController;
    private SpecialAbilitiesController specialAbilitiesController;

    /**
     * Constructor for the MapController
     * @param scene is the root node for the map view
     * @param map is the client's map model
     * @param player is the client's player model
     */
    public MapController(Scene scene, Map map, Player player) {
        this.scene = scene;
        this.map = map;
        this.fogMap = new Map(map);
        this.player = player;
        this.isCommitted = false;
        this.canUpgradeUnits = false;
        this.hasUpgradedTechLevel = true;
        currSource = null;
        currTarget = null;
        this.roundActions = new ArrayList<>();
        this.upgradeController = new UpgradeController(this);
        this.actionPhaseController = new ActionPhaseController(this);
        this.specialAbilitiesController = new SpecialAbilitiesController(this);
    }

    /**
     * Constructor including messages to send to server
     * @param scene is the root node for the map view
     * @param map is the client's map model
     * @param player is the client's player model
     * @param mapMessage holds the map information for serialization
     * @param actionMessage holds the action information for serialization
     * @param roundMessage holds round information for serialization
     */
    public MapController(Scene scene, Map map, Player player, MapMessage mapMessage, ActionMessage actionMessage, RoundMessage roundMessage) {
        this(scene, map, player);
        this.mapMessage = mapMessage;
        this.actionMessage = actionMessage;
        this.roundMessage = roundMessage;
    }

    /**
     * Changes the CSS of a valid territory to have hover and click effects
     * @param b is the button
     * @param t is the territory associated with the button
     */
    public void validTerritoryButtonCSS(Button b, Territory t) {
        if (b.getStylesheets().get(0).equals(bombedTerritory.toString())) {
            return;
        }
        if (b.getStylesheets().get(0).equals(hiddenTerritory.toString()) ||
                b.getStylesheets().get(0).equals(hiddenValidTerritory.toString())) {
            removeAllCSS(b);
            b.getStylesheets().add(hiddenValidTerritory.toString());
            return;
        }
        if (b.getStylesheets().get(0).contains("Fog")) {
            validFogButtonCSS(b, t);
            return;
        }
        removeAllCSS(b);
        switch (t.getOwner().getPlayerNumber()) {
            case 1: b.getStylesheets().add(p1ValidCssResource.toString()); break;
            case 2: b.getStylesheets().add(p2ValidCssResource.toString()); break;
            case 3: b.getStylesheets().add(p3ValidCssResource.toString()); break;
            case 4: b.getStylesheets().add(p4ValidCssResource.toString()); break;
            case 5: b.getStylesheets().add(p5ValidCssResource.toString()); break;
        }
    }

    /**
     * Changes the CSS for valid buttons in fog
     * @param b is the button
     * @param t is the territory associated with the button
     */
    public void validFogButtonCSS(Button b, Territory t) {
        removeAllCSS(b);
        switch (t.getOwner().getPlayerNumber()) {
            case 1: b.getStylesheets().add(p1fogValidCssResource.toString()); break;
            case 2: b.getStylesheets().add(p2fogValidCssResource.toString()); break;
            case 3: b.getStylesheets().add(p3fogValidCssResource.toString()); break;
            case 4: b.getStylesheets().add(p4fogValidCssResource.toString()); break;
            case 5: b.getStylesheets().add(p5fogValidCssResource.toString()); break;
        }
    }

    /**
     * Changes CSS for visible, unclickable territories
     * @param b is the button
     * @param t is the territory associated with the button
     */
    public void territoryButtonCSS(Button b, Territory t) {
        removeAllCSS(b);
        switch (t.getOwner().getPlayerNumber()) {
            case 1: b.getStylesheets().add(p1CssResource.toString()); break;
            case 2: b.getStylesheets().add(p2CssResource.toString()); break;
            case 3: b.getStylesheets().add(p3CssResource.toString()); break;
            case 4: b.getStylesheets().add(p4CssResource.toString()); break;
            case 5: b.getStylesheets().add(p5CssResource.toString()); break;
        }
    }

    /**
     * Changes CSS for fogged, unclickable territories
     * @param b is the button
     * @param t is the territory associated with the button
     */
    public void fogTerritoryButtonCSS(Button b, Territory t) {
        removeAllCSS(b);
        switch (t.getOwner().getPlayerNumber()) {
            case 1: b.getStylesheets().add(p1fogCssResource.toString()); break;
            case 2: b.getStylesheets().add(p2fogCssResource.toString()); break;
            case 3: b.getStylesheets().add(p3fogCssResource.toString()); break;
            case 4: b.getStylesheets().add(p4fogCssResource.toString()); break;
            case 5: b.getStylesheets().add(p5fogCssResource.toString()); break;
        }
    }

    /**
     * Initialize the CSS for the the client view of the map
     */
    public void initializeFogMap() {
        for (int i = 1; i < fogMap.getNumTerritories() + 1; i++) {
            Button btn = (Button) scene.lookup("#t" + i);

            Territory t = fogMap.getTerritoryByName("t" + i);
            if (!t.getOwner().getName().equals(player.getName()) && !t.isNextTo(player)) {
                removeAllCSS(btn);
                btn.getStylesheets().add(hiddenTerritory.toString());
                t.setHidden(true);
            }
        }
    }

    /**
     * Sets the labels in the InfoPane to display info about
     * the territory that is currently being hovered over
     * @param b is the button that is being hovered over
     */
    public void onTerritoryHover(Button b) {
        b.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue &&
                    !b.getStylesheets().get(0).equals(hiddenTerritory.toString()) &&
                    !b.getStylesheets().get(0).equals(hiddenValidTerritory.toString())) {
                Territory t = fogMap.getTerritoryByName(b.getId());

                Label territoryNameLabel = (Label)scene.lookup("#territoryNameLabel");
                territoryNameLabel.setText("Territory Name:\t" + t.getTerritoryName());

                Label ownerLabel = (Label)scene.lookup("#ownerLabel");
                ownerLabel.setText("Owner:\t\t\t" + t.getOwner().getName());

                Label sizeLabel = (Label)scene.lookup("#sizeLabel");
                sizeLabel.setText("Size:\t\t\t\t" + t.getSize());

                Label foodProductionLabel = (Label)scene.lookup("#foodProductionLabel");
                foodProductionLabel.setText("Food Production:\t" + t.getFoodProduction());

                Label techProductionLabel = (Label)scene.lookup("#techProductionLabel");
                techProductionLabel.setText("Tech Production:\t" + t.getTechProduction());

                Label level0Label = (Label)scene.lookup("#lvl0Label");
                Label level1Label = (Label)scene.lookup("#lvl1Label");
                Label level2Label = (Label)scene.lookup("#lvl2Label");
                Label level3Label = (Label)scene.lookup("#lvl3Label");
                Label level4Label = (Label)scene.lookup("#lvl4Label");
                Label level5Label = (Label)scene.lookup("#lvl5Label");
                Label level6Label = (Label)scene.lookup("#lvl6Label");
                Label spyLabel = (Label) scene.lookup("#numSpyUnitLabel");
                level0Label.setText("Lvl 0:\t" + t.getNumUnitsByLevel(0));
                level1Label.setText("Lvl 1:\t" + t.getNumUnitsByLevel(1));
                level2Label.setText("Lvl 2:\t" + t.getNumUnitsByLevel(2));
                level3Label.setText("Lvl 3:\t" + t.getNumUnitsByLevel(3));
                level4Label.setText("Lvl 4:\t" + t.getNumUnitsByLevel(4));
                level5Label.setText("Lvl 5:\t" + t.getNumUnitsByLevel(5));
                level6Label.setText("Lvl 6:\t" + t.getNumUnitsByLevel(6));
                spyLabel.setText("Spies:\t" + t.getNumSpies(player));
            }
        });
    }

    /**
     * Places a single level 0 unit on the territory clicked
     * and updates the button's text to reflect the number of units in the territory
     * @param ae
     */
    @FXML
    public void onTerritoryPlacement(ActionEvent ae) {
        Button btn = (Button) ae.getSource();
        Territory t = map.getTerritoryByName(btn.getId());
        if (player.getName().equals(t.getOwner().getName()) && player.getUnitsToPlace() > 0) {
            t.addUnit(new Unit(0));
            btn.setText(t.getNumUnits() + "\n" + t.getTerritoryName());
            player.decrementUnitsToPlace();

            Label unitsToPlaceLabel = (Label)scene.lookup("#unitsToPlaceLabel");
            unitsToPlaceLabel.setText("Units to place:\t\t" + player.getUnitsToPlace());
        }
    }

    /**
     * Creates the list of placement actions based on territories' units
     * Disables use of the territory buttons
     * @param ae
     */
    @FXML
    public void onCommit(ActionEvent ae) {
        if (!isCommitted) {
            Button btn = (Button) ae.getSource();
            List<Action> placements = new ArrayList<>();
            for (Iterator<Territory> it = map.getTerritories(); it.hasNext(); ) {
                Territory t = it.next();
                if (player.getName().equals(t.getOwner().getName())) {
                    PlacementAction pa = new PlacementAction(t.getOwner(), t, t.getUnits());
                    placements.add(pa);
                }

            }
            actionMessage.setActionList(placements);
            System.out.println("Committed");
            isCommitted = true;

            updateModelMap(mapMessage.getGameMap());
            updateView();
            toggleButtonDisable();
            scene.lookup("#cloakingResearchButton").setDisable(false);
            btn.setOnAction(this::onCommitActionRound);
        }
    }

    /**
     * updates the model's map
     * @param map
     */
    protected void updateModelMap(Map map) {
        this.map = map;
    }

    /**
     * updates the model's player
     * @param player
     */
    protected void updateModelPlayer(Player player) {
        this.player = player;
    }

    /**
     * updates the model's player and map
     * @param map
     * @param player
     */
    public void updateModel(Map map, Player player) {
        updateModelMap(map);
        updateModelPlayer(player);
    }

    /**
     * Updates the client's view of the map based on unit and spy location
     * @param onlyCSS
     */
    public void updateFogMap(boolean onlyCSS) {
        for (int i = 1; i < fogMap.getNumTerritories() + 1; i++) {
            Button btn = (Button) scene.lookup("#t" + i);
            Territory t = fogMap.getTerritoryByName(btn.getId());
            Territory tServer = map.getTerritoryByName(btn.getId());
            t.setBomb(tServer.isBomb());
            if (tServer.getCloaked() && !tServer.getOwner().getName().equals(player.getName()) && !hasSpyIn(tServer)) {
                removeAllCSS(btn);
                btn.getStylesheets().add(hiddenTerritory.toString());
            } else if (tServer.getOwner().getName().equals(player.getName()) || tServer.isNextTo(player) || hasSpyIn(tServer)) {
                if (!onlyCSS) {
                    t.setUnits(tServer.getUnits());
                    t.setSpies(tServer.getSpies());
                    t.setOwner(tServer.getOwner());
                    btn.setText(t.getNumUnits() + "\n" + t.getName());
                }
                t.setHidden(false);

                if (t.isBomb()) {
                    removeAllCSS(btn);
                    btn.getStylesheets().add(bombedTerritory.toString());
                } else {
                    territoryButtonCSS(btn, t);
                }
            } else if (!t.isHidden()) {
                fogTerritoryButtonCSS(btn, t);
            }
        }
    }

    /**
     * Updates fog map
     */
    public void updateFogMap() {
        updateFogMap(false);
    }

    /**
     * Checks that the client has a spy in the given territory
     * @param t is the territory
     * @return
     */
    private boolean hasSpyIn(Territory t) {
        for (Spy s: t.getSpies()) {
            if (s.getOwner().getName().equals(player.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * updates the client's view player info based on the current model
     */
    protected void updateViewPlayerInfo() {
        Label unitsToPlaceLabel = (Label)scene.lookup("#unitsToPlaceLabel");
        Label foodLabel = (Label)scene.lookup("#foodLabel");
        Label techLabel = (Label)scene.lookup("#techLabel");
        Label techLevelLabel = (Label)scene.lookup("#techLevelLabel");

        unitsToPlaceLabel.setText("Units to place:\t\t" + player.getUnitsToPlace());
        foodLabel.setText("Food:\t\t\t" + player.getFoodResources());
        techLabel.setText("Technology:\t\t" + player.getTechResources());
        techLevelLabel.setText("Tech Level:\t\t" + player.getMaxTechLevel());
    }

    /**
     * updates the client's view based on the current model
     */
    public void updateView() {
        updateFogMap();
        updateViewPlayerInfo();
    }

    /**
     * removes all the CSS from a button
     * @param b is the button
     */
    public void removeAllCSS(Button b) {
        if (b == null) {
            return;
        }
        while (b.getStylesheets().size() > 0) {
            b.getStylesheets().remove(0);
        }
    }

    /**
     * Commits an action round to the server and waits for response
     * @param ae
     */
    public void onCommitActionRound(ActionEvent ae) {
        actionPhaseController.onCommitActionRound(ae);
    }

    /**
     * Simulates the beginning of the action phase
     * Territories become interactable and the client is asked to choose a source territory
     * @param ae
     */
    @FXML
    public void onActionButton(ActionEvent ae) {
        actionPhaseController.onActionButton(ae);
    }

    protected void setToSelectSourcePhase() {
        actionPhaseController.setToSelectSourcePhase();
    }

    /**
     * Updates the number of units selected label for a slider
     * @param me
     */
    public void onSlide(MouseEvent me) {
        actionPhaseController.onSlide(me);
    }

    /**
     * Disables territory buttons on commit button click
     * Enables territory buttons on action button click
     */
    protected void toggleButtonDisable() {
        actionPhaseController.toggleButtonDisable();
    }

    public void enableUpgradePanel() { upgradeController.enableUpgradesPanel(); }

    public void onPromoteSpy(ActionEvent ae) { upgradeController.onPromoteSpy(ae); }

    public void onCancelUpgrade(ActionEvent ae) { upgradeController.onCancelUpgrade(ae); }

    public void onUpgradeLevel(ActionEvent ae) { upgradeController.onUpgradeLevel(ae); }

    public void onFromUpgradeLevel(ActionEvent ae) { upgradeController.onFromUpgradeLevel(ae); }

    public void onUpgradeSlider(MouseEvent me) { upgradeController.onUpgradeSlider(me); }

    public void onConfirmUpgrade(ActionEvent ae) { upgradeController.onConfirmUpgrade(ae); }

    protected void toggleUpgradesPanel() { upgradeController.toggleUpgradesPanel(); }

    public void onCloakingResearchButton(ActionEvent ae) { specialAbilitiesController.onCloakingResearchButton(ae); }

    public void toggleSpecialAbilitiesPanel(boolean on) { specialAbilitiesController.toggleAbilitiesPanel(on); }

    public void onCloakButton(ActionEvent ae) { specialAbilitiesController.onCloakButton(ae); }

    public void onPsionicStormButton(ActionEvent ae) { specialAbilitiesController.onPsionicStormButton(ae); }

    public void onEmpyreanArmadaButton(ActionEvent ae) { specialAbilitiesController.onEmpyreanArmadaButton(ae); }

    public void onTachyonicBombButton(ActionEvent ae) { specialAbilitiesController.onTachyonicBombButton(ae); }

    public Scene getScene() { return scene; }

    public Map getMap() { return map; }

    public Map getFogMap() { return fogMap; }

    public Player getPlayer() { return player; }

    public boolean getCanUpgradeUnits() { return canUpgradeUnits; }

    public boolean getHasUpgradedTechLevel() { return hasUpgradedTechLevel; }

    public Territory getCurrSource() { return currSource; }

    public Territory getCurrTarget() { return currTarget; }

    public ActionMessage getActionMessage() { return actionMessage; }

    public RoundMessage getRoundMessage() { return roundMessage; }

    public List<Action> getRoundActions() { return roundActions; }

    public void setIsCommitted(boolean committed) { isCommitted = committed; }

    public void setCanUpgradeUnits(boolean canUpgradeUnits) { this.canUpgradeUnits = canUpgradeUnits; }

    public void setHasUpgradedTechLevel(boolean hasUpgradedTechLevel) { this.hasUpgradedTechLevel = hasUpgradedTechLevel; }

    public void setCurrSource(Territory currSource) { this.currSource = currSource; }

    public void setCurrTarget(Territory currTarget) { this.currTarget = currTarget; }
}

