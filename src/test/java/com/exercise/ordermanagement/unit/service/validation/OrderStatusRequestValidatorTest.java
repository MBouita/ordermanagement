package com.exercise.ordermanagement.unit.service.validation;

import com.exercise.ordermanagement.dto.OrderStatusRequest;
import com.exercise.ordermanagement.enums.OrderStatus;
import com.exercise.ordermanagement.exception.ValidationException;
import com.exercise.ordermanagement.service.validation.OrderStatusRequestValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class OrderStatusRequestValidatorTest {

    @InjectMocks
    private OrderStatusRequestValidator validator;

    @Test
    public void doNothingIfPayloadIsCorrect() throws Exception {
        OrderStatusRequest orderRequest = new OrderStatusRequest();
        orderRequest.setStatus(OrderStatus.TAKEN);
        validator.validate(orderRequest);
    }

    @Test
    public void throwExceptionWhenOrderStatusIsIncorrect() throws ValidationException {
        OrderStatusRequest orderRequest = new OrderStatusRequest();
        orderRequest.setStatus(OrderStatus.UNASSIGNED);
        assertThrows(ValidationException.class, () -> validator.validate(orderRequest));
    }
}