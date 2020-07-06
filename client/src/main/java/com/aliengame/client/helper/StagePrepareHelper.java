package com.aliengame.client.helper;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StagePrepareHelper {

    public void prepareStage(ActionEvent actionEvent, String fxmlUrl, String userId) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent mainMenu = FXMLLoader.load(getClass().getResource(fxmlUrl));
        if ("".equals(userId)) {
            mainMenu.setId(stage.getScene().getRoot().getId());
        } else {
            mainMenu.setId(userId);
        }
        mainMenu.getStyleClass().add("pane");
        Scene scene = new Scene(mainMenu, 600, 800);
        stage.setScene(scene);
        stage.getScene().getStylesheets().add("/assets/css/main.css");
        stage.show();
    }
}
