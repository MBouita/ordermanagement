package com.exercise.ordermanagement.service;

import com.exercise.ordermanagement.dto.OrderRequest;
import com.exercise.ordermanagement.entity.Order;
import com.exercise.ordermanagement.enums.OrderStatus;
import com.exercise.ordermanagement.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DistanceCalculator distanceCalculator;

    public Order createOrder(OrderRequest orderRequest) throws Exception {
        if (isInvalidCoordinates(orderRequest.getOrigin()) || isInvalidCoordinates(orderRequest.getDestination())) {
            throw new Exception("Invalid order request");
        }
        Order newOrder = new Order();
        newOrder.setDistance(distanceCalculator.calculateDistance(orderRequest.getOrigin(), orderRequest.getDestination()));
        newOrder.setStatus(OrderStatus.UNASSIGNED);
        return orderRepository.save(newOrder);
    }
    private boolean isInvalidCoordinates(String[] coordinates) {
        return coordinates == null || coordinates.length != 2;
    }

}