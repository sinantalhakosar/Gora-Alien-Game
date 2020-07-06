package com.aliengame.server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    OFFLINE("OFFLINE"),
    IN_LOBBY("IN_LOBBY"),
    IN_GAME("IN_GAME"),
    WAITING("WAITING");


    private String status;
}
