package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.client.controller.LoginController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class LoginPane {
    private GridPane root;
    private LoginController loginController;

    /**
     * LoginPane constructor
     * @param root is the root node
     * @param loginController is the login conroller
     */
    public LoginPane(GridPane root, LoginController loginController) {
        this.root = root;
        this.loginController = loginController;
    }

    /**
     * draws the components of the login pane
     */
    public void drawLoginPane() {
        final int COLUMNS = 4;
        final int ROWS = 5;
        for (int i = 0; i < COLUMNS; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0/COLUMNS);
            root.getColumnConstraints().add(cc);
        }
        for (int i = 0; i < ROWS; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0/ROWS);
            root.getRowConstraints().add(rc);
        }

        Label riscLabel = createLabel("RISC", "RISCLabel");
        riscLabel.setAlignment(Pos.CENTER);
        root.add(riscLabel, 1,0, 2, 1);

        Label userNameLabel = createLabel("User Name:", "userNameLabel");
        root.add(userNameLabel, 1,1);
        Label passwordLabel = createLabel("Password:", "passwordLabel");
        root.add(passwordLabel, 1,2);

        TextField userNameTextField = new TextField();
        userNameTextField.setId("userNameTextField");
        root.add(userNameTextField, 2, 1);

        PasswordField passwordField = new PasswordField();
        passwordField.setId("passwordField");
        root.add(passwordField, 2, 2);

//        CheckBox checkBox = new CheckBox();
//        checkBox.setId("againstAICheckBox");
//        checkBox.setTextFill(Paint.valueOf("yellow"));

//        Label againstAILabel = createLabel("Against AI? ", "againstAILabel");
//        againstAILabel.setGraphic(checkBox);
//        againstAILabel.setContentDisplay(ContentDisplay.RIGHT);
//        root.add(againstAILabel, 1, 3);

        Button loginButton = new Button("Login");
        loginButton.setId("loginButton");
        loginButton.setOnAction(loginController::onLogin);
        root.add(loginButton, 2,3);
    }

    /**
     * convenience creator for label
     * @param text is the text
     * @param id is the id
     * @return the label
     */
    private Label createLabel(String text, String id) {
        Label l = new Label(text);
        l.setId(id);
        return l;
    }

    /**
     * return the root node of the login pane
     * @return
     */
    public GridPane getRoot() {
        return root;
    }

}