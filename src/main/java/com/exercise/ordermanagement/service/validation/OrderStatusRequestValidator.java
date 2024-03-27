package com.exercise.ordermanagement.service.validation;

import com.exercise.ordermanagement.dto.OrderStatusRequest;
import com.exercise.ordermanagement.exception.ValidationException;
import org.springframework.stereotype.Component;

import static com.exercise.ordermanagement.enums.OrderStatus.TAKEN;

@Component
public class OrderStatusRequestValidator implements IValidator<OrderStatusRequest> {
    public void validate(OrderStatusRequest request) throws ValidationException {
        if (request.getStatus() != TAKEN) {
            throw new ValidationException("Only TAKEN status is supported");
        }
    }
}
