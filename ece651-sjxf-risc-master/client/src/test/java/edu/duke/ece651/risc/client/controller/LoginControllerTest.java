package edu.duke.ece651.risc.client.controller;

import edu.duke.ece651.risc.client.App;
import edu.duke.ece651.risc.shared.*;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.TextInputControlMatchers;

@ExtendWith(ApplicationExtension.class)
public class LoginControllerTest {
    public final int HEIGHT = 1100;
    public final int WIDTH = 1800;
    Scene loginScene;

    @Start
    private void start(Stage primaryStage) {
        LoginMessage loginMessage = new LoginMessage();
        App app = new App();

        Stage stage = new Stage();

        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setResizable(false);

        loginScene = app.setLoginScene(stage, loginMessage);

        stage.show();
    }

    @Test
    public void test_login(FxRobot robot) {
        robot.clickOn("#userNameTextField");
        robot.type(KeyCode.H, KeyCode.I);
        FxAssert.verifyThat("#userNameTextField", TextInputControlMatchers.hasText("hi"));

        robot.clickOn("#passwordField");
        robot.type(KeyCode.Y, KeyCode.O);
        FxAssert.verifyThat("#passwordField", TextInputControlMatchers.hasText("yo"));

        robot.clickOn("#loginButton");
    }
}
