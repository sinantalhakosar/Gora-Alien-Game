package com.aliengame.client.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RESTURLs {
    REST_URL("http://144.122.71.144:8083/"),
    REGISTER("server_program13/api/account/register"),
    SIGNIN("server_program13/api/account/login"),
    SCORE("server_program13/api/score/"),
    ADDSCORE("server_program13/api/score/addScore/"),
    GETSCORE("server_program13/api/score/getScore/"),
    STATUS("server_program13/api/account/status/"),
    MATCH("server_program13/api/account/match/");

    private String url;
}
