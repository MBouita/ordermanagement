package com.exercise.ordermanagement.unit.controller;

import com.exercise.ordermanagement.controller.OrderController;
import com.exercise.ordermanagement.dto.NewOrderRequest;
import com.exercise.ordermanagement.dto.NewOrderResponse;
import com.exercise.ordermanagement.dto.OrderStatusRequest;
import com.exercise.ordermanagement.entity.Order;
import com.exercise.ordermanagement.enums.OrderStatus;
import com.exercise.ordermanagement.exception.ValidationException;
import com.exercise.ordermanagement.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    public void addOrderSuccessfully() throws Exception {
        NewOrderRequest newOrderRequest = new NewOrderRequest();

        Order order = new Order();
        order.setStatus(OrderStatus.UNASSIGNED);
        order.setDistance(10);

        NewOrderResponse expectedResult = new NewOrderResponse(order);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(orderService.createOrder(any(NewOrderRequest.class))).thenReturn(order);

        ResponseEntity<?> response = orderController.createOrder(newOrderRequest);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    public void addOrderWillReturnBadRequestIfInvalidOrderRequest() throws Exception {
        NewOrderRequest newOrderRequest = new NewOrderRequest();

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(orderService.createOrder(any(NewOrderRequest.class))).thenThrow(new ValidationException("Validation Exception"));

        ResponseEntity<?> response = orderController.createOrder(newOrderRequest);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals("{\"error\":\"Validation Exception\"}", new ObjectMapper().writeValueAsString(response.getBody()));
    }

    @Test
    public void updateOrderSuccessfully() throws Exception {
        int orderId = 1;
        OrderStatusRequest request = new OrderStatusRequest();

        doNothing().when(orderService).takeOrder(orderId, request);

        ResponseEntity<?> response = orderController.updateOrder(orderId, request);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals("{\"status\":\"SUCCESS\"}", new ObjectMapper().writeValueAsString(response.getBody()));
    }

    @Test
    public void updateOrderWillReturnNotFoundIfServiceThrowException() throws Exception {
        int orderId = 1;
        OrderStatusRequest request = new OrderStatusRequest();

        doThrow(new ValidationException("Order not found")).when(orderService).takeOrder(orderId, request);

        ResponseEntity<?> response = orderController.updateOrder(orderId, request);

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals("{\"error\":\"Order not found\"}", new ObjectMapper().writeValueAsString(response.getBody()));
    }
}
