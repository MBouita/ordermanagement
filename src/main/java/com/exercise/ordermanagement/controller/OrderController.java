package com.exercise.ordermanagement.controller;

import com.exercise.ordermanagement.dto.*;
import com.exercise.ordermanagement.entity.Order;
import com.exercise.ordermanagement.exception.ValidationException;
import com.exercise.ordermanagement.service.OrderService;
import com.google.maps.errors.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody NewOrderRequest newOrderRequest) {
        try {
            Order newOrder = orderService.createOrder(newOrderRequest);
            NewOrderResponse newOrderResponse = new NewOrderResponse(newOrder);
            return ResponseEntity.ok(newOrderResponse);
        } catch (IllegalArgumentException | ValidationException | IOException | InterruptedException | ApiException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PatchMapping("/orders/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Integer id, @RequestBody OrderStatusRequest request) {
        try {
            orderService.takeOrder(id, request);
            UpdateOrderResponse response = new UpdateOrderResponse("SUCCESS");
            return ResponseEntity.ok(response);
        } catch (ValidationException e) {
            ErrorResponse errorResponse = new ErrorResponse("Order not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
