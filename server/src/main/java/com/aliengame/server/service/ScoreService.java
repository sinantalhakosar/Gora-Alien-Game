package com.aliengame.server.service;

import com.aliengame.server.entity.Account;
import com.aliengame.server.entity.Score;
import com.aliengame.server.repository.AccountRepository;
import com.aliengame.server.repository.ScoreRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;

    private final AccountRepository accountRepository;

    public ScoreService(ScoreRepository scoreRepository, AccountRepository accountRepository) {
        this.scoreRepository = scoreRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Score addition to database
     *
     * @param account score owner
     * @param score   score amount
     * @param level   last level of owner
     */
    public void addScore(Account account, double score, int level) {
        if (account != null) {
            Score newScore = Score.builder().date(new Date()).score(score).lastLevel(level).build();
            scoreRepository.save(newScore);
            List<Score> scoreList = account.getScoreList();
            scoreList.add(newScore);
            account.setScoreList(scoreList);
            accountRepository.save(account);
        }
    }

    /**
     * Brings the given account score
     *
     * @param username score owner account's username
     * @return all scores of account
     */
    public List<Score> getScore(String username) {
        Account byUsername = accountRepository.findByUsername(username);
        if (byUsername != null) {
            return byUsername.getScoreList();
        }
        return null;
    }

    /**
     * Get all scores in DB
     *
     * @return all scores in a list
     */
    public List<Map<String, String>> getScores() {
        return scoreRepository.getLeaderBoard();
    }

    /**
     * Gets all scores in last 7 days
     * @return list of scores
     */
    public List<Map<String, String>> getWeeklyScores() {
        return scoreRepository.getLeaderBoardWeekly();
    }

    /**
     * Gets all scores in last 30 days
     * @return list of scores
     */
    public List<Map<String, String>> getMonthlyScores() {
        return scoreRepository.getLeaderBoardMonthly();
    }
}
