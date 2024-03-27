package com.exercise.ordermanagement.dto;

import com.exercise.ordermanagement.entity.Order;
import com.exercise.ordermanagement.enums.OrderStatus;
import lombok.Data;

@Data
public class NewOrderResponse {
    private Integer id;
    private Integer distance;
    private OrderStatus status;

    public NewOrderResponse(Order order) {
        this.id = order.getId();
        this.distance = order.getDistance();
        this.status = order.getStatus();
    }

    public NewOrderResponse() {

    }
}
