package com.exercise.ordermanagement.unit.service.validation;

import com.exercise.ordermanagement.dto.OrderRequest;
import com.exercise.ordermanagement.exception.ValidationException;
import com.exercise.ordermanagement.service.validation.OrderRequestValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class OrderRequestValidatorTest {

    @InjectMocks
    private OrderRequestValidator validator;
    @Test
    public void doNothingIfOrderIsCorrect() throws Exception {
        String[] origin = new String[]{"START_LATITUDE", "START_LONGITUDE"};
        String[] destination = new String[]{"END_LATITUDE", "END_LONGITUDE"};
        OrderRequest orderRequest = new OrderRequest(origin, destination);
        validator.validate(orderRequest);
    }

    @Test
    public void throwExceptionWhenOriginCoordinatesAreIncorrect() throws ValidationException {
        String[] origin = new String[]{"START_LATITUDE", "START_LONGITUDE","123"};
        String[] destination = new String[]{"END_LATITUDE", "END_LONGITUDE"};
        OrderRequest orderRequest = new OrderRequest(origin, destination);
        assertThrows(ValidationException.class, () -> validator.validate(orderRequest));
    }

    @Test
    public void throwExceptionWhenDestinationCoordinatesAreIncorrect() throws ValidationException {
        String[] origin = new String[]{"START_LATITUDE", "START_LONGITUDE"};
        String[] destination = new String[]{"END_LATITUDE", "END_LONGITUDE","123"};
        OrderRequest orderRequest = new OrderRequest(origin, destination);
        assertThrows(ValidationException.class, () -> validator.validate(orderRequest));
    }

}