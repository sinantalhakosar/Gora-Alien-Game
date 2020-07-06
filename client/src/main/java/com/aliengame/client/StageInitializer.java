package com.aliengame.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<GUIApplication.StageReadyEvent> {
    @Value("classpath:/fxmls/signin.fxml")
    private Resource indexResource;
    private String applicationTitle;
    private ApplicationContext applicationContext;

    public StageInitializer(ApplicationContext applicationContext) {
        this.applicationTitle = "Space Invaders Group 13";
        this.applicationContext = applicationContext;
    }

    /**
     * This method loads Signin Page and set Scene to that page.
     *
     * @param event to get Stage
     */
    public void onApplicationEvent(GUIApplication.StageReadyEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(indexResource.getURL());
            fxmlLoader.setControllerFactory(aClass -> applicationContext.getBean(aClass));
            Parent parent = fxmlLoader.load();
            parent.getStyleClass().add("pane");
            Stage stage = event.getStage();
            stage.setScene(new Scene(parent, 600, 800, Color.BLACK));
            stage.setTitle(applicationTitle);
            stage.getScene().getStylesheets().add("/assets/css/main.css");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
