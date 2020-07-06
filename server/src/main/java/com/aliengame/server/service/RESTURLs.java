package com.aliengame.server.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RESTURLs {
    REST_URL("http://144.122.71.144:8083/"),
    REGISTER("server_program13/api/account/register"),
    SIGNIN("server_program13/api/account/login"),
    ADDSCORE("server_program13/api/score/addScore/"),
    SCORE("server_program13/api/score/");

    private String url;
}
