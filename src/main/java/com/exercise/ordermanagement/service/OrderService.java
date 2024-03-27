package com.exercise.ordermanagement.service;

import com.exercise.ordermanagement.dto.NewOrderRequest;
import com.exercise.ordermanagement.dto.OrderStatusRequest;
import com.exercise.ordermanagement.entity.Order;
import com.exercise.ordermanagement.enums.OrderStatus;
import com.exercise.ordermanagement.exception.ValidationException;
import com.exercise.ordermanagement.repository.OrderRepository;
import com.exercise.ordermanagement.service.validation.NewOrderRequestValidator;
import com.exercise.ordermanagement.service.validation.OrderStatusRequestValidator;
import com.google.maps.errors.ApiException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private NewOrderRequestValidator newOrderValidator;

    @Autowired
    private OrderStatusRequestValidator orderStatusValidator;
    @Autowired
    private IDistanceCalculator distanceCalculator;

    public Order createOrder(NewOrderRequest orderRequest) throws ValidationException, IOException, InterruptedException, ApiException {
        newOrderValidator.validate(orderRequest);
        Order newOrder = new Order();
        newOrder.setDistance(distanceCalculator.calculateDistance(orderRequest.getOrigin(), orderRequest.getDestination()));
        newOrder.setStatus(OrderStatus.UNASSIGNED);
        return orderRepository.save(newOrder);
    }

    @Transactional
    public void takeOrder(Integer orderId, OrderStatusRequest request) throws ValidationException {
        orderStatusValidator.validate(request);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ValidationException("Order not found"));

        if (order.getStatus() == OrderStatus.TAKEN) {
            throw new ValidationException("Order has already been taken");
        }
        order.setStatus(OrderStatus.TAKEN);
        orderRepository.save(order);
    }
}