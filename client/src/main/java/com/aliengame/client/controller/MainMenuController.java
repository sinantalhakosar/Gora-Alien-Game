package com.aliengame.client.controller;

import com.aliengame.client.helper.StagePrepareHelper;
import com.aliengame.client.service.RESTService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    private StagePrepareHelper stagePrepareHelper;

    private RESTService restService;

    public MainMenuController() {
        this.stagePrepareHelper = new StagePrepareHelper();
        this.restService = new RESTService();
    }

    /**
     * This method prepares Score Board page.
     * @param event Event of button
     * @throws IOException It throws an exception if it is not load to FXML
     */
    @FXML
    protected void handleLeaderBoardButtonAction(ActionEvent event) throws IOException {
        stagePrepareHelper.prepareStage(event, URLs.FXML_SCOREBOARD.getUrl(), "");
    }

    /**
     * This method starts the game for given player id.
     * @param event Event of button
     */
    @FXML
    protected void handlePlayButtonAction(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        GameController gameController = new GameController();
        gameController.play(currentStage, currentStage.getScene().getRoot().getId());
    }

    /**
     * This method redirect to login page.
     * @param event Event of button
     * @throws IOException It throws an exception if it is not load to FXML
     */
    @FXML
    protected void handleLogoutButtonAction(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stagePrepareHelper.prepareStage(event, URLs.FXML_SIGNIN.getUrl(), "");
        restService.setStatus(currentStage.getScene().getRoot().getId(), "OFFLINE");
    }
}
