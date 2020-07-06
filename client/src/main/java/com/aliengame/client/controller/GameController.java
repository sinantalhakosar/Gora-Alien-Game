package com.aliengame.client.controller;

import com.aliengame.client.service.GameService;
import com.aliengame.client.service.RESTService;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

class GameController {

    private GameService gameService;

    private RESTService restService;

    GameController() {
        this.gameService = new GameService();
        this.restService = new RESTService();
    }


    /**
     * This method initiliaze game scene and runs game playing method.
     * @param stage    Stage of the game
     * @param userId Id of active User
     */
    void play(Stage stage, String userId) throws IOException {
        Scene scene = new Scene(gameService.createContent());
        restService.setStatus(userId, "IN_GAME");
        gameService.playerMovements(scene, userId);
        stage.setScene(scene);
        stage.getScene().getStylesheets().add("/assets/css/main.css");
        stage.show();
    }


}
