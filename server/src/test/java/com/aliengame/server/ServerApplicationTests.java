package com.aliengame.server;

import com.aliengame.server.entity.Account;
import com.aliengame.server.entity.Score;
import com.aliengame.server.entity.Status;
import com.aliengame.server.repository.AccountRepository;
import com.aliengame.server.repository.ScoreRepository;
import com.aliengame.server.service.RESTURLs;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


/**
 * TO RUN THE TESTS CORRECTLY, THE SERVER SHOULD BE RUNNING,
 * AND SHOULD BE RESTARTED.
 *
 ***********************************************************
 *********************RESTART THE SERVER********************
 ***********************************************************
 */

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServerApplicationTests {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private Account account1;

    private Score score1;

    private static final String REST_URL = RESTURLs.REST_URL.getUrl();

    @Test
    public void AlreadyExistUserNameRegisterTest() throws Exception{

        String registerUrl = REST_URL.concat(RESTURLs.REGISTER.getUrl());
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(registerUrl);
        JSONObject json = new JSONObject();
        json.put("name","barbi");
        json.put("surname","barbi");
        json.put("email", "barbi@barbi.com");
        json.put("username", "barbi");
        json.put("password", "barbi");
        StringEntity entity = new StringEntity(json.toString());
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        String output = EntityUtils.toString(response.getEntity());

        assertEquals(output,"Username Already Exists");
    }

    @Test
    public void WithoutPasswordUserNameRegisterTest() throws Exception{

        String registerUrl = REST_URL.concat(RESTURLs.REGISTER.getUrl());
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(registerUrl);
        JSONObject json = new JSONObject();
        json.put("name","subzero");
        json.put("surname","subzero");
        json.put("email", "subzero@subzero.com");
        json.put("username", JSONObject.NULL);
        json.put("password", JSONObject.NULL);
        StringEntity entity = new StringEntity(json.toString());
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        String output = EntityUtils.toString(response.getEntity());

        assertEquals(output,"Please Provide All Necessary Information");
    }

    @Test
    public void WithoutEmailRegisterTest() throws Exception{

        String registerUrl = REST_URL.concat(RESTURLs.REGISTER.getUrl());
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(registerUrl);
        JSONObject json = new JSONObject();
        json.put("name","patrick");
        json.put("surname","patrick");
        json.put("email", JSONObject.NULL);
        json.put("username", "patrick");
        json.put("password", "patrick");
        StringEntity entity = new StringEntity(json.toString());
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        String output = EntityUtils.toString(response.getEntity());

        assertEquals(output,"Please Provide All Necessary Information");
    }

    @Test
    public void SigninTest() throws Exception{

        String registerUrl = REST_URL.concat(RESTURLs.SIGNIN.getUrl());
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(registerUrl);
        JSONObject json = new JSONObject();
        json.put("username", "clockwork");
        json.put("password", "clock1");
        StringEntity entity = new StringEntity(json.toString());
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        String output = EntityUtils.toString(response.getEntity());
        client.close();

        assertEquals(output,"213");
    }

    @Test
    public void WrongUsernameSigninTest() throws Exception{

        String registerUrl = REST_URL.concat(RESTURLs.SIGNIN.getUrl());
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(registerUrl);
        JSONObject json = new JSONObject();
        json.put("username", "hellohellohello");
        json.put("password", "hellohello");
        StringEntity entity = new StringEntity(json.toString());
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        String output = EntityUtils.toString(response.getEntity());
        client.close();

        assertEquals(output,"Username or password are wrong.");
    }


    @Test
    public void WrongPasswordSigninTest() throws Exception{

        String registerUrl = REST_URL.concat(RESTURLs.SIGNIN.getUrl());
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(registerUrl);
        JSONObject json = new JSONObject();
        json.put("username", "hellohello");
        json.put("password", "hellohellohello");
        StringEntity entity = new StringEntity(json.toString());
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        String output = EntityUtils.toString(response.getEntity());
        client.close();

        assertEquals(output,"Username or password are wrong.");
    }


    @Test
    public void EmptySigninTest() throws Exception{

        String registerUrl = REST_URL.concat(RESTURLs.SIGNIN.getUrl());
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(registerUrl);
        JSONObject json = new JSONObject();
        json.put("username", JSONObject.NULL);
        json.put("password", JSONObject.NULL);
        StringEntity entity = new StringEntity(json.toString());
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        String output = EntityUtils.toString(response.getEntity());
        client.close();

        assertEquals(output,"Username or password are wrong.");
    }

    @Test
    public void WithoutUserNameRegisterTest() throws Exception{

        String registerUrl = REST_URL.concat(RESTURLs.REGISTER.getUrl());
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(registerUrl);
        JSONObject json = new JSONObject();
        json.put("name","ken");
        json.put("surname","ken");
        json.put("email", "ken@ken.com");
        json.put("username", JSONObject.NULL);
        json.put("password", "kenken");
        StringEntity entity = new StringEntity(json.toString());
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        String output = EntityUtils.toString(response.getEntity());

        assertEquals(output,"Please Provide All Necessary Information");
    }

    @Test
    public void addScoreTest() throws Exception{
        String userId = "213";
        int score = 325;
        int level = 2;
        String registerUrl = REST_URL.concat(RESTURLs.ADDSCORE.getUrl()+ userId + "/" + score + "/" + level);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(registerUrl);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        String output = EntityUtils.toString(response.getEntity());
        client.close();
        assertEquals(output, "Score added successfully to clockwork");
    }
}
