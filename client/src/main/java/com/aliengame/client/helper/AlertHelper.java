package com.aliengame.client.helper;

import javafx.scene.control.Alert;

public class AlertHelper {

    public void fireAlert(String header, String body) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(header);
        alert.setHeaderText(null);
        alert.setContentText(body);
        alert.showAndWait();
    }
}
