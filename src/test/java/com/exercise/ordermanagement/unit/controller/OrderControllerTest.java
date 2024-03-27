package com.exercise.ordermanagement.unit.controller;

import com.exercise.ordermanagement.controller.OrderController;
import com.exercise.ordermanagement.dto.OrderRequest;
import com.exercise.ordermanagement.dto.OrderResponse;
import com.exercise.ordermanagement.entity.Order;
import com.exercise.ordermanagement.enums.OrderStatus;
import com.exercise.ordermanagement.service.OrderService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    public void addOrderSuccessfully() throws Exception {
        OrderRequest orderRequest = new OrderRequest();

        Order order = new Order();
        order.setStatus(OrderStatus.UNASSIGNED);
        order.setDistance(10);

        OrderResponse expectedResult = new OrderResponse(order);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(orderService.createOrder(any(OrderRequest.class))).thenReturn(order);

        ResponseEntity<?> response = orderController.createOrder(orderRequest);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    public void addOrderWillReturnBadRequestIfInvalidOrderRequest() throws Exception {
        OrderRequest orderRequest = new OrderRequest();

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(orderService.createOrder(any(OrderRequest.class))).thenThrow(new Exception());

        ResponseEntity<?> response = orderController.createOrder(orderRequest);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals(null, response.getBody());
    }


}
