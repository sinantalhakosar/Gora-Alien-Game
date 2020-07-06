package com.aliengame.client.entity;

import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameObject extends Rectangle {
    private String playerID;
    private int health;
    private int score;
    private boolean dead = false;
    private int name;
    private int type;
    private String source;

    /**
     * Constructor for game objects.
     *
     * @param x      X coordinate
     * @param y      Y coordinate
     * @param w      Width
     * @param h      Height
     * @param name   Player, Enemy or Bullet
     * @param type   Specified version of name attribute
     * @param health Objects health
     * @param score  Objects score when it get hit
     */
    public GameObject(int x, int y, int w, int h, int name, int type, int health, int score) {
        super(w, h);
        this.name = name;
        this.type = type;
        this.health = health;
        this.score = score;
        setTranslateX(x);
        setTranslateY(y);
    }

    /**
     * Object Left Move
     */
    public void moveLeft() {
        setTranslateX(getTranslateX() - 5);
    }

    /**
     * Object Right Move
     */
    public void moveRight() {
        setTranslateX(getTranslateX() + 5);
    }

    /**
     * Object Up Move
     */
    public void moveUp() {
        setTranslateY(getTranslateY() - 5);
    }

    /**
     * Object Down Move
     */
    public void moveDown() {
        setTranslateY(getTranslateY() + 5);
    }

    /**
     * Object Moves with Mouse
     */
    public void mouseMove(double xCoordinate) {
        setTranslateX(xCoordinate);
    }

    /**
     * Player Score Addition
     *
     * @param addition addition score amount
     */
    public void scoreUp(int addition) {
        this.score += addition;
    }

    /**
     * Object's health setter
     *
     * @param health set health amount
     */
    public void setHealth(int health) {
        this.health = health;
    }
}
