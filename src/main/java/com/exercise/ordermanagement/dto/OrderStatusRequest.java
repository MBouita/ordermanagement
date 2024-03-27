package com.exercise.ordermanagement.dto;

import com.exercise.ordermanagement.enums.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class OrderStatusRequest extends Request {

    private OrderStatus status;

}
