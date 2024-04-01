package com.exercise.ordermanagement.controller;

import com.exercise.ordermanagement.dto.*;
import com.exercise.ordermanagement.entity.Order;
import com.exercise.ordermanagement.exception.OrderManagementException;
import com.exercise.ordermanagement.service.OrderService;
import com.google.maps.errors.ApiException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@Valid @RequestBody NewOrderRequest newOrderRequest) {
        try {
            Order newOrder = orderService.createOrder(newOrderRequest);
            NewOrderResponse newOrderResponse = new NewOrderResponse(newOrder);
            return ResponseEntity.ok(newOrderResponse);
        } catch (IllegalArgumentException | IOException | InterruptedException | ApiException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PatchMapping("/orders/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Integer id, @Valid @RequestBody OrderStatusRequest request) {
        try {
            orderService.takeOrder(id, request);
            UpdateOrderResponse response = new UpdateOrderResponse("SUCCESS");
            return ResponseEntity.ok(response);
        } catch (OrderManagementException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/orders")
    @Valid
    public ResponseEntity<?> getOrders(@RequestParam(name = "page") Integer page,
                                       @RequestParam(name = "limit") Integer limit) {
        if (page < 1 || limit < 1) {
            ErrorResponse errorResponse = new ErrorResponse("page and limit must be strictly positive");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        List<Order> orders = orderService.getOrders(page, limit);
        if (orders.isEmpty()) {
            return ResponseEntity.ok().body(new ArrayList<>());
        }
        return ResponseEntity.ok().body(orders);
    }
}
