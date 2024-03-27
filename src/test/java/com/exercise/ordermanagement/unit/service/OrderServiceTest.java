package com.exercise.ordermanagement.unit.service;

import com.exercise.ordermanagement.dto.OrderRequest;
import com.exercise.ordermanagement.entity.Order;
import com.exercise.ordermanagement.enums.OrderStatus;
import com.exercise.ordermanagement.exception.ValidationException;
import com.exercise.ordermanagement.repository.OrderRepository;
import com.exercise.ordermanagement.service.DistanceCalculator;
import com.exercise.ordermanagement.service.OrderService;
import com.exercise.ordermanagement.service.validation.OrderRequestValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderRequestValidator validator;

    @Mock
    private DistanceCalculator distanceCalculator;
    @InjectMocks
    private OrderService orderService;

    @Test
    public void orderCreationSuccessfullWhenOrderRequestSubmitted() throws Exception {
        String[] origin = new String[]{"START_LATITUDE", "START_LONGITUDE"};
        String[] destination = new String[]{"END_LATITUDE", "END_LONGITUDE"};
        OrderRequest orderRequest = new OrderRequest(origin, destination);

        Order expectedResult = new Order();
        expectedResult.setStatus(OrderStatus.UNASSIGNED);
        expectedResult.setDistance(10);

        doNothing().when(validator).validate(orderRequest);
        when(distanceCalculator.calculateDistance(orderRequest.getOrigin(), orderRequest.getDestination())).thenReturn(10);
        when(orderRepository.save(expectedResult)).thenReturn(expectedResult);


        Order order = orderService.createOrder(orderRequest);

        assertEquals(10, order.getDistance());
        assertEquals(OrderStatus.UNASSIGNED, order.getStatus());
    }

    @Test
    public void orderCreationThrowExceptionWhenOrderRequestIsInvalid() throws ValidationException {
        OrderRequest invalidRequest = new OrderRequest(new String[]{"START_LATITUDE", "START_LONGITUDE"}, new String[]{"START_LATITUDE", "START_LONGITUDE", ""});
        doThrow(ValidationException.class).when(validator).validate(invalidRequest);
        assertThrows(Exception.class, () -> orderService.createOrder(invalidRequest));
    }

}