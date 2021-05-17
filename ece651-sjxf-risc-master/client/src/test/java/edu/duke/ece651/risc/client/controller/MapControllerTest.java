package edu.duke.ece651.risc.client.controller;

import edu.duke.ece651.risc.client.*;
import edu.duke.ece651.risc.shared.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.net.URL;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class MapControllerTest {
    private Map map;
    private Player player1;
    private Player player2;
    private Player player3;
    private MapController mc;
    private Scene mapScene;
    private ActionMessage actionMessage;
    private MapMessage mapMessage;
    private RoundMessage roundMessage;

    @Start
    private void start(Stage stage) {
        player1 = new Player("Bob", 1, 10, 2000, 2000);
        player2 = new Player("Sue", 2, 10, 0, 0);
        player3 = new Player("Greg", 3, 10, 0, 0);
        V2MapFactory mf = new V2MapFactory();
        map = mf.build3PlayerMap(player1, player2, player3);
        actionMessage = new ActionMessage();
        mapMessage = new MapMessage();
        roundMessage = new RoundMessage();
        App app = new App();
        mapScene = app.setMainScene(stage, map, player1, mapMessage, actionMessage, roundMessage);

        stage.show();
    }

    @Test
    public void test_onTerritoryPlacement(FxRobot robot) {

        for (int i = 0; i < 8; i++) {
            robot.clickOn("#t1");
        }
        for (int i = 0; i < 3; i++) {
            robot.clickOn("#t2");
        }

        endRound(robot, false);

        moveUnits(robot, "t1", "t2");

        upgradeUnits(robot, "t2", 0, 1);

        robot.clickOn("#t1");
        robot.clickOn("#cancelUpgradeButton");

        promoteSpy(robot, "t2", 2);
        spyMove(robot, "t2", "t3", 1);

        robot.clickOn("#upgradeButton");

        spyMove(robot, "t2", "t5", 1);

        map.getTerritoryByName("t5").addSpy(player1);
        endRound(robot, true);

        spyMove(robot, "t5", "t8", 1);
        map.getTerritoryByName("t8").addSpy(player1);
        endRound(robot, true);

        spyMove(robot, "t8", "t5", 1);
        map.getTerritoryByName("t8").subtractSpy(player1);
        endRound(robot, true);

        map.getTerritoryByName("t1").addUnit(new Unit(6));
        map.getTerritoryByName("t4").addUnit(new Unit(0));
        player1.setMaxTechLevel(4);
        endRound(robot, true);
        moveUnits(robot, "t1", "t4", 1, 6);

        tachyonicBomb(robot, "t5");
        empyreanArmada(robot, "t1");
        empyreanArmada(robot, "t7");
        psionicStorm(robot, "t9");

        robot.clickOn("#cloakingResearchButton");
        player1.setCanCloak(true);
        map.getTerritoryByName("t5").setBomb(true);
        endRound(robot, true);
        cloak(robot, "t3");
        Button t5 = (Button) mapScene.lookup("#t5");
    }

    private void moveUnits(FxRobot robot, String from, String to) {
        robot.clickOn("#" + from);

        robot.clickOn("#" + to);

        robot.drag(230, 620).dropTo(500, 620);
        robot.clickOn("#confirmButton");
    }

    private void moveUnits(FxRobot robot, String from, String to, int howMany, int lvl) {
        robot.clickOn("#" + from);

        robot.clickOn("#" + to);

        Slider s = (Slider) mapScene.lookup("#lvl" + lvl + "Slider");

        s.setValue(howMany);
        robot.clickOn("#confirmButton");
    }

    private void endRound(FxRobot robot, boolean action) {
        if (action) {
            actionMessage.setAvailable(false);
            RoundResult rr = new RoundResult(map, player1);
            roundMessage.setRoundResult(rr);
            roundMessage.setAvailable(true);
        } else {
            mapMessage.setGameMap(map);
            mapMessage.setAvailable(true);
        }

        robot.clickOn("#commit");

        robot.clickOn("#action");


        if (action) {
            roundMessage.setAvailable(false);
        } else {
            mapMessage.setAvailable(false);
        }
    }

    private void upgradeUnits(FxRobot robot, String t, int from, int to) {
        robot.clickOn("#" + t);

        ComboBox<Integer> fromComboBox = (ComboBox<Integer>) mapScene.lookup("#fromComboBox");
        ComboBox<Integer> toComboBox = (ComboBox<Integer>) mapScene.lookup("#toComboBox");
        Slider s = (Slider) mapScene.lookup("#numUnitsUpgradeSlider");

        robot.clickOn("#fromComboBox");
        for (int i = 0; i < from + 1; i++) {
            robot.type(KeyCode.DOWN);
        }
        robot.type(KeyCode.ENTER);

        robot.clickOn("#toComboBox");
        for (int i = 0; i < to - from; i++) {
            robot.type(KeyCode.DOWN);
        }
        robot.type(KeyCode.ENTER);

        robot.drag(1560, 260).dropTo(1690, 260);

        robot.clickOn("#confirmUpgradeButton");
    }

    private void upgradeUnits(FxRobot robot, String t, int from, int to, int howMany) {
        robot.clickOn("#" + t);

        ComboBox<Integer> fromComboBox = (ComboBox<Integer>) mapScene.lookup("#fromComboBox");
        ComboBox<Integer> toComboBox = (ComboBox<Integer>) mapScene.lookup("#toComboBox");
        Slider s = (Slider) mapScene.lookup("#numUnitsUpgradeSlider");

        robot.clickOn("#fromComboBox");
        for (int i = 0; i < from + 1; i++) {
            robot.type(KeyCode.DOWN);
        }
        robot.type(KeyCode.ENTER);

        robot.clickOn("#toComboBox");
        for (int i = 0; i < to - from; i++) {
            robot.type(KeyCode.DOWN);
        }
        robot.type(KeyCode.ENTER);

        s.setValue(howMany);
        robot.drag(1560, 260).dropTo(1690, 260);

        robot.clickOn("#confirmUpgradeButton");
    }

    private void promoteSpy(FxRobot robot, String t, int howMany) {
        robot.clickOn("#" + t);

        for (int i = 0; i < howMany; i++) {
            robot.clickOn("#promoteSpyButton");
        }
    }

    private void spyMove(FxRobot robot, String from, String to, int howMany) {
        robot.clickOn("#" + from);

        Slider s = (Slider) mapScene.lookup("#spySlider");

        robot.clickOn("#" + to);

        s.setValue(howMany);
        robot.clickOn("#confirmButton");
    }

    private void tachyonicBomb(FxRobot robot, String t) {
        robot.clickOn("#fireTachyonicBombButton");
        robot.clickOn("#" + t);
    }

    private void empyreanArmada(FxRobot robot, String t) {
        robot.clickOn("#deployEmpyreanArmadaButton");
        robot.clickOn("#" + t);
    }

    private void psionicStorm(FxRobot robot, String t) {
        robot.clickOn("#firePsionicStormButton");
        robot.clickOn("#" + t);
    }

    private void cloak(FxRobot robot, String t) {
        robot.clickOn("#cloakButton");
        robot.clickOn("#" + t);
    }
}
