package com.exercise.ordermanagement.unit.controller.validation;

import com.exercise.ordermanagement.config.validators.OrderStatusValidator;
import com.exercise.ordermanagement.dto.OrderStatusRequest;
import com.exercise.ordermanagement.enums.OrderStatus;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
public class OrderStatusRequestValidatorTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    private static OrderStatusValidator validator;

    @BeforeAll
    public static void setupValidator() {
        validator = new OrderStatusValidator();
    }

    @Test
    void validationSuccessful() {
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest();
        orderStatusRequest.setStatus(OrderStatus.TAKEN);
        validator.isValid(orderStatusRequest.getStatus(), constraintValidatorContext);
    }

    @Test
    void validationFailingWhenStatusRequestIsInvalid() {
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest();
        orderStatusRequest.setStatus(OrderStatus.UNASSIGNED);

        boolean isValid = validator.isValid(orderStatusRequest.getStatus(), constraintValidatorContext);

        assertFalse(isValid);
    }
}