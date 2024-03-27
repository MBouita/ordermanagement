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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Test
    public void getOrdersSuccessfully() throws Exception {
        int page = 1;
        int limit = 10;

        List<Order> orders = new ArrayList<>();
        orders.add(addNewOrder(1, 1000));
        orders.add(addNewOrder(2, 2000));

        when(orderService.getOrders(page, limit)).thenReturn(orders);

        ResponseEntity<?> response = orderController.getOrders(page, limit);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(orders, response.getBody());
    }

    @Test
    public void getOrdersWillReturnBadRequestIfInvalidPageOrLimit() throws Exception {
        int page = 0;
        int limit = 10;

        when(orderService.getOrders(page, limit)).thenThrow(new ValidationException("Invalid page or limit"));

        ResponseEntity<?> response = orderController.getOrders(page, limit);

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals("{\"error\":\"Invalid page or limit\"}", new ObjectMapper().writeValueAsString(response.getBody()));
    }

    @Test
    public void getOrdersWillReturnEmptyListIfNoResults() throws Exception {
        int page = 1;
        int limit = 10;

        when(orderService.getOrders(page, limit)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = orderController.getOrders(page, limit);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    private Order addNewOrder(int id, int distance) {
        Order order = new Order();
        order.setId(id);
        order.setDistance(distance);
        order.setStatus(OrderStatus.UNASSIGNED);
        return order;
    }

}
