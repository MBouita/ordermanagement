package com.exercise.ordermanagement.service;

import com.exercise.ordermanagement.dto.NewOrderRequest;
import com.exercise.ordermanagement.dto.OrderStatusRequest;
import com.exercise.ordermanagement.entity.Order;
import com.exercise.ordermanagement.enums.OrderStatus;
import com.exercise.ordermanagement.exception.OrderManagementException;
import com.exercise.ordermanagement.repository.OrderRepository;
import com.google.maps.errors.ApiException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private IDistanceCalculator distanceCalculator;

    public Order createOrder(NewOrderRequest orderRequest) throws IOException, InterruptedException, ApiException {
        Order newOrder = new Order();
        newOrder.setDistance(distanceCalculator.calculateDistance(orderRequest.getOrigin(), orderRequest.getDestination()));
        newOrder.setStatus(OrderStatus.UNASSIGNED);
        return orderRepository.save(newOrder);
    }

    @Transactional
    public void takeOrder(Integer orderId, OrderStatusRequest request) throws OrderManagementException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderManagementException("Order not found"));

        if (order.getStatus() == OrderStatus.TAKEN) {
            throw new OrderManagementException("Order has already been taken");
        }
        order.setStatus(OrderStatus.TAKEN);
        orderRepository.save(order);
    }

    public List<Order> getOrders(Integer page, Integer limit) {
        PageRequest pageable = PageRequest.of(page - 1, limit);

        Page<Order> orderPage = orderRepository.findAll(pageable);

        return orderPage.getContent();
    }
}