package com.exercise.ordermanagement.service;

import com.exercise.ordermanagement.dto.OrderRequest;
import com.exercise.ordermanagement.entity.Order;
import com.exercise.ordermanagement.enums.OrderStatus;
import com.exercise.ordermanagement.exception.ValidationException;
import com.exercise.ordermanagement.repository.OrderRepository;
import com.exercise.ordermanagement.service.validation.OrderRequestValidator;
import com.google.maps.errors.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderRequestValidator validator;
    @Autowired
    private IDistanceCalculator distanceCalculator;

    public Order createOrder(OrderRequest orderRequest) throws ValidationException, IOException, InterruptedException, ApiException {
        validator.validate(orderRequest);
        Order newOrder = new Order();
        newOrder.setDistance(distanceCalculator.calculateDistance(orderRequest.getOrigin(), orderRequest.getDestination()));
        newOrder.setStatus(OrderStatus.UNASSIGNED);
        return orderRepository.save(newOrder);
    }

}