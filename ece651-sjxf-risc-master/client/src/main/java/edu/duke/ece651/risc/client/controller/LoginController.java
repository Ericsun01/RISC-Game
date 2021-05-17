package edu.duke.ece651.risc.client.controller;


import edu.duke.ece651.risc.shared.LoginMessage;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import edu.duke.ece651.risc.shared.Action;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.awt.*;

public class LoginController {
    private Scene scene;
    private String userName;
    private String password;
    private LoginMessage loginMessage;
    private Stage stage;
    private boolean receInfo[];

    /**
     * Contructor for LoginController
     * @param scene is the login scene
     * @param loginMessage is the message to send to the server
     * @param stage is the overarching stage
     * @param receInfo describes whether info is received
     */
    public LoginController (Scene scene, LoginMessage loginMessage, Stage stage, boolean[] receInfo) {
        this.scene = scene;
        this.userName = "";
        this.password = "";
        this.loginMessage = loginMessage;
        this.stage = stage;
        this.receInfo = receInfo;
    }

    /**
     * gathers the user's name and password and sends to server
     * @param ae
     */
    public void onLogin (ActionEvent ae) {
        TextField userNameTextField = (TextField)scene.lookup("#userNameTextField");
        userName = userNameTextField.getText();

        TextField passwordField = (TextField) scene.lookup("#passwordField");
        password = passwordField.getText();
        //System.out.println("GUI: The password we input is "+passwordField.getText());

        loginMessage.setInformation(userName, password);

        stage.close();
    }
}
