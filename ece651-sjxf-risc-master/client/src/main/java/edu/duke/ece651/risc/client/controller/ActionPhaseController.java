package edu.duke.ece651.risc.client.controller;

import edu.duke.ece651.risc.shared.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ActionPhaseController {
    private MapController mc;
    private Scene scene;

    /**
     * Constructor for action phase controller
     * Acts as an extension to the MapController
     * @param mc is the MapController it is associated with
     */
    public ActionPhaseController(MapController mc) {
        this.mc = mc;
        this.scene = mc.getScene();
    }

    /**
     * Sends action information to the server
     * waits for response from the server
     * updates map and player
     * @param ae
     */
    public void onCommitActionRound(ActionEvent ae) {
        toggleButtonDisable();
        System.out.println("before actionMessage");
        mc.getActionMessage().setActionList(mc.getRoundActions());
        System.out.println("Committed action round");

        RoundResult roundResult = mc.getRoundMessage().getRoundResult();
        Map newMap = roundResult.getMap();
        Player newPlayer = roundResult.getPlayer();
        mc.updateModel(newMap, newPlayer);
        mc.updateView();
    }

    /**
     * Simulates the beginning of the action phase
     * Territories become interactable and the client is asked to choose a source territory
     * @param ae
     */
    @FXML
    public void onActionButton(ActionEvent ae) {
        toggleButtonDisable();
        mc.setIsCommitted(false);

        setToSelectSourcePhase();

        mc.getRoundActions().clear();

        mc.setHasUpgradedTechLevel(false);
        scene.lookup("#upgradeButton").setDisable(false);

        mc.toggleSpecialAbilitiesPanel(true);
    }

    /**
     * Resets all buttons to their proper state for the "select source" phase
     */
    public void setToSelectSourcePhase() {
        Label phaseLabel = (Label)scene.lookup("#phaseLabel");
        phaseLabel.setText("Please choose a source territory");

        Label upgradeErrorLabel = (Label) scene.lookup("#upgradeErrorLabel");
        upgradeErrorLabel.setText("");

        Button cloakButton = (Button) scene.lookup("#cloakButton");
        cloakButton.setDisable(true);

        mc.updateFogMap(true);
        for (int i = 1; i < mc.getMap().getNumTerritories() + 1; i++) {
            Button b = (Button) scene.lookup("#t" + i);
            b.setDisable(false);
            Territory t = mc.getFogMap().getTerritoryByName(b.getId());
            //If the territory is owned by the player, it may be a source territory
            if ((mc.getPlayer().getName().equals(t.getOwner().getName()) || t.getNumSpies(mc.getPlayer()) > 0) && !t.isBomb()) {
                b.setOnAction(this::onSourceTerritory);
                mc.validTerritoryButtonCSS(b, t);
            } else {
                b.setOnAction(null);
            }
        }
    }

    /**
     * Sets currSource to the clicked territory
     * Sets other territories to be potential target territories
     * @param ae
     */
    public void onSourceTerritory(ActionEvent ae) {

        Button btn = (Button) ae.getSource();
        System.out.println("sourceTerritory");
        mc.setCurrSource(mc.getFogMap().getTerritoryByName(btn.getId()));

        Label phaseLabel = (Label)scene.lookup("#phaseLabel");
        phaseLabel.setText("Please choose a target territory");

        SamePlayerConnectionChecker spcc = new SamePlayerConnectionChecker(null);

        mc.updateFogMap(true);
        //loop through territories making the potential target territories
        for (int i = 1; i < mc.getFogMap().getNumTerritories() + 1; i++) {
            Button b = (Button) scene.lookup("#t" + i);
            Territory t = mc.getFogMap().getTerritoryByName("t" + i);
            //If button is not the source territory and (the territories are connected or neighbors) make it potential target territory
            if (b != btn && (spcc.checkMyRule(t, mc.getCurrSource()) == null || t.hasNeighbor(mc.getCurrSource())) && !t.isBomb()) {
                b.setOnAction(this::onTargetTerritory);
                mc.validTerritoryButtonCSS(b, t);
            } else {
                b.setOnAction(null);
            }
        }

        //enable upgrades
        if (currSourceIsFriendly()) {
            mc.enableUpgradePanel();
            if (mc.getPlayer().getCanCloak()) {
                scene.lookup("#cloakButton").setDisable(false);
            }

            Label upgradeTerritoryLabel = (Label) scene.lookup("#upgradeTerritoryLabel");
            upgradeTerritoryLabel.setText("Territory:\t" + mc.getCurrSource().getName());
        }
    }

    /**
     * Checks that a territory is friendly to this client
     * @param t is the territory to check
     * @return
     */
    private boolean tIsFriendly(Territory t) {
        return t.getOwner().getName().equals(mc.getPlayer().getName());
    }

    /**
     * Checks that the current source territory is friendly to this client
     * @return
     */
    private boolean currSourceIsFriendly() {
        return mc.getCurrSource().getOwner().getName().equals(mc.getPlayer().getName());
    }

    /**
     * Checks whether the current source territory has this client's spy
     * @return
     */
    private boolean currSourceHasMySpies() {
        return mc.getCurrSource().getNumSpies(mc.getPlayer()) > 0;
    }

    /**
     * Sets currTarget to the clicked territory if it makes a valid connection with currSource
     * Allows access to unit sliders, confirm, and cancel buttons
     * Disables use of territory buttons until action is confirmed or cancelled
     * @param ae
     */
    public void onTargetTerritory(ActionEvent ae) {
        Button btn = (Button) ae.getSource();
        Territory t = mc.getFogMap().getTerritoryByName(btn.getId());

        Button cloakButton = (Button) scene.lookup("#cloakButton");
        cloakButton.setDisable(true);

        SamePlayerConnectionChecker spcc = new SamePlayerConnectionChecker(null);
        boolean isValidAction = false;

        //If bombed
        if (t.isBomb()) {
            return;
        }

        //If spy only action
        if (!currSourceIsFriendly() && currSourceHasMySpies()) {
            if (t.hasNeighbor(mc.getCurrSource())) {
                mc.setCurrTarget(t);
                Slider s = (Slider) scene.lookup("#spySlider");
                s.setDisable(false);
                s.setMax(mc.getCurrSource().getNumSpies(mc.getPlayer()));
                enableConfirmAndCancel();
            }
            return;
        }

        //If attack action
        if (mc.getCurrSource().hasNeighbor(t) && !tIsFriendly(t)) {
            mc.setCurrTarget(t);
            isValidAction = true;
            System.out.println("attackAction");
        }
        //If move action
        else if (spcc.checkMyRule(t, mc.getCurrSource()) == null) {
            mc.setCurrTarget(t);
            isValidAction = true;
            System.out.println("moveAction");
        }
        if (isValidAction) {
            Label phaseLabel = (Label)scene.lookup("#phaseLabel");
            phaseLabel.setText("Please choose units to send");

            for (int i = 0; i <= Unit.MAX_LEVEL; i++) {
                Slider s = (Slider)scene.lookup("#lvl" + i + "Slider");
                s.setDisable(false);
                s.setMax(mc.getCurrSource().getNumUnitsByLevel(i));
            }
            Slider s = (Slider) scene.lookup("#spySlider");
            s.setDisable(false);
            s.setMax(mc.getCurrSource().getNumSpies(mc.getPlayer()));
            for (int i = 1; i < mc.getFogMap().getNumTerritories() + 1; i++) {
                Button b = (Button)scene.lookup("#t" + i);
                b.setDisable(true);
            }
            enableConfirmAndCancel();
            mc.toggleUpgradesPanel();
        }
    }

    /**
     * Enables the confirm and cancel buttons to be clicked
     */
    private void enableConfirmAndCancel() {
        Button confirmButton = (Button)scene.lookup("#confirmButton");
        Button cancelButton = (Button)scene.lookup("#cancelButton");
        confirmButton.setDisable(false);
        cancelButton.setDisable(false);

        confirmButton.setOnAction(this::onConfirmAction);
        cancelButton.setOnAction(this::onCancelAction);
    }

    /**
     * Checks whether the source and target territories are owned by the same player
     * @return
     */
    private boolean currSourceAndTargetSameOwner() {
        return mc.getCurrSource().getOwner().getName().equals(mc.getCurrTarget().getOwner().getName());
    }

    /**
     * Builds move or attack action based on currSource, currTarget, and slider values
     * returns view to "select source" state
     * @param ae
     */
    public void onConfirmAction(ActionEvent ae) {
        if (mc.getCurrSource().getNumUnits() == 0) {
            this.onCancelAction(ae);
            return;
        }
        //TwoTerritoryActionChecker moveChecker = new EnoughFoodResourceChecker(null);
        TwoTerritoryActionChecker attackChecker = new EnoughFoodResourceChecker(null);
        ArrayList<Unit> units = gatherSliderInfo();
        ArrayList<Spy> spies = gatherSpySliderInfo();
        Label errorLabel = (Label)scene.lookup("#errorMessageLabel");
        int foodSpentPerUnit = 0;
        int foodSpentPerSpy = 0;

        System.out.println(spies.size());

        if (!currSourceIsFriendly() && currSourceHasMySpies()) {
            //Spy move in enemy territory
            SpyMoveAction sma = new SpyMoveAction(mc.getPlayer(), mc.getCurrTarget(), mc.getCurrSource(), new ArrayList<Spy>(spies), mc.getFogMap());
            foodSpentPerSpy = 1;
            mc.getRoundActions().add(sma);
            System.out.println("confirm: spy move in enemy territory");
        } else if (currSourceAndTargetSameOwner() && currSourceIsFriendly()) {
            //Move Action
            MoveAction ma = new MoveAction(mc.getPlayer(), mc.getCurrTarget(), mc.getCurrSource(), new ArrayList<Unit>(units), mc.getFogMap());
            SpyMoveAction sma = new SpyMoveAction(mc.getPlayer(), mc.getCurrTarget(), mc.getCurrSource(), new ArrayList<Spy>(spies), mc.getFogMap());

            int min_distance = Utility.find_min_size(mc.getFogMap(), mc.getCurrSource(), mc.getCurrTarget());
            if (min_distance * (units.size() + spies.size()) > mc.getPlayer().getFoodResources()) {
                errorLabel.setText("Not enough food for move");
                return;
            }
            foodSpentPerUnit = min_distance;
            foodSpentPerSpy = min_distance;
            if (units.size() > 0) {
                mc.getRoundActions().add(ma);
            }
            if (spies.size() > 0) {
                mc.getRoundActions().add(sma);
            }
            System.out.println("confirm: spy move in friendly territory");
        } else if (currSourceIsFriendly() && !currSourceAndTargetSameOwner()) {
            //Attack Action
            AttackAction aa = new AttackAction(mc.getPlayer(), mc.getCurrTarget(), mc.getCurrSource(), new ArrayList<Unit>(units));
            SpyMoveAction sma = new SpyMoveAction(mc.getPlayer(), mc.getCurrTarget(), mc.getCurrSource(), new ArrayList<Spy>(spies), mc.getFogMap());
            if (attackChecker.checkInteraction(aa) != null) {
                errorLabel.setText("Not enough food for attack");
                return;
            }
            foodSpentPerUnit = 1;
            foodSpentPerSpy = 1;
            if (units.size() > 0) {
                mc.getRoundActions().add(aa);
            }
            if (spies.size() > 0) {
                mc.getRoundActions().add(sma);
            }
            System.out.println("confirm: spy move from friendly to enemy territory");
        }

        for (Unit u: units) {
            mc.getCurrSource().subtractUnit(u.getLevel());
            mc.getPlayer().subtractFoodResources(foodSpentPerUnit);
        }
        for (Spy s: spies) {
            mc.getCurrSource().subtractSpy(mc.getPlayer());
            mc.getPlayer().subtractFoodResources(foodSpentPerSpy);
        }

        Button btn = (Button)scene.lookup("#" + mc.getCurrSource().getName());
        btn.setText(mc.getCurrSource().getNumUnits() + "\n" + mc.getCurrSource().getName());

        Label foodLabel = (Label)scene.lookup("#foodLabel");
        int foodResource = mc.getPlayer().getFoodResources();
        foodLabel.setText("Food:\t\t\t" + foodResource);

        System.out.println("Confirmed");
        this.onCancelAction(ae);
    }

    /**
     * Returns view to "select source" state
     * @param ae
     */
    public void onCancelAction(ActionEvent ae) {
        Button confirmButton = (Button)scene.lookup("#confirmButton");
        Button cancelButton = (Button)scene.lookup("#cancelButton");
        Label errorLabel = (Label)scene.lookup("#errorMessageLabel");

        confirmButton.setDisable(true);
        cancelButton.setDisable(true);
        mc.setCurrSource(null);
        mc.setCurrTarget(null);
        errorLabel.setText("");

        Label phaseLabel = (Label)scene.lookup("#phaseLabel");
        phaseLabel.setText("Please choose a source territory");

        for (int i = 0; i <= Unit.MAX_LEVEL; i++) {
            Slider s = (Slider) scene.lookup("#lvl" + i + "Slider");
            Label l = (Label) scene.lookup("#selected" + i + "Label");
            s.setValue(0);
            l.setText("0");
            s.setDisable(true);
        }
        Slider s = (Slider) scene.lookup("#spySlider");
        Label l = (Label) scene.lookup("#selectedSpyLabel");
        l.setText("0");
        s.setValue(0);
        s.setDisable(true);

        setToSelectSourcePhase();
    }

    /**
     * Disables territory buttons on commit button click
     * Enables territory buttons on action button click
     */
    protected void toggleButtonDisable() {
        if (scene.lookup("#commit").isDisabled()) {
            for (int i = 1; i < mc.getMap().getNumTerritories() + 1; i++) {
                scene.lookup("#t" + i).setDisable(false);
            }
            scene.lookup("#commit").setDisable(false);
            scene.lookup("#action").setDisable(true);
        }
        else {
            for (int i = 1; i < mc.getMap().getNumTerritories() + 1; i++) {
                scene.lookup("#t" + i).setDisable(true);
            }
            scene.lookup("#commit").setDisable(true);
            scene.lookup("#action").setDisable(false);
        }
    }

    /**
     * Creates a list of all units specified by the sliders
     * @return ArrayList of units
     */
    protected ArrayList<Unit> gatherSliderInfo() {
        ArrayList<Unit> units = new ArrayList<>();
        for (int i = 0; i <= Unit.MAX_LEVEL; i++) {
            Slider levelSlider = (Slider)scene.lookup("#lvl" + i + "Slider");
            for (int j = 0; j < (int)Math.round(levelSlider.getValue()); j++) {
                units.add(new Unit(i));
            }
        }
        return units;
    }

    /**
     * Creates a list of all spies specified by the slider
     * @return ArrayList of spies
     */
    private ArrayList<Spy> gatherSpySliderInfo() {
        ArrayList<Spy> spies = new ArrayList<>();
        Slider spySlider = (Slider) scene.lookup("#spySlider");
        for (int i = 0; i < (int)Math.round(spySlider.getValue()); i++) {
            spies.add(new Spy(mc.getPlayer()));
        }
        return spies;
     }

    /**
     * Updates the number of units selected label for a slider
     * @param me
     */
    public void onSlide(MouseEvent me) {
        Slider s = (Slider) me.getSource();
        if (s.getId().equals("spySlider")) {
            ((Label) scene.lookup("#selectedSpyLabel")).setText(String.valueOf((int) s.getValue()));
        } else {
            int i = Integer.parseInt(s.getId().replaceAll("\\D+",""));
            ((Label) scene.lookup("#selected" + i + "Label")).setText(String.valueOf((int) s.getValue()));
        }
    }
}
