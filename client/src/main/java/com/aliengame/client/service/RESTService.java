package com.aliengame.client.service;

import com.aliengame.client.entity.ScoreRecord;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RESTService {
    /**
     * This method connects the rest service and post request to save the account.
     *
     * @param name     name of user
     * @param surname  surname of user
     * @param email    email of user
     * @param username username of user
     * @param password pass of user
     * @return response body
     */
    public String register(String name, String surname, String email, String username, String password) {
        try {
            String registerUrl = RESTURLs.REST_URL.getUrl().concat(RESTURLs.REGISTER.getUrl());
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(registerUrl);
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("surname", surname);
            json.put("email", email);
            json.put("username", username);
            json.put("password", password);
            StringEntity entity = new StringEntity(json.toString());
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            String output = EntityUtils.toString(response.getEntity());
            client.close();
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method connects the rest service and post request to login the account.
     *
     * @param username username of account
     * @param password pass of account
     * @return
     */
    public String signin(String username, String password) {
        try {
            String loginUrl = RESTURLs.REST_URL.getUrl().concat(RESTURLs.SIGNIN.getUrl());
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(loginUrl);
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", password);
            StringEntity entity = new StringEntity(json.toString());
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            String output = EntityUtils.toString(response.getEntity());
            client.close();
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method takes game score and user information then post to the server
     *
     * @param userId scores's owner player id
     * @param score  player's score
     * @param level  player's last played level
     * @return whether operation is successfull or not
     * @throws IOException for executing the request
     */
    boolean saveScore(String userId, int score, int level) throws IOException {
        String scoreUrl = RESTURLs.REST_URL.getUrl().concat(RESTURLs.ADDSCORE.getUrl() + userId + "/" + score + "/" + level);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(scoreUrl);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        String output = EntityUtils.toString(response.getEntity());
        client.close();
        return output.contains("Score added successfully");
    }

    /**
     * This method gets the related scores from server
     *
     * @param timedomain requested time domain: Weekly or Monthly
     * @return Score records in given time domain
     * @throws IOException for executing the request
     */
    public List<ScoreRecord> getTimeDomainedScores(String timedomain) throws IOException {
        String scoreTimeDomainUrl = RESTURLs.REST_URL.getUrl().concat(RESTURLs.SCORE.getUrl() + timedomain);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(scoreTimeDomainUrl);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpGet);
        String output = EntityUtils.toString(response.getEntity());
        client.close();
        List<ScoreRecord> scoreRecordList = new ArrayList<>();
        String[] split = output.replace("[", "").replace("]", "").replace("{", "").split("},");
        float score;
        String username;
        for (String str : split) {
            str = str.replace("}", "");
            try {
                score = Float.parseFloat(str.split(",")[0].split(":")[1]);
                username = str.split(",")[1].split(":")[1].replace("\"", "");
                scoreRecordList.add(new ScoreRecord(username, score));
            } catch (Exception e) {
                //e.printStackTrace();
                score = Float.parseFloat(str.split(",")[1].split(":")[1]);
                username = str.split(",")[0].split(":")[1].replace("\"", "");
                scoreRecordList.add(new ScoreRecord(username, score));
            }
        }
        return scoreRecordList;
    }

    /**
     * Set user status on every mode (different screens) to db
     * @param userId id of account
     * @param status status of account
     * @throws IOException from httpclient
     */
    public void setStatus(String userId, String status) throws IOException {
        String statusUrl = RESTURLs.REST_URL.getUrl().concat(RESTURLs.STATUS.getUrl().concat(userId + "/" + status));
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(statusUrl);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpPut);
        String output = EntityUtils.toString(response.getEntity());
        client.close();
    }

    /**
     * Gets the requested user's score
     * @param id id of user
     * @return latest user score as string
     * @throws IOException from httpclient
     */
    public String getScore(String id) throws IOException {
        String statusUrl = RESTURLs.REST_URL.getUrl().concat(RESTURLs.GETSCORE.getUrl() + id);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(statusUrl);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpGet);
        String output = EntityUtils.toString(response.getEntity());
        client.close();
        return output;
    }
}