package com.exercise.ordermanagement.integration.controller;

import com.exercise.ordermanagement.OrdermanagementApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes= OrdermanagementApplication.class)
@TestPropertySource(locations="classpath:application-test.properties")
public class OrderControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void orderCreation() {
        String validRequestJson = "{\n    \"origin\": [\"START_LATITUDE\", \"START_LONGITUDE\"],\n    \"destination\": [\"END_LATITUDE\", \"END_LONGITUDE\"]\n}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/orders",
                new HttpEntity<>(validRequestJson, headers),
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("\"id\":"));
        assertTrue(response.getBody().contains("\"distance\":10"));
        assertTrue(response.getBody().contains("\"status\":\"UNASSIGNED\""));
    }

    @Test
    public void orderCreationWillFailIfIncorrectTypeForFieldIsProvided() {
        String validRequestJson = "{\n    \"origin\": 1,\n    \"destination\": [3.48, 12.57]\n}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/orders",
                new HttpEntity<>(validRequestJson, headers),
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void orderCreationWillFailIfIncorrectAmountOfCoordinatesAreSubmitted() {
        String validRequestJson = "{\n    \"origin\": [\"3.48\", \"12.57\"],\n    \"destination\": [\"3.48\",\"3.48\", \"12.57\"]\n}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/orders",
                new HttpEntity<>(validRequestJson, headers),
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
