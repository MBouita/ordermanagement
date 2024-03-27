package com.exercise.ordermanagement.dto;

import com.exercise.ordermanagement.entity.Order;
import com.exercise.ordermanagement.enums.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class OrderResponse {
    private Integer id;
    private Integer distance;
    private OrderStatus status;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.distance = order.getDistance();
        this.status = order.getStatus();
    }

    public OrderResponse() {

    }
}
