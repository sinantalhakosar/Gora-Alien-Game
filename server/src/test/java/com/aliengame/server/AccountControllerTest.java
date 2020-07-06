package com.aliengame.server;

import com.aliengame.server.entity.Account;
import com.aliengame.server.entity.Score;
import com.aliengame.server.entity.Status;
import com.aliengame.server.repository.AccountRepository;
import com.aliengame.server.repository.ScoreRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class AccountControllerTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ScoreRepository scoreRepository;


    @Test
    public void getAllAccounts() {

        Account testAccount1 = Account.builder()
                .name("sinan")
                .email("sinan@random")
                .password("bca321")
                .username("sinan1")
                .status(Status.valueOf("IN_LOBBY"))
                .build();

        accountRepository.save(testAccount1);

        Account testAccount2 = Account.builder()
                .name("süha")
                .email("süha@random")
                .password("cba231")
                .username("süha1")
                .status(Status.valueOf("IN_LOBBY"))
                .build();

        accountRepository.save(testAccount2);

        List<Account> accountList = Arrays.asList(testAccount1,testAccount2);

        assertNotNull(accountRepository.findAll());

    }

    @Test
    public void getAccountbyId() {

        Account testAccount3 = Account.builder()
                .email("halil@random")
                .password("qwe543")
                .username("halil__")
                .status(Status.valueOf("IN_LOBBY"))
                .build();

        accountRepository.save(testAccount3);

        assertNotNull(accountRepository.findById(testAccount3.getId()));
    }

    @Test
    public void getLeaderBoardTest() {

        Date today = new Date();
        Score score1 = Score.builder().score(10.0).date(today).build();
        Score score2 = Score.builder().score(15.0).date(today).build();
        Score score3 = Score.builder().score(20.0).date(today).build();
        Score score4 = Score.builder().score(25.0).date(today).build();

        scoreRepository.save(score1);
        scoreRepository.save(score2);
        scoreRepository.save(score3);
        scoreRepository.save(score4);

        List<Score> scoreList1 = new ArrayList<>();
        List<Score> scoreList2 = new ArrayList<>();
        scoreList1.add(score1);
        scoreList1.add(score2);
        scoreList2.add(score3);
        scoreList2.add(score4);


        Account testAccount4 = Account.builder()
                .name("süha")
                .email("süha@random")
                .password("cba231")
                .username("süha1")
                .status(Status.valueOf("IN_LOBBY"))
                .scoreList(scoreList1)
                .build();
        accountRepository.save(testAccount4);

        Account testAccount5 = Account.builder()
                .name("sinan")
                .email("sinan@random")
                .password("bca321")
                .username("sinan1")
                .status(Status.valueOf("IN_LOBBY"))
                .scoreList(scoreList2)
                .build();
        accountRepository.save(testAccount5);


        assertEquals("sinan", scoreRepository.getLeaderBoard().get(0).values().toArray()[0]);

        assertEquals(2410.0, scoreRepository.getLeaderBoard().get(0).values().toArray()[1]);

        assertEquals("clockwork", scoreRepository.getLeaderBoard().get(1).values().toArray()[0]);

        assertEquals(1631.0, scoreRepository.getLeaderBoard().get(1).values().toArray()[1]);

    }

}
