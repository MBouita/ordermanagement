package com.exercise.ordermanagement.controller;

import com.exercise.ordermanagement.dto.OrderRequest;
import com.exercise.ordermanagement.dto.OrderResponse;
import com.exercise.ordermanagement.entity.Order;
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
