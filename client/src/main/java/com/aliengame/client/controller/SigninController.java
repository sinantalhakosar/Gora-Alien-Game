package com.aliengame.client.controller;

import com.aliengame.client.helper.AlertHelper;
import com.aliengame.client.helper.StagePrepareHelper;
import com.aliengame.client.service.RESTService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SigninController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private StagePrepareHelper stagePrepareHelper;
    private AlertHelper alertHelper;


    public SigninController() {
        this.stagePrepareHelper = new StagePrepareHelper();
        this.alertHelper = new AlertHelper();
    }

    /**
     * This method gets the user information and login user if it is okay, then prepares Main Menu page.
     * @param event Event of button
     * @throws IOException It throws an exception if it is not load to FXML
     */
    @FXML
    protected void handleSignInButtonAction(ActionEvent event) throws IOException {
        if (usernameField.getText().isEmpty()) {
            alertHelper.fireAlert("! Empty Field !", "Please Enter Username!");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            alertHelper.fireAlert("! Empty Field !", "Please Enter Password!");
            return;
        }


        RESTService restService = new RESTService();
        String httpResponseBody = restService.signin(usernameField.getText(), passwordField.getText());
        if (!httpResponseBody.equals("Username or password are wrong.")) {
            stagePrepareHelper.prepareStage(event, URLs.FXML_MAIN_PAGE.getUrl(), httpResponseBody);
        } else {
            alertHelper.fireAlert("! Login Fail !", httpResponseBody);
        }
    }

    /**
     * This method prepares the Registratin Form page.
     * @param event Event of button
     * @throws IOException It throws an exception if it is not load to FXML
     */
    @FXML
    protected void handleRegisterButtonAction(ActionEvent event) throws IOException {
        stagePrepareHelper.prepareStage(event, URLs.FXML_REGISTER.getUrl(), "");
    }
}
