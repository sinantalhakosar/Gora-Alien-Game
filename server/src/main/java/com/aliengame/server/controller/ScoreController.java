package com.aliengame.server.controller;

import com.aliengame.server.entity.Account;
import com.aliengame.server.entity.Score;
import com.aliengame.server.service.AccountService;
import com.aliengame.server.service.ScoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("server_program13/api/score")
@RestController
public class ScoreController {

    private final ScoreService scoreService;

    private final AccountService accountService;

    public ScoreController(ScoreService scoreService, AccountService accountService) {
        this.scoreService = scoreService;
        this.accountService = accountService;
    }

    /**
     * Catches the score addition requests to given id
     *
     * @param id    account id to add score
     * @param score score amount
     * @param level last level of player
     * @return ResponseEntity
     */
    @RequestMapping(value = "/addScore/{id}/{score}/{level}", method = RequestMethod.POST)
    public ResponseEntity<String> addScore(@PathVariable long id, @PathVariable double score, @PathVariable int level) {
        Account userExists = accountService.findUserByID(id);
        if (userExists != null) {
            scoreService.addScore(userExists, score, level);
            return ResponseEntity.status(HttpStatus.OK).body("Score added successfully to " + userExists.getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    /**
     * Catches the get score requests to given id
     *
     * @param id score owner account id
     * @return ResponseEntity
     */
    @RequestMapping(value = "/getScore/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getScore(@PathVariable long id) {
        Account userExists = accountService.findUserByID(id);
        String sent = "";
        if (userExists != null) {
            List<Score> scores = scoreService.getScore(userExists.getUsername());
            Date max = Collections.max(scores.stream().map(Score::getDate).collect(Collectors.toList()));
            for (Score score : scores) {
                if (score.getDate() == max) {
                    sent = String.valueOf(score.getScore());
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(sent);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    /**
     * Catches all requests to return all scores
     *
     * @return ResponseEntity
     */
    @RequestMapping(value = "/getAllScores", method = RequestMethod.GET)
    public ResponseEntity<String> getAllScores() {
        try {
            scoreService.getScores();
            return ResponseEntity.status(HttpStatus.OK).body("All scores got successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error happened, while getting all scores");
        }
    }

    /**
     * Catches the requests for weekly scores
     * @return ResponseEntity
     */
    @RequestMapping(value = "/getWeeklyScores", method = RequestMethod.GET)
    public ResponseEntity<List> getWeeklyScores() {
        try {
            List<Map<String, String>> weeklyScores = scoreService.getWeeklyScores();
            return ResponseEntity.status(HttpStatus.OK).body(weeklyScores);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList());
        }
    }

    /**
     * Catches the requests for monthly scores
     * @return ResponseEntity
     */
    @RequestMapping(value = "/getMonthlyScores", method = RequestMethod.GET)
    public ResponseEntity<List> getMonthlyScores() {
        try {
            List<Map<String, String>> monthlyScores = scoreService.getMonthlyScores();
            return ResponseEntity.status(HttpStatus.OK).body(monthlyScores);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList());
        }
    }
}
