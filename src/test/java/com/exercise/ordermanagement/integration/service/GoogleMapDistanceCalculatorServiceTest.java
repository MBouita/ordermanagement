package com.exercise.ordermanagement.integration.service;

import com.exercise.ordermanagement.OrdermanagementApplication;
import com.exercise.ordermanagement.service.GoogleMapDistanceCalculatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes= OrdermanagementApplication.class)
@TestPropertySource(locations="classpath:application-test.properties")
public class GoogleMapDistanceCalculatorServiceTest {

    @Autowired
    GoogleMapDistanceCalculatorService distanceCalculatorService;

    @Test
    void canRetrieveDistanceUsingGCP() throws Exception {
        assertNotNull(distanceCalculatorService.calculateDistance(new String[]{"55.93", "-3.118"}, new String[]{"50.087", "14.421"}));
    }

    @Test
    void canNotRetrieveDistanceUsingGCPIfThereIsNoRoute() {
        assertThrows(IllegalArgumentException.class, () -> distanceCalculatorService.calculateDistance(new String[]{"55.93", "-3.118"}, new String[]{"21.28712", "114.648867"}));
    }
}