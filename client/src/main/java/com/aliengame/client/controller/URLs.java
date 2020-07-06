package com.aliengame.client.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum URLs {
    FXML_MAIN_PAGE("/fxmls/main.fxml"),
    FXML_REGISTER("/fxmls/register.fxml"),
    FXML_SCOREBOARD("/fxmls/score.fxml"),
    FXML_SIGNIN("/fxmls/signin.fxml");

    private String url;
}
