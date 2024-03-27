package com.exercise.ordermanagement.service;

import com.exercise.ordermanagement.dto.OrderRequest;
import com.exercise.ordermanagement.entity.Order;
import com.exercise.ordermanagement.enums.OrderStatus;
import com.exercise.ordermanagement.exception.ValidationException;
import com.exercise.ordermanagement.repository.OrderRepository;
import com.exercise.ordermanagement.service.validation.OrderRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderRequestValidator validator;
    @Autowired
    private DistanceCalculator distanceCalculator;

    public Order createOrder(OrderRequest orderRequest) throws ValidationException {
        validator.validate(orderRequest);
        Order newOrder = new Order();
        newOrder.setDistance(distanceCalculator.calculateDistance(orderRequest.getOrigin(), orderRequest.getDestination()));
        newOrder.setStatus(OrderStatus.UNASSIGNED);
        return orderRepository.save(newOrder);
    }

}