package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.client.controller.MapController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class SpecialAbilitiesPane {
    private GridPane root;
    private MapController mc;

    /**
     * constructor for SpecialAbilitiesPane
     * @param root is the root node
     * @param mc is the map controller
     */
    public SpecialAbilitiesPane(GridPane root, MapController mc) {
        this.root = root;
        root.getStylesheets().add(this.getClass().getResource("/ui/specialAbilities.css").toString());
        this.mc = mc;
    }

    /**
     * draws the special abilities pane
     */
    public void drawSpecialAbilitiesPane() {
        setConstraints();

        //Cloak Pane
        GridPane cloakPane = new GridPane();
        setSubConstraints(cloakPane);
        root.add(cloakPane, 0, 1);

        Label cloakingLabel = createLabel("Cloaking", "cloakingLabel");
        cloakPane.add(cloakingLabel, 0, 0);

        Button cloakingResearchButton = createButton("Research", "cloakingResearchButton", mc::onCloakingResearchButton);
        cloakingResearchButton.setDisable(true);
        cloakPane.add(cloakingResearchButton, 1, 0);

        Button cloakButton = createButton("Cloak", "cloakButton", mc::onCloakButton);
        cloakButton.setDisable(true);
        cloakPane.add(cloakButton, 0, 2, 2, 1);


        //TachyonicBomb Pane
        GridPane tachyonicBombPane = new GridPane();
        setSubConstraints(tachyonicBombPane);
        root.add(tachyonicBombPane, 0, 2);

        Label tachyonicBombLabel = createLabel("Tachyonic Bomb", "tachyonicBombLabel");
        tachyonicBombPane.add(tachyonicBombLabel, 0, 0);

        Button fireTachyonicBombButton = createButton("FIRE", "fireTachyonicBombButton", mc::onTachyonicBombButton);
        tachyonicBombPane.add(fireTachyonicBombButton, 0, 2, 2, 1);
        fireTachyonicBombButton.setDisable(true);

        //PsionicStorm Pane
        GridPane psionicStormPane = new GridPane();
        setSubConstraints(psionicStormPane);
        root.add(psionicStormPane, 0, 3);

        Label psionicStormLabel = createLabel("Psionic Storm", "psionicStormLabel");
        psionicStormPane.add(psionicStormLabel, 0, 0);

        Button firePsionicStormButton = createButton("FIRE", "firePsionicStormButton", mc::onPsionicStormButton);
        psionicStormPane.add(firePsionicStormButton, 0, 2, 2, 1);
        firePsionicStormButton.setDisable(true);

        //ImmortalArmada Pane
        GridPane empyreanArmadaPane = new GridPane();
        setSubConstraints(empyreanArmadaPane);
        root.add(empyreanArmadaPane, 0, 4);

        Label empyreanArmadaLabel = createLabel("Empyrean Armada", "empyreanArmadaLabel");
        empyreanArmadaPane.add(empyreanArmadaLabel, 0, 0);

        Button deployEmpyreanArmada = createButton("DEPLOY", "deployEmpyreanArmadaButton", mc::onEmpyreanArmadaButton);
        empyreanArmadaPane.add(deployEmpyreanArmada, 0, 2, 2, 1);
        deployEmpyreanArmada.setDisable(true);
    }

    /**
     * sets constraints
     */
    public void setConstraints() {
//        for (int i = 0; i < 5; i++) {
//            ColumnConstraints cc = new ColumnConstraints();
//            cc.setPercentWidth(100.0/5.0);
//            root.getColumnConstraints().add(cc);
//        }
        for (int i = 0; i < 5; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0/5.0);
            root.getRowConstraints().add(rc);
        }
    }

    /**
     * sets sub constraints for the provided gridpane
     * @param gp is the grid pane
     */
    public void setSubConstraints(GridPane gp) {
        for (int i = 0; i < 2; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(50);
            gp.getColumnConstraints().add(cc);
        }
        RowConstraints rc1 = new RowConstraints();
        RowConstraints rc2 = new RowConstraints();
        RowConstraints rc3 = new RowConstraints();
        rc1.setPercentHeight(20);
        rc2.setPercentHeight(10);
        rc3.setPercentHeight(60);
        gp.getRowConstraints().addAll(rc1, rc2, rc3);
    }

    /**
     * convenience creator for label
     * @param text is the text
     * @param id is the CSS id
     * @return the label
     */
    private Label createLabel(String text, String id) {
        Label l = new Label(text);
        l.setId(id);
        l.setAlignment(Pos.CENTER);
        return l;
    }

    /**
     * convenience creator for Button
     * @param text is the text
     * @param id is the CSS id
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
     * @return the root node of the special abilities pane
     */
    public GridPane getRoot() {
        return root;
    }
}
