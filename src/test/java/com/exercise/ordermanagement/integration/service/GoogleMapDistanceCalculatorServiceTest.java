package com.exercise.ordermanagement.integration.service;

import com.exercise.ordermanagement.OrdermanagementApplication;
import com.exercise.ordermanagement.dto.OrderRequest;
import com.exercise.ordermanagement.entity.Order;
import com.exercise.ordermanagement.enums.OrderStatus;
import com.exercise.ordermanagement.exception.ValidationException;
import com.exercise.ordermanagement.repository.OrderRepository;
import com.exercise.ordermanagement.service.GoogleMapDistanceCalculatorService;
import com.exercise.ordermanagement.service.IDistanceCalculator;
import com.exercise.ordermanagement.service.OrderService;
import com.exercise.ordermanagement.service.validation.OrderRequestValidator;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes= OrdermanagementApplication.class)
@TestPropertySource(locations="classpath:application-test.properties")
public class GoogleMapDistanceCalculatorServiceTest {

    @Autowired
    GoogleMapDistanceCalculatorService distanceCalculatorService;

    @Test
    void canRetrieveDistanceUsingGCP() throws Exception {
        Integer distance = distanceCalculatorService.calculateDistance(new String[]{"55.93", "-3.118"}, new String[]{"50.087", "14.421"});

        assertEquals(1895535, distance.intValue());
    }

    @Test
    void canNotRetrieveDistanceUsingGCPIfThereIsNoRoute() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> distanceCalculatorService.calculateDistance(new String[]{"55.93", "-3.118"}, new String[]{"21.28712", "114.648867"}));
    }
}