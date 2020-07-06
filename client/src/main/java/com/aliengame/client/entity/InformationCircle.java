package com.aliengame.client.entity;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.Random;


public class InformationCircle extends StackPane {

    private Text innerText;
    private Circle circle;

    /**
     * Information circle objects constructor
     *
     * @param x               X Coordinate
     * @param y               Y Coordinate
     * @param r               Radius of circles
     * @param informationText Information Text inside the circle
     */
    public InformationCircle(int x, int y, int r, String informationText) {
        super();
        this.circle = new Circle(x, y + 10, r, Color.BLACK);
        this.innerText = new Text(x, y + 10, informationText);
        innerText.setFill(Color.WHITE);
        this.getChildren().addAll(circle, innerText);
        this.setLayoutX(x);
        this.setLayoutY(y);
    }

    /**
     * Information circle setter
     *
     * @param newInformation new Information string
     */
    public void updateInformation(String newInformation) {
        this.innerText.setText(newInformation);
    }

    public void updateColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        this.circle.setFill(Color.color(r, g, b));
    }
}
