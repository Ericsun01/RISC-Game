package edu.duke.ece651.risc.client.controller;

import edu.duke.ece651.risc.client.App;
import edu.duke.ece651.risc.client.MapPane;
import edu.duke.ece651.risc.shared.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class MapController2Test {

    String PATH = "file:/C:/Users/Stephen%20Hahn/IdeaProjects/ece651-sjxf-risc/client/build/resources/main/ui/";

    private Map map;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Player player5;
    private MapController mc;
    private Scene mapScene;
    private ActionMessage actionMessage;
    private MapMessage mapMessage;
    private RoundMessage roundMessage;

    @Start
    private void start(Stage stage) {
        player1 = new Player("Bob", 1, 10, 0, 0);
        player2 = new Player("Sue", 2, 10, 0, 0);
        player3 = new Player("Greg", 3, 10, 0, 0);
        player4 = new Player("gorpl", 4, 10, 0, 0);
        player5 = new Player("fribbl", 5, 10, 0, 0);
        V2MapFactory mf = new V2MapFactory();
        map = mf.build5PlayerMap(player1, player2, player3, player4, player5);

        Map testMap1 = mf.build2PlayerMap(player1, player2);
        Map testMap2 = mf.build4PlayerMap(player1, player2, player3, player4);
        MapPane mp = new MapPane(new Group(), new MapController(mapScene, map, player1));
        mp.draw6TerritoryMap(0,0,0,0,null,null);
        mp.draw12TerritoryMap(0,0,0,0,null,null);

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

        moveUnits(robot, "t1", "t2", 1, 0);
        upgradeUnits(robot, "t2", 0, 1, 1);
        robot.clickOn("#cancelUpgradeButton");
        robot.clickOn("#upgradeButton");

        tachyonicBomb(robot, "t5", false);
        psionicStorm(robot, "t5", false);
        empyreanArmada(robot, "t5", false);
        robot.clickOn("#cloakingResearchButton");

        player1.setCanCloak(true);
        endRound(robot, true);

        tachyonicBomb(robot, "t5", false);
        psionicStorm(robot, "t5", false);
        empyreanArmada(robot, "t5", false);
        robot.clickOn("#cloakButton");

        player1.setTechResources(20000);
        player1.setCanCloak(false);
        endRound(robot, true);

        tachyonicBomb(robot, "t5", false);
        psionicStorm(robot, "t5", false);
        empyreanArmada(robot, "t5", false);
        robot.clickOn("#cloakingResearchButton");

        player1.setFoodResources(20000);
        player1.setCanCloak(true);
        endRound(robot, true);

        tachyonicBomb(robot, "t5", false);
        psionicStorm(robot, "t5", false);
        empyreanArmada(robot, "t5", false);

        player1.setMaxTechLevel(4);
        endRound(robot, true);

        tachyonicBomb(robot, "t5", true);
        psionicStorm(robot, "t5", true);
        empyreanArmada(robot, "t5", true);

        moveUnits(robot, "t2", "t5", 1, 0);

        map.getTerritoryByName("t5").setBomb(true);
        endRound(robot, true);

        psionicStorm(robot, "t5", true);

        player1.setTechResources(0);
        endRound(robot, true);
        upgradeUnits(robot, "t2", 0, 4, 1);
        robot.clickOn("#cancelUpgradeButton");
        promoteSpy(robot, "t2", 1);
    }

    private void moveUnits(FxRobot robot, String from, String to, int howMany, int lvl) {
        robot.clickOn("#" + from);

        Slider s = null;
        switch (lvl) {
            case 0: s = (Slider) mapScene.lookup("#lvl0Slider"); break;
            case 1: s = (Slider) mapScene.lookup("#lvl1Slider"); break;
            case 2: s = (Slider) mapScene.lookup("#lvl2Slider"); break;
            case 3: s = (Slider) mapScene.lookup("#lvl3Slider"); break;
            case 4: s = (Slider) mapScene.lookup("#lvl4Slider"); break;
            case 5: s = (Slider) mapScene.lookup("#lvl5Slider"); break;
            case 6: s = (Slider) mapScene.lookup("#lvl6Slider"); break;
        }
        robot.clickOn("#" + to);

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

    private void upgradeUnits(FxRobot robot, String t, int from, int to, int howMany) {
        robot.clickOn("#" + t);

        ComboBox<Integer> fromComboBox = (ComboBox<Integer>) mapScene.lookup("#fromComboBox");
        ComboBox<Integer> toComboBox = (ComboBox<Integer>) mapScene.lookup("#toComboBox");
        Slider s = (Slider) mapScene.lookup("#numUnitsUpgradeSlider");

        robot.clickOn("#confirmUpgradeButton");

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
        robot.clickOn("#confirmUpgradeButton");
    }

    private void promoteSpy(FxRobot robot, String t, int howMany) {
        robot.clickOn("#" + t);

        for (int i = 0; i < howMany; i++) {
            robot.clickOn("#promoteSpyButton");
        }
    }

    private void tachyonicBomb(FxRobot robot, String t, boolean success) {
        robot.clickOn("#fireTachyonicBombButton");
        if (success) {
            robot.clickOn("#" + t);
        }
    }

    private void empyreanArmada(FxRobot robot, String t, boolean success) {
        robot.clickOn("#deployEmpyreanArmadaButton");
        if (success) {
            robot.clickOn("#" + t);
        }
    }

    private void psionicStorm(FxRobot robot, String t, boolean success) {
        robot.clickOn("#firePsionicStormButton");
        if (success) {
            robot.clickOn("#" + t);
        }
    }

}
