package com.aliengame.client.controller;

import com.aliengame.client.helper.AlertHelper;
import com.aliengame.client.helper.StagePrepareHelper;
import com.aliengame.client.service.RESTService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.validator.routines.EmailValidator;

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    private StagePrepareHelper stagePrepareHelper;

    private AlertHelper alertHelper;

    public RegisterController() {
        this.stagePrepareHelper = new StagePrepareHelper();
        this.alertHelper = new AlertHelper();
    }

    /**
     * This method checks the given information and save the account to database then redirect login page.
     * @param event Event of button
     */
    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        if (nameField.getText().isEmpty()) {
            alertHelper.fireAlert("! Empty Field !", "Please Enter Your Name!");
            return;
        }
        if (emailField.getText().isEmpty()) {
            alertHelper.fireAlert("! Empty Field !", "Please Enter Email Address!");
            return;
        }
        if (!isEmailValid(emailField)) {
            alertHelper.fireAlert("! Email Validation Error !", "Please Enter Valid Email Address!");
            return;
        }
        if (usernameField.getText().isEmpty()) {
            alertHelper.fireAlert("! Empty Field !", "Please Enter Your Username!");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            alertHelper.fireAlert("! Empty Field !", "Please Enter Password!");
            return;
        }

        if (passwordField.getText().length() < 5) {
            alertHelper.fireAlert("! Empty Field !", "Please Enter Password with at least 5 Characters Long!");
            return;
        }

        RESTService restService = new RESTService();
        try {
            String httpResponseBody = restService.register(nameField.getText(), surnameField.getText(), emailField.getText(), usernameField.getText(), passwordField.getText());
            if (httpResponseBody.equals("Account created successfully")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("! SUCCESS !");
                alert.setHeaderText(null);
                alert.setContentText(httpResponseBody);
                alert.showAndWait();
                stagePrepareHelper.prepareStage(event, URLs.FXML_SIGNIN.getUrl(), "");
            } else {
                alertHelper.fireAlert("! Register Fail !", httpResponseBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method checks the given email whether it is valid or not.
     * @param emailField Textfield
     * @return is entered text is in email format
     */
    private boolean isEmailValid(TextField emailField) {
        boolean allowLocal = true;
        return EmailValidator.getInstance(true).isValid(emailField.getText());
    }
}
