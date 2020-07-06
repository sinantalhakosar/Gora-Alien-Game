package com.aliengame.client;

import com.aliengame.client.entity.ScoreRecord;
import com.aliengame.client.service.RESTService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RESTServiceControllerTests {
    RESTService restService = new RESTService();

    @Test
    public void registerWithoutEmailTest() {
        String output;
        output = restService.register("taha", "utku", "", "tahautku1", "tahautku1");
        assertEquals(output, "Please Provide All Necessary Information");
    }


    @Test
    public void registerWithoutUserNameTest() {
        String output;
        output = restService.register("taha", "utku", "tahautku@random.com", "", "utkutaha1");
        assertEquals(output, "Please Provide All Necessary Information");
    }

    @Test
    public void registerAlreadyExistingUserTest() {
        String output = restService.register("denemedeneme", "densur", "densur@den.com", "denden1", "denden1");

        assertEquals(output, "Username Already Exists");
    }

    @Test
    public void signInWithoutPasswordTest() {

        String output = restService.signin("denden1", "");

        assertEquals(output, "Username or password are wrong.");

    }

    @Test
    public void signInWithoutUserNameTest() {
        String output = restService.signin("", "densur");

        assertEquals(output, "Username or password are wrong.");

    }

    @Test
    public void signInWithNotExistingUserNameTest() {
        String output = restService.signin("abcdenemexyz", "hellohello");
        assertEquals(output, "Username or password are wrong.");

    }

    @Test
    public void signInWithNotExistingPasswordTest() {
        String output = restService.signin("hellohello", "abcdenemexyz");
        assertEquals(output, "Username or password are wrong.");

    }

    @Test
    public void returnWeeklyScoreListTest() throws IOException {
        List<ScoreRecord> scoreRecordList;
        scoreRecordList = restService.getTimeDomainedScores("getWeeklyScores");
        assertFalse(scoreRecordList.isEmpty());

    }

    @Test
    public void returnAllScoreListTest() throws IOException {
        List<ScoreRecord> scoreRecordList;
        scoreRecordList = restService.getTimeDomainedScores("getAllScores");
        assertFalse(scoreRecordList.isEmpty());

    }
}
