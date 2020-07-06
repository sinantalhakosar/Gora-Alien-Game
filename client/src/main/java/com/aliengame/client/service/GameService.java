package com.aliengame.client.service;

import com.aliengame.client.entity.GameObject;
import com.aliengame.client.entity.InformationCircle;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameService {

    private final Pane rootPane;
    private final Pane gamePane;
    private final Pane informationPane;

    private final GameObject player;
    private final GameObject player2;
    private String playerId;
    private final InformationCircle healthCircle;
    private final InformationCircle scoreCircle;
    private final InformationCircle levelCircle;
    private final InformationCircle resultCircle;

    private double time = 0;
    private int level;
    private int deadEnemy;
    private boolean isGameOver;
    private final List<KeyCode> codes;

    private final Image plekumatImg = new Image("assets/images/plekumat.png");
    private final Image tihuluImg = new Image("assets/images/tihulu.png");
    private final Image kunaImg = new Image("assets/images/kuna.png");
    private final Image amirtocaImg = new Image("assets/images/amirtoca.png");
    private final Image logarImg = new Image("assets/images/logar.png");

    private final Image fire = new Image("assets/images/fire.png");
    private final Image water = new Image("assets/images/water.png");
    private final Image rock = new Image("assets/images/rock.png");
    private final Image wood = new Image("assets/images/wood.png");

    private final List<Image> shoots = Arrays.asList(fire, water, rock, wood);

    private final RESTService restService;

    private static final String SERVER_IP = "144.122.71.144";
    private static final int SERVER_PORT = 8081;

    private DataInputStream dis;
    private DataOutputStream dos;

    {
        try {
            InetAddress ip = InetAddress.getByName(SERVER_IP);
            Socket s = new Socket(ip, SERVER_PORT);
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double bossShootCounter = 0, otherScore = 0;
    private String otherPlayerId;
    private final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            update();
        }
    };

    /**
     * Generation of objects that are in game
     */
    public GameService() {
        this.level = 1;
        this.isGameOver = false;
        this.rootPane = new Pane();
        this.gamePane = new Pane();
        this.informationPane = new Pane();
        this.player = new GameObject(300, 650, 75, 75, 0, 0, 10, 0);

        Image sinanImg = new Image("assets/images/shipA.png");
        this.player.setFill(new ImagePattern(sinanImg));

        this.player2 = new GameObject(450, 650, 75, 75, 4, 0, 10, 0);


        this.healthCircle = new InformationCircle(25, 20, 40, "Health: " + this.player.getHealth());
        this.healthCircle.setCache(true);
        this.healthCircle.setCacheHint(CacheHint.SPEED);
        this.scoreCircle = new InformationCircle(275, 20, 40, "Score: " + this.player.getScore());
        this.scoreCircle.setCache(true);
        this.scoreCircle.setCacheHint(CacheHint.SPEED);
        this.levelCircle = new InformationCircle(500, 20, 40, "Level: " + this.level);
        this.levelCircle.setCache(true);
        this.levelCircle.setCacheHint(CacheHint.SPEED);
        this.resultCircle = new InformationCircle(250, 300, 60, "Game Result");
        this.resultCircle.setCache(true);
        this.resultCircle.setCacheHint(CacheHint.SPEED);
        Button mainMenuButton = new Button("Return Main Menu");
        mainMenuButton.setOnAction(new EventHandler<>() {
            /**
             * Provides to go main menu after game ends
             *
             * @param event button event
             */
            @Override
            public void handle(ActionEvent event) {
                rootPane.getChildren().clear();
            }
        });
        this.deadEnemy = 0;
        this.restService = new RESTService();
        this.codes = new ArrayList<>();

    }

    /**
     * Creates content in the scene
     * @return root of contents
     */
    public Parent createContent() {
        rootPane.setPrefSize(600, 800);
        rootPane.getStyleClass().add("pane");
        gamePane.getChildren().add(player);
        informationPane.getChildren().addAll(healthCircle, scoreCircle, levelCircle);

        animationTimer.start();
        if (this.isGameOver) {
            animationTimer.stop();
            return rootPane;
        }
        createEnemies();

        rootPane.getChildren().addAll(gamePane, informationPane);
        return rootPane;
    }

    /**
     * Generates bullet object for given shooter objects
     * @param shooter Actor who shoots
     */
    private void shoot(GameObject shooter) {
        GameObject shoot = new GameObject((int) shooter.getTranslateX() + 37, (int) shooter.getTranslateY(), 5, 20, 2, shooter.getName() + 5, 1, 1);
        if (shooter.getName() == 0 && this.level < 5) {
            shoot.setFill(new ImagePattern(shoots.get(this.level - 1)));
        }
        shoot.setCache(true);
        shoot.setCacheHint(CacheHint.SPEED);
        gamePane.getChildren().add(shoot);
    }

    /**
     * Boss object shooter
     * @param boss
     */
    public void bossShoot(GameObject boss) {

        if (bossShootCounter == 2) {
            bossShootCounter = 0;
        }
        for (int i = 0; i < bossShootCounter + 3; i++) {
            GameObject bShoot = new GameObject((int) (boss.getTranslateX() + (150 * i) / (bossShootCounter + 2)), (int) boss.getTranslateY(), 5, 20, 4, 8, 1, 1);
            bShoot.setCache(true);
            bShoot.setCacheHint(CacheHint.SPEED);
            gamePane.getChildren().add(bShoot);
        }
        bossShootCounter++;

    }

    /**
     * boss object mover
     * @param boss
     */
    public void bossMove(GameObject boss) {
        Random r = new Random();
        int result = r.nextInt(4);
        double newloc = 0.0;
        switch (result) {
            case 0: // 2 right
                newloc = boss.getTranslateX() + 2;
                break;
            case 1: // 2 left
                newloc = boss.getTranslateX() - 2;
                break;
            case 2: // 4 right
                newloc = boss.getTranslateX() + 4;
                break;
            case 3: // 4 left
                newloc = boss.getTranslateX() - 4;
                break;
            default:
        }
        if (newloc > 0 && newloc < 600) {
            boss.setTranslateX(newloc);
        }
    }

    /**
     * Creates enemy objects depending on current level
     */
    private void createEnemies() {
        GameObject enemy, boss;
        if (this.level == 5) {
            boss = new GameObject(275, 150, 150, 150, 3, 7, 20, 10);
            boss.setCache(true);
            boss.setCacheHint(CacheHint.SPEED);
            boss.setFill(new ImagePattern(logarImg));
            gamePane.getChildren().add(boss);
        } else {
            for (int i = 0; i < 5; i++) {
                if (this.level == 1) {
                    enemy = new GameObject(25 + i * 125, 150, 70, 70, 1, 1, 1, 2);
                    enemy.setCache(true);
                    enemy.setCacheHint(CacheHint.SPEED);
                    enemy.setFill(new ImagePattern(plekumatImg));
                    gamePane.getChildren().add(enemy);
                }
                if (this.level == 2 || this.level == 3) {
                    enemy = new GameObject(25 + i * 125, 150, 70, 70, 1, 2, 2, 4);
                    enemy.setCache(true);
                    enemy.setCacheHint(CacheHint.SPEED);
                    enemy.setFill(new ImagePattern(tihuluImg));
                    gamePane.getChildren().add(enemy);
                }
                if (this.level >= 3 && i != 4) {
                    if (this.level == 3) {
                        enemy = new GameObject(75 + i * 125, 250, 70, 70, 1, 3, 3, 6);
                    } else {
                        enemy = new GameObject(75 + i * 125, 150, 70, 70, 1, 3, 3, 6);
                    }
                    enemy.setCache(true);
                    enemy.setCacheHint(CacheHint.SPEED);
                    enemy.setFill(new ImagePattern(kunaImg));
                    gamePane.getChildren().add(enemy);
                }
                if (this.level == 4) {
                    enemy = new GameObject(25 + i * 125, 250, 75, 75, 1, 4, 4, 8);
                    enemy.setCache(true);
                    enemy.setCacheHint(CacheHint.SPEED);
                    enemy.setFill(new ImagePattern(amirtocaImg));
                    gamePane.getChildren().add(enemy);
                }
            }
        }

    }

    /**
     * Puts game objects in a list
     * @return list of game objects
     */
    private List<GameObject> gameObjects() {
        return gamePane.getChildren().stream().map(n -> (GameObject) n).collect(Collectors.toList());
    }

    /**
     * Updates all game objects in an interval and controls the game flow (hit, score up, die etc.)
     */
    private void update() {
        gameObjects().forEach(o -> {
            switch (o.getType()) {
                case 6:
                    o.moveDown();
                    if (o.getBoundsInParent().intersects(player.getBoundsInParent())) {
                        if (player.getHealth() > 1) {
                            player.setHealth(player.getHealth() - 1);
                            healthCircle.updateInformation("Health: " + player.getHealth());
                        } else {
                            player.setDead(true);
                            gameFinisher("Game Over\nScore:");
                            return;
                        }
                        o.setDead(true);
                    }
                    break;
                case 5:
                    o.moveUp();
                    Stream<GameObject> enemies = gameObjects().stream().filter(e -> e.getName() == 1);
                    Stream<GameObject> bosses = gameObjects().stream().filter(e -> e.getName() == 3);
                    Stream<GameObject> enemybullets = gameObjects().stream().filter(e -> e.getType() == 6);
                    Stream<GameObject> bossbullets = gameObjects().stream().filter(e -> e.getType() == 8);
                    enemies.forEach(enemy -> {
                        if (o.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                            player.scoreUp(enemy.getScore());
                            if (enemy.getHealth() > 1) {
                                enemy.setHealth(enemy.getHealth() - 1);
                            } else {
                                enemy.setDead(true);
                                this.deadEnemy += 1;
                                if (this.deadEnemy == -1 * this.level / 3 + 5 * ((this.level / 3) + 1)) {
                                    this.level += 1;
                                    levelCircle.updateInformation("Level: " + this.level);
                                    this.deadEnemy = 0;
                                    createEnemies();
                                }
                                if (this.level == 5) {
                                    try {
                                        level5Prepare();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    return;
                                }
                            }
                            o.setDead(true);
                        }
                    });

                    bosses.forEach(boss -> {
                        if (o.getBoundsInParent().intersects(boss.getBoundsInParent())) {
                            player.scoreUp(boss.getScore());
                            if (boss.getHealth() > 1) {
                                boss.setHealth(boss.getHealth() - 1);
                            } else {
                                boss.setDead(true);
                                this.deadEnemy += 1;
                                if (this.deadEnemy == 1) {
                                    this.level += 1;
                                    gameFinisher("Winner:");
                                }
                            }
                            o.setDead(true);
                        }
                    });

                    enemybullets.forEach(enemybullet -> {
                        if (o.getBoundsInParent().intersects(enemybullet.getBoundsInParent())) {
                            player.scoreUp(enemybullet.getScore());
                            o.setDead(true);
                            enemybullet.setDead(true);
                        }
                    });
                    bossbullets.forEach(bossbullet -> {
                        if (o.getBoundsInParent().intersects(bossbullet.getBoundsInParent())) {
                            player.scoreUp(bossbullet.getScore());
                            o.setDead(true);
                            bossbullet.setDead(true);
                        }
                    });
                    scoreCircle.updateInformation("Score: " + player.getScore());
                    break;
                case 8:
                    o.moveDown();
                    if (o.getBoundsInParent().intersects(player.getBoundsInParent())) {
                        if (player.getHealth() > 1) {
                            player.setHealth(player.getHealth() - 1);
                        } else {
                            player.setDead(true);
                            gameFinisher("Game Over\nScore:");
                            return;
                        }
                        o.setDead(true);
                    }
                    if (o.getBoundsInParent().intersects(player2.getBoundsInParent())) {
                        if (player2.getHealth() > 1) {
                            player2.setHealth(player2.getHealth() - 1);
                        } else {
                            player2.setDead(true);
                            gameFinisher("Game Over\nScore:");
                            return;
                        }
                        o.setDead(true);
                    }
                    break;
                case 9:
                    o.moveUp();
                    Stream<GameObject> bosses_o = gameObjects().stream().filter(e -> e.getName() == 3);
                    Stream<GameObject> bossbullets_o = gameObjects().stream().filter(e -> e.getType() == 8);
                    bossbullets_o.forEach(bossbullet -> {
                        if (o.getBoundsInParent().intersects(bossbullet.getBoundsInParent())) {
                            o.setDead(true);
                            bossbullet.setDead(true);
                        }
                    });
                    bosses_o.forEach(boss -> {
                        if (o.getBoundsInParent().intersects(boss.getBoundsInParent())) {
                            if (boss.getHealth() > 1) {
                                boss.setHealth(boss.getHealth() - 1);
                            } else {
                                boss.setDead(true);
                                this.deadEnemy += 1;
                                if (this.deadEnemy == 1) {
                                    this.level += 1;
                                    gameFinisher("Winner:");
                                }
                            }
                            o.setDead(true);
                        }
                    });
                default:
                    if (o.getType() == 0) {
                        time += 0.04;
                    } else {
                        time += 0.01;
                    }
                    if (time > 2) {
                        if (Math.random() < 0.3) {
                            if (o.getName() == 1) {
                                shoot(o);
                            } else if (o.getName() == 3) {
                                bossShoot(o);
                                bossMove(o);
                            }
                            time = 0;
                        }
                    }
                    break;
            }
        });
        gamePane.getChildren().removeIf(n -> {
            GameObject gameObject = (GameObject) n;
            return gameObject.isDead() || gameObject.getTranslateX() > 600 || gameObject.getTranslateY() > 800;
        });


    }


    /**
     * Generates player movements according to user input
     * @param scene current scene
     * @param userId current player
     */
    public void playerMovements(Scene scene, String userId) {
        this.playerId = userId;
        this.player.setPlayerID(userId);
        Thread receiver = new Thread(() -> {
            while (true) {
                try {
                    String receive = dis.readUTF();
                    if (receive.equals("shoot")) {
                        Platform.runLater(() -> shoot(player2));
                    } else if (receive.equals("start")) {
                        dos.writeUTF("user-" + userId);
                        TimeUnit.SECONDS.sleep(3);
                        Platform.runLater(() -> {
                            animationTimer.start();
                            alert.close();
                        });

                    } else if (receive.contains("user")) {
                        if (!(this.playerId.equals((receive.split("-")[1])))) {
                            otherPlayerId = String.valueOf(Integer.valueOf(receive.split("-")[1]));
                            this.player2.setPlayerID(otherPlayerId);
                            levelCircle.updateInformation("P2 ID:" + otherPlayerId);
                        }
                    } else if (receive.contains("score")) {
                        otherScore = Double.parseDouble(receive.split("-")[1]);
                    } else {
                        String[] coordinateAndUserId = receive.split("-");
                        System.out.println("x:" + coordinateAndUserId[0]);
                        System.out.println("y:" + coordinateAndUserId[1]);
                        player2.setTranslateX(Double.parseDouble(coordinateAndUserId[0]));
                        player2.setTranslateY(Double.parseDouble(coordinateAndUserId[1]));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        receiver.start();

        EventHandler<MouseEvent> mouseHandler = mouseEvent -> {
            double sceneX = mouseEvent.getSceneX();
            if (sceneX < 600 && sceneX > 0) {
                player.mouseMove(mouseEvent.getSceneX());
                try {
                    dos.writeUTF(Double.toString(player.getTranslateX()).concat("-").concat(Double.toString(player.getTranslateY())));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        };
        //scene.setOnMouseDragged(mouseHandler);
        scene.setOnMouseMoved(mouseHandler);

        scene.setOnKeyPressed(e -> {
            if (this.level > 4) {
                Thread sender = new Thread(() -> {
                    String sent;
                    if (this.level == 5) {
                        if (e.getCode() == KeyCode.SPACE) {
                            sent = "shoot";
                        } else {
                            sent = Double.toString(player.getTranslateX()).concat("-").concat(Double.toString(player.getTranslateY()));
                            System.out.println("sent:" + sent);
                        }
                    } else {
                        sent = "score-" + this.player.getScore();
                    }
                    try {
                        dos.writeUTF(sent);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                sender.start();
            }
            switch (e.getCode()) {
                case A:
                    player.moveLeft();
                    break;
                case D:
                    player.moveRight();
                    break;
                case W:
                    player.moveUp();
                    break;
                case S:
                    player.moveDown();
                    break;
                case SPACE:
                    shoot(player);
                    break;
                default:
                    codes.add(e.getCode());
                    if (isCheatActivated()) {
                        this.level += 1;
                        if (this.level == 5) {
                            try {
                                level5Prepare();
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        } else {
                            levelCircle.updateInformation("Level: " + this.level);
                            this.deadEnemy = 0;
                            gameObjects().stream().filter(en -> en.getName() == 1).forEach(enemy -> enemy.setDead(true));
                            gameObjects().stream().filter(en -> en.getName() == 2).forEach(enemy -> enemy.setDead(true));
                            createEnemies();
                        }
                    }
            }
        });

        scene.setOnKeyReleased((event) -> this.codes.remove(event.getCode()));
    }

    /**
     * prepare screen and service to level 5
     * @throws InterruptedException
     */
    private void level5Prepare() throws InterruptedException {

        gameObjects().stream().filter(e -> e.getName() == 1).forEach(enemy -> enemy.setDead(true));
        gameObjects().stream().filter(e -> e.getName() == 2).forEach(bullet -> bullet.setDead(true));
        TimeUnit.SECONDS.sleep(2);
        animationTimer.stop();
        String message = "level5";
        try {
            dos.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.player.setHealth(10);
        this.player2.setHealth(10);
        Image suhaImg = new Image("assets/images/shipB.png");
        this.player2.setFill(new ImagePattern(suhaImg));
        healthCircle.updateInformation("P1 ID:" + this.playerId);
        levelCircle.updateInformation("P2 ID:" + this.otherPlayerId);
        scoreCircle.updateInformation("Score:" + this.player.getScore());
        gamePane.getChildren().add(this.player2);

        alert.setTitle("! Matching !");
        alert.setHeaderText(null);
        alert.setContentText("Matching with another player... Game will automatically start in 3 seconds after matched !!!");
        alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
        alert.show();
        this.deadEnemy = 0;
        createEnemies();
    }

    /**
     * This methods runs at the end of the game shows the result to user
     *
     * @param resultMessage string message depending on the end type (game over or accomplish)
     */
    private void gameFinisher(String resultMessage) {

        rootPane.getChildren().clear();
        String winnerId = "", winnerScore = "", totalScore = "";
        try {
            if (!this.isGameOver) {
                //restService.saveScore(this.playerId, this.player.getScore(), this.level - 1);
                this.isGameOver = true;
            }
            restService.setStatus(this.playerId, "IN_LOBBY");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (this.level == 6) {
            String message = "score-" + this.player.getScore();

            try {
                dos.writeUTF(message);
                TimeUnit.SECONDS.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.player2.setScore((int) this.otherScore);
            if (this.player.getScore() > this.player2.getScore()) {
                this.player.setScore(this.player.getScore() + 30);
                winnerId = this.playerId;
                winnerScore = String.valueOf(this.player.getScore());
            } else if (this.player2.getScore() > this.player.getScore()) {
                this.player2.setScore(this.player2.getScore() + 30);
                winnerId = otherPlayerId;
                winnerScore = String.valueOf(this.player2.getScore());
            }
            try {
                restService.saveScore(this.playerId, this.player.getScore(), this.level - 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            totalScore = String.valueOf(this.player.getScore() + this.player2.getScore());

        } else {
            resultCircle.updateInformation(resultMessage + this.player.getScore());
        }


        animationTimer.stop();
        alert.setTitle("Total Score:" + totalScore + ", Your Score:" + this.player.getScore());
        alert.setHeaderText(null);
        alert.setContentText("Winner is " + winnerId + " with " + winnerScore + " score");
        alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
        alert.setOnHidden(evt -> Platform.exit());
        alert.show();
    }

    /**
     * Checks whether correct combination of cheat keys pressed
     * It checks CMD + SHIFT + DIGIT9 for MACOS
     * It checks CTRL + SHIFT + DIGIT9 for OTHER
     *
     * @return is cheat activated
     */
    private boolean isCheatActivated() {
        List<KeyCode> validKeyCodes_MAC = new ArrayList<>();
        List<KeyCode> validKeyCodes = new ArrayList<>();
        validKeyCodes.add(KeyCode.CONTROL);
        validKeyCodes.add(KeyCode.SHIFT);
        validKeyCodes.add(KeyCode.DIGIT9);
        Collections.sort(validKeyCodes);
        validKeyCodes_MAC.add(KeyCode.COMMAND);
        validKeyCodes_MAC.add(KeyCode.SHIFT);
        validKeyCodes_MAC.add(KeyCode.DIGIT9);
        Collections.sort(validKeyCodes_MAC);
        Collections.sort(this.codes);
        return validKeyCodes.equals(this.codes) || validKeyCodes_MAC.equals(this.codes);
    }


}
