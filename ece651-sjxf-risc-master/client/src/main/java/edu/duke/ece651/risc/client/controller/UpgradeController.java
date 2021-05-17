package edu.duke.ece651.risc.client.controller;

import edu.duke.ece651.risc.client.UpgradePane;
import edu.duke.ece651.risc.shared.*;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class UpgradeController {

    private MapController mc;
    private Scene scene;

    public UpgradeController(MapController mc) {
        this.mc = mc;
        this.scene = mc.getScene();
    }

    /**
     * Checks and upgrades max tech level
     * If there is an error, error message displayed in upgrade pane
     * @param ae
     */
    public void onUpgradeLevel(ActionEvent ae) {
        int toLevel = mc.getPlayer().getMaxTechLevel() + 1;
        MaxTechUpgradeAction mtua = new MaxTechUpgradeAction(mc.getPlayer(), toLevel);
        MaxTechUpgradeActionChecker checker = new MaxTechCanUpgradeChecker(
                new MaxTechEnoughTechResourcesChecker(
                        new NextUpgradeLevelChecker(
                                new MaxUpgradeLevelChecker(null)
                        )
                )
        );
        String error = checker.checkInteraction(mtua);
        Label upgradeErrorLabel = (Label) mc.getScene().lookup("#upgradeErrorLabel");

        if (!mc.getHasUpgradedTechLevel() && error == null) {

            mc.getRoundActions().add(mtua);

            MaxTechUpgradeCost m = new MaxTechUpgradeCost();
            mc.getPlayer().subtractTechResource(m.getCostByLevel(toLevel));

            Label techLabel = (Label) mc.getScene().lookup("#techLabel");
            techLabel.setText("Technology:\t\t" + mc.getPlayer().getTechResources());

            mc.getScene().lookup("#upgradeButton").setDisable(true);
            mc.setHasUpgradedTechLevel(!mc.getHasUpgradedTechLevel());

            upgradeErrorLabel.setText("");
        }
        else if (error != null) {
            upgradeErrorLabel.setText(error);
        }
    }

    /**
     * Once option is selected in from ComboBox, the to ComboBox is limited to only valid
     * levels to upgrade to
     * @param ae
     */
    public void onFromUpgradeLevel(ActionEvent ae) {
        ComboBox<Integer> fromComboBox = (ComboBox<Integer>) ae.getSource();
        if (fromComboBox.getValue() == null) {
            return;
        }
        int fromLevel = fromComboBox.getValue();

        ComboBox<Integer> toComboBox = (ComboBox<Integer>) mc.getScene().lookup("#toComboBox");
        int numOptions = toComboBox.getItems().size();
        for (int i = 0; i < numOptions; i++) {
            toComboBox.getItems().remove(0);
        }
        for (int i = fromLevel + 1; i < mc.getPlayer().getMaxTechLevel() + 1; i++) {
            toComboBox.getItems().add(i);
        }

        Slider upgradeSlider = (Slider) mc.getScene().lookup("#numUnitsUpgradeSlider");
        upgradeSlider.setMax(mc.getCurrSource().getNumUnitsByLevel(fromLevel));
    }

    /**
     * Updates the numUnits label when the slider is moved
     * @param me
     */
    public void onUpgradeSlider(MouseEvent me) {
        Slider s = (Slider) me.getSource();
        ((Label)mc.getScene().lookup("#numUnitsUpgradeSliderValueLabel")).setText(String.valueOf((int)s.getValue()));
    }

    /**
     * Upgrades the specified number of units based on
     * fromComboBox, toComboBox, and numUnitsUpgradeSlider.
     * If there is an error, it is printed in the errorMessageLabel.
     * @param ae
     */
    public void onConfirmUpgrade(ActionEvent ae) {
        Label upgradeErrorLabel = (Label) mc.getScene().lookup("#upgradeErrorLabel");
        UnitUpgradeActionChecker checker = new UnitEnoughTechResourceChecker(new MaxUnitLevelCanUpgradeChecker(new OnlyUplevelChecker(null)));
        UnitUpgradeCost upgradeCost = new UnitUpgradeCost();
        boolean tooManyUnits = false;

        ComboBox<Integer> fromComboBox = (ComboBox<Integer>) mc.getScene().lookup("#fromComboBox");
        ComboBox<Integer> toComboBox = (ComboBox<Integer>) mc.getScene().lookup("#toComboBox");

        if (fromComboBox.getValue() == null || toComboBox.getValue() == null) {
            upgradeErrorLabel.setText("\nPlease input a from and to level");
            return;
        }
        int fromLevel = fromComboBox.getValue();
        int toLevel = toComboBox.getValue();
        fromComboBox.valueProperty().set(null);
        toComboBox.valueProperty().set(null);

        Slider upgradeSlider = (Slider) mc.getScene().lookup("#numUnitsUpgradeSlider");
        int sliderValue = (int)upgradeSlider.getValue();

        Label sliderLabel = (Label) mc.getScene().lookup("#numUnitsUpgradeSliderValueLabel");
        upgradeSlider.setValue(0);
        sliderLabel.setText("0");

        UnitUpgradeAction upgradeAction = new UnitUpgradeAction(mc.getPlayer(), mc.getCurrSource(), fromLevel, toLevel, sliderValue);

        String error = checker.checkInteraction(upgradeAction);
        if (error != null) {
            upgradeErrorLabel.setText("\n" + error);
            return;
        } else {
            mc.getRoundActions().add(upgradeAction);
            for (int i = 0; i < upgradeAction.getNumUnits(); i++) {
                Unit u = mc.getCurrSource().tryGetUnitByLevel(fromLevel);
                if (u != null) {
                    mc.getPlayer().subtractTechResource(upgradeCost.getCostByLevel(toLevel) - upgradeCost.getCostByLevel(fromLevel));
                    u.setLevel(toLevel);
                }
            }
        }
        Label techLabel = (Label) mc.getScene().lookup("#techLabel");
        techLabel.setText("Technology:\t\t" + mc.getPlayer().getTechResources());

        toggleUpgradesPanel();
        mc.setToSelectSourcePhase();
        upgradeErrorLabel.setText("");
    }

    /**
     * promotes the lowest level (higher than 0) unit to be a spy
     * @param ae
     */
    public void onPromoteSpy(ActionEvent ae) {
        Label techLabel = (Label) mc.getScene().lookup("#techLabel");
        Label errorMessage = (Label) scene.lookup("#upgradeErrorLabel");
        if (mc.getPlayer().getTechResources() < 20) {
            errorMessage.setText("Not enough tech for a spy. 20 required");
            return;
        }
        for (Unit u: mc.getCurrSource().getUnits()) {
            if (u.getLevel() > 0) {
                mc.getCurrSource().promoteToSpy(u, mc.getPlayer());
                mc.getPlayer().subtractTechResource(20);
                techLabel.setText("Technology:\t\t" + mc.getPlayer().getTechResources());

                SpyUpgradeAction sua = new SpyUpgradeAction(mc.getPlayer(), mc.getCurrSource());
                mc.getRoundActions().add(sua);
                break;
            }
        }
    }

    /**
     * returns to the select source phase
     * @param ae
     */
    public void onCancelUpgrade(ActionEvent ae) {
        mc.setToSelectSourcePhase();
        toggleUpgradesPanel();
    }

    /**
     * Toggles user interaction for upgrade panel based on mc.getCanUpgradeUnits()
     */
    protected void toggleUpgradesPanel() {
        ComboBox<Integer> fromComboBox = (ComboBox<Integer>) mc.getScene().lookup("#fromComboBox");
        ComboBox<Integer> toComboBox = (ComboBox<Integer>) mc.getScene().lookup("#toComboBox");
        Slider upgradeSlider = (Slider) mc.getScene().lookup("#numUnitsUpgradeSlider");
        Button promoteSpyButton = (Button) mc.getScene().lookup("#promoteSpyButton");
        Button confirmUpgradeButton = (Button) mc.getScene().lookup("#confirmUpgradeButton");
        Button cancelUpgradeButton = (Button) mc.getScene().lookup("#cancelUpgradeButton");
        if (mc.getCanUpgradeUnits()) {
            fromComboBox.setDisable(true);
            toComboBox.setDisable(true);
            upgradeSlider.setDisable(true);
            promoteSpyButton.setDisable(true);
            confirmUpgradeButton.setDisable(true);
            cancelUpgradeButton.setDisable(true);
        } else {
            fromComboBox.setDisable(false);
            toComboBox.setDisable(false);
            upgradeSlider.setDisable(false);
            promoteSpyButton.setDisable(false);
            confirmUpgradeButton.setDisable(false);
            cancelUpgradeButton.setDisable(false);
        }
        mc.setCanUpgradeUnits(!mc.getCanUpgradeUnits());
    }

    /**
     * turns on the buttons in the upgrade panel
     */
    public void enableUpgradesPanel() {
        Button promoteSpyButton = (Button) mc.getScene().lookup("#promoteSpyButton");
        if (promoteSpyButton.isDisabled()) {
            toggleUpgradesPanel();
        }
    }
}
