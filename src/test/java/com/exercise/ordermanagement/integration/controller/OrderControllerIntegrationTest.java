package com.exercise.ordermanagement.integration.controller;

import com.exercise.ordermanagement.OrdermanagementApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = OrdermanagementApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void orderCreation() {
        String validRequestJson = "{\n    \"origin\": [\"55.93\", \"-3.118\"],\n    \"destination\": [\"50.087\", \"14.421\"]\n}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/orders",
                new HttpEntity<>(validRequestJson, headers),
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("\"id\":"));
        assertTrue(response.getBody().contains("\"distance\":"));
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
        assertEquals("{\"error\":\"JSON parse error: Expected an array for field origin\"}",response.getBody());
    }

    @Test
    public void orderCreationWillFailIfNoBodyIsProvided() {
        String validRequestJson = "";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/orders",
                new HttpEntity<>(validRequestJson, headers),
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Required request body is missing"));
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
        assertEquals("{\"error\":\"Destination must have exactly 2 values. \"}", response.getBody());
    }

    @Test
    public void takeOrderSuccessfully() throws JsonProcessingException {
        String validRequestJson = "{\n    \"origin\": [\"55.93\", \"-3.118\"],\n    \"destination\": [\"50.087\", \"14.421\"]\n}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        ResponseEntity<String> orderCreationResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/orders",
                new HttpEntity<>(validRequestJson, headers),
                String.class
        );
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(orderCreationResponse.getBody());

        int orderId = jsonNode.get("id").asInt();

        String requestJson = "{\"status\":\"TAKEN\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/orders/" + orderId,
                HttpMethod.PATCH,
                requestEntity,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        String expectedResponseBody = "{\"status\":\"SUCCESS\"}";
        assertEquals(expectedResponseBody, response.getBody());
    }

    @Test
    public void takeOrderFailureWhenOrderNotFound() throws Exception {
        int orderId = 0;

        String requestJson = "{\"status\":\"TAKEN\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/orders/" + orderId,
                HttpMethod.PATCH,
                requestEntity,
                String.class
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        String expectedResponseBody = "{\"error\":\"Order not found\"}";
        assertEquals(expectedResponseBody, response.getBody());
    }

    @Test
    public void takeOrderFailureWhenIncorrectStatusProvided() throws Exception {
        int orderId = 0;

        String requestJson = "{\"status\":\"UNASSIGNED\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/orders/" + orderId,
                HttpMethod.PATCH,
                requestEntity,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals("{\"error\":\"Only TAKEN status is supported. \"}", response.getBody());
    }


    @Test
    public void getOrdersSuccessfully() {
        String validRequestJson = "{\n    \"origin\": [\"55.93\", \"-3.118\"],\n    \"destination\": [\"50.087\", \"14.421\"]\n}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        restTemplate.postForEntity(
                "http://localhost:" + port + "/orders",
                new HttpEntity<>(validRequestJson, headers),
                String.class
        );


        int page = 1;
        int limit = 10;

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/orders?page=" + page + "&limit=" + limit,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("\"id\":"));
        assertTrue(response.getBody().contains("\"distance\":"));
        assertTrue(response.getBody().contains("\"status\":\"UNASSIGNED\""));
    }

    @Test
    public void getOrdersWillReturnBadRequestIfInvalidPageOrLimit() {
        int page = 0;
        int limit = 10;

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/orders?page=" + page + "&limit=" + limit,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"error\":\"page and limit must be strictly positive\"}", response.getBody());
    }

    @Test
    public void getOrdersWillReturnEmptyListIfNoResults() {
        int page = 1;
        int limit = 10;
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/orders?page=" + page + "&limit=" + limit,
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
