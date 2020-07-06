package com.aliengame.client;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ClientApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        Application.launch(GUIApplication.class, args);
    }

}
