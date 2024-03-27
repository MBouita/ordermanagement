package com.exercise.ordermanagement.controller;

import com.exercise.ordermanagement.dto.ErrorResponse;
import com.exercise.ordermanagement.dto.OrderRequest;
import com.exercise.ordermanagement.dto.OrderResponse;
import com.exercise.ordermanagement.entity.Order;
import com.exercise.ordermanagement.exception.ValidationException;
import com.exercise.ordermanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            Order newOrder = orderService.createOrder(orderRequest);
            OrderResponse orderResponse = new OrderResponse(newOrder);
            return ResponseEntity.ok(orderResponse);
        } catch (IllegalArgumentException | ValidationException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

}
