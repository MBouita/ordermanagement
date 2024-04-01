package com.exercise.ordermanagement.dto;

import com.exercise.ordermanagement.config.validators.ValidOrderStatus;
import com.exercise.ordermanagement.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderStatusRequest {

    @ValidOrderStatus
    private OrderStatus status;

}
