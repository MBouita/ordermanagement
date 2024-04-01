package com.exercise.ordermanagement.config.validators;

import com.exercise.ordermanagement.enums.OrderStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OrderStatusValidator implements ConstraintValidator<ValidOrderStatus, OrderStatus> {
    @Override
    public void initialize(ValidOrderStatus constraintAnnotation) {
    }

    @Override
    public boolean isValid(OrderStatus value, ConstraintValidatorContext context) {
        return value == OrderStatus.TAKEN;
    }
}