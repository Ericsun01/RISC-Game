package edu.duke.ece651.risc.client.controller;

import edu.duke.ece651.risc.shared.*;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.checkerframework.checker.units.qual.A;

public class SpecialAbilitiesController {

    private MapController mc;
    private Scene scene;
    private String currSuperWeapon;

    /**
     * Constructor for SpecialAbilitiesController
     * Acts as an extension for MapController
     * @param mc is the associated MapController
     */
    public SpecialAbilitiesController(MapController mc) {
        this.mc = mc;
        this.scene = mc.getScene();
        this.currSuperWeapon = null;
    }

    /**
     * Checks that cloaking research is valid
     * if valid, adds to action list
     * @param ae
     */
    public void onCloakingResearchButton(ActionEvent ae) {
        Label upgradeErrorLabel = (Label) scene.lookup("#upgradeErrorLabel");
        if (mc.getPlayer().getTechResources() < 100) {
            upgradeErrorLabel.setText("100 tech required for research");
            return;
        }
        if (mc.getPlayer().getMaxTechLevel() < 3) {
            upgradeErrorLabel.setText("Tech level 3 or higher required for research");
            return;
        }

        Label techLabel = (Label) scene.lookup("#techLabel");
        mc.getPlayer().subtractTechResource(100);
        techLabel.setText("Technology:\t\t" + mc.getPlayer().getTechResources());

        CloakUpgradeAction cua = new CloakUpgradeAction(mc.getPlayer());
        mc.getRoundActions().add(cua);
        Button crb = (Button) ae.getSource();
        crb.setDisable(true);
    }

    /**
     * checks that cloak is valid on current source territory
     * if valid, add to action list
     * @param ae
     */
    public void onCloakButton(ActionEvent ae) {
        Label upgradeErrorLabel = (Label) scene.lookup("#upgradeErrorLabel");
        if (mc.getPlayer().getTechResources() < 20) {
            upgradeErrorLabel.setText("20 tech required for cloaking");
            return;
        }

        Label techLabel = (Label) scene.lookup("#techLabel");
        mc.getPlayer().subtractTechResource(20);
        techLabel.setText("Technology:\t\t" + mc.getPlayer().getTechResources());

        CloakAction ca = new CloakAction(mc.getPlayer(), mc.getCurrSource());
        mc.getRoundActions().add(ca);
        Button cb = (Button) ae.getSource();
        cb.setDisable(true);
        mc.onCancelUpgrade(ae);
    }

    /**
     * turns the ability buttons on or off
     * @param on is whether you want it on or off
     */
    public void toggleAbilitiesPanel(boolean on) {
        Button cloakButton = (Button) scene.lookup("#cloakButton");
        Button tachyonButton = (Button) scene.lookup("#fireTachyonicBombButton");
        Button psionicStormButton = (Button) scene.lookup("#firePsionicStormButton");
        Button empyreanArmadaButton = (Button) scene.lookup("#deployEmpyreanArmadaButton");
        if (on) {
            //cloakButton.setDisable(false);
            tachyonButton.setDisable(false);
            psionicStormButton.setDisable(false);
            empyreanArmadaButton.setDisable(false);
        } else {
            cloakButton.setDisable(true);
            tachyonButton.setDisable(true);
            psionicStormButton.setDisable(true);
            empyreanArmadaButton.setDisable(true);
        }

        if (!mc.getPlayer().getCanCloak()) {
            cloakButton.setDisable(true);
        }
    }

    /**
     * checks that a territory is friendly to this client
     * @param t is the territory
     * @return
     */
    private boolean isFriendlyTerritory(Territory t) {
        return t.getOwner().getName().equals(mc.getPlayer().getName());
    }

    /**
     * Sets psionic storm as the current weapon
     * Makes enemy territories clickable
     * @param ae
     */
    public void onPsionicStormButton(ActionEvent ae) {
        Label errorLabel = (Label) scene.lookup("#upgradeErrorLabel");
        if (mc.getPlayer().getTechResources() < PsionicStormAction.TECH_COST) {
            errorLabel.setText("Not enough tech for psionic storm"); return;
        }
        if (mc.getPlayer().getFoodResources() < PsionicStormAction.FOOD_COST) {
            errorLabel.setText("Not enough food for psionic storm"); return;
        }
        if (mc.getPlayer().getMaxTechLevel() < PsionicStormAction.REQUIRED_LEVEL) {
            errorLabel.setText("Must be at least lvl 4 for psionic storm"); return;
        }

        currSuperWeapon = "psionic storm";
        for (int i = 1; i < mc.getFogMap().getNumTerritories() + 1; i++) {
            Button btn = (Button) scene.lookup("#t" + i);
            Territory t = mc.getFogMap().getTerritoryByName("t" + i);

            if (!isFriendlyTerritory(t)) {
                btn.setOnAction(this::onSuperWeaponTarget);
                mc.validTerritoryButtonCSS(btn, t);
            } else {
                btn.setOnAction(null);
            }
        }
        toggleAbilitiesPanel(false);
    }

    /**
     * sets tachyonic bomb to current weapon
     * makes enemy territories clickable
     * @param ae
     */
    public void onTachyonicBombButton(ActionEvent ae) {
        Label errorLabel = (Label) scene.lookup("#upgradeErrorLabel");
        if (mc.getPlayer().getTechResources() < TachyonicBombAction.TECH_COST) {
            errorLabel.setText("Not enough tech for tachyonic bomb"); return;
        }
        if (mc.getPlayer().getFoodResources() < TachyonicBombAction.FOOD_COST) {
            errorLabel.setText("Not enough food for tachyonic bomb"); return;
        }
        if (mc.getPlayer().getMaxTechLevel() < TachyonicBombAction.REQUIRED_LEVEL) {
            errorLabel.setText("Must be at least lvl 4 for tachyonic bomb"); return;
        }

        currSuperWeapon = "tachyonic bomb";
        for (int i = 1; i < mc.getFogMap().getNumTerritories() + 1; i++) {
            Button btn = (Button) scene.lookup("#t" + i);
            Territory t = mc.getFogMap().getTerritoryByName("t" + i);

            if (!isFriendlyTerritory(t)) {
                btn.setOnAction(this::onSuperWeaponTarget);
                mc.validTerritoryButtonCSS(btn, t);
            } else {
                btn.setOnAction(null);
            }
        }
        toggleAbilitiesPanel(false);
    }

    /**
     * sets empyrean armada to the current superweapon
     * makes all territories clickable
     * @param ae
     */
    public void onEmpyreanArmadaButton(ActionEvent ae) {
        Label errorLabel = (Label) scene.lookup("#upgradeErrorLabel");
        if (mc.getPlayer().getTechResources() < EmpyreanArmadaAction.TECH_COST) {
            errorLabel.setText("Not enough tech for empyrean armada"); return;
        }
        if (mc.getPlayer().getFoodResources() < EmpyreanArmadaAction.FOOD_COST) {
            errorLabel.setText("Not enough food for empyrean armada"); return;
        }
        if (mc.getPlayer().getMaxTechLevel() < EmpyreanArmadaAction.REQUIRED_LEVEL) {
            errorLabel.setText("Must be at least lvl 4 for empyrean armada"); return;
        }

        currSuperWeapon = "empyrean armada";
        for (int i = 1; i < mc.getFogMap().getNumTerritories() + 1; i++) {
            Button btn = (Button) scene.lookup("#t" + i);
            Territory t = mc.getFogMap().getTerritoryByName("t" + i);
            mc.validTerritoryButtonCSS(btn, t);
            btn.setOnAction(this::onSuperWeaponTarget);
        }
        toggleAbilitiesPanel(false);
    }

    /**
     * adds action to action list based on curr super weapon
     * @param ae
     */
    public void onSuperWeaponTarget(ActionEvent ae) {
        Button btn = (Button) ae.getSource();
        Territory t = mc.getFogMap().getTerritoryByName(btn.getId());
        Label techLabel = (Label) scene.lookup("#techLabel");
        Label foodLabel = (Label) scene.lookup("#foodLabel");
        if (t.isBomb()) {
            return;
        }

        Action superAction = null;
        if (currSuperWeapon.equals("psionic storm")) {
            superAction = new PsionicStormAction(mc.getPlayer(), t);
            mc.getPlayer().subtractFoodResources(PsionicStormAction.FOOD_COST);
            mc.getPlayer().subtractTechResource(PsionicStormAction.TECH_COST);
        } else if (currSuperWeapon.equals("empyrean armada")) { //TODO: other super weapons
            superAction = new EmpyreanArmadaAction(mc.getPlayer(), t);
            mc.getPlayer().subtractTechResource(EmpyreanArmadaAction.TECH_COST);
            mc.getPlayer().subtractFoodResources(EmpyreanArmadaAction.FOOD_COST);
        } else if (currSuperWeapon.equals("tachyonic bomb")) {
            superAction = new TachyonicBombAction(mc.getPlayer(), t);
            mc.getPlayer().subtractFoodResources(TachyonicBombAction.FOOD_COST);
            mc.getPlayer().subtractTechResource(TachyonicBombAction.TECH_COST);
        }

        if (superAction != null) {
            mc.getRoundActions().add(superAction);
            techLabel.setText("Technology:\t\t" + mc.getPlayer().getTechResources());
            foodLabel.setText("Food:\t\t\t" + mc.getPlayer().getFoodResources());
        }

        toggleAbilitiesPanel(true);
        mc.setToSelectSourcePhase();
        currSuperWeapon = null;
    }
}
