package com.facecloud.facecloudserverapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    private String name = "java";

    @Autowired
    private TestRestTemplate restTemplate;

    private String postLocationUrl = "";

    private HttpHeaders headers;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private JSONObject locationJsonObject;
    private JSONObject wrongLocationJsonObject;

    @BeforeEach
    public void initializeJson() throws JSONException {
        headers = new HttpHeaders();

        String string = name + "j";

        headers.setContentType(MediaType.APPLICATION_JSON);
        locationJsonObject = new JSONObject();
        locationJsonObject.put("id", 0L);
        locationJsonObject.put("username", string);
        locationJsonObject.put("latitude", 10D);
        locationJsonObject.put("longitude", 15D);
        locationJsonObject.put("time", LocalDateTime.of(2022, 01, 01, 0,0));

        wrongLocationJsonObject = new JSONObject();
        wrongLocationJsonObject.put("ggg", 10);
    }

    @Test
    public void testGreetingMessage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/homePage",
                String.class)).contains("Hello, World");
    }

    @Test
    public void testLocationMessage(){
        String get = this.restTemplate.getForObject("http://localhost:" + port + "/" + name,
                String.class);

        System.out.println(get);

        assertThat(get).contains(name);
    }

    @Test
    public void testStatisticsMessage(){
        String get = "";
        try {
             get = this.restTemplate.getForObject("http://localhost:" + port + "/" + name + "/statistics",
                    String.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println(get);

        assertThat(get).isEqualTo("dany uzytkownik nie ma historii w tabeli");

    }

    @Test
    public void testPostLocation() throws JsonProcessingException {

        HttpEntity<String> request = new HttpEntity<String>(locationJsonObject.toString(), headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/", request, String.class);

        JsonNode root = objectMapper.readTree(responseEntity.getBody());

        System.out.println(root.path("longitude").asDouble());
        System.out.println(root);

        assertEquals(name +"j", root.path("username").asText());
    }

    @Test
    public void testWrongPostLocation() throws JsonProcessingException {
        HttpEntity<String> request = new HttpEntity<String>(wrongLocationJsonObject.toString(), headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/", "", String.class);

        JsonNode root = objectMapper.readTree(responseEntity.getBody());

        System.out.println(root.path("longitude").asDouble());
        System.out.println(root);

        assertEquals(400, root.path("status").asDouble());
    }

}
