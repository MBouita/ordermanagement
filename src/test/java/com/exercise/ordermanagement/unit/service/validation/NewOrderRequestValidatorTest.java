package com.exercise.ordermanagement.unit.service.validation;

import com.exercise.ordermanagement.dto.NewOrderRequest;
import com.exercise.ordermanagement.exception.ValidationException;
import com.exercise.ordermanagement.service.validation.NewOrderRequestValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class NewOrderRequestValidatorTest {

    @InjectMocks
    private NewOrderRequestValidator validator;

    @Test
    public void doNothingIfOrderIsCorrect() throws Exception {
        String[] origin = new String[]{"START_LATITUDE", "START_LONGITUDE"};
        String[] destination = new String[]{"END_LATITUDE", "END_LONGITUDE"};
        NewOrderRequest orderRequest = new NewOrderRequest(origin, destination);
        validator.validate(orderRequest);
    }

    @Test
    public void throwExceptionWhenOriginCoordinatesAreIncorrect() {
        String[] origin = new String[]{"START_LATITUDE", "START_LONGITUDE", "123"};
        String[] destination = new String[]{"END_LATITUDE", "END_LONGITUDE"};
        NewOrderRequest orderRequest = new NewOrderRequest(origin, destination);
        assertThrows(ValidationException.class, () -> validator.validate(orderRequest));
    }

    @Test
    public void throwExceptionWhenDestinationCoordinatesAreIncorrect() {
        String[] origin = new String[]{"START_LATITUDE", "START_LONGITUDE"};
        String[] destination = new String[]{"END_LATITUDE", "END_LONGITUDE", "123"};
        NewOrderRequest orderRequest = new NewOrderRequest(origin, destination);
        assertThrows(ValidationException.class, () -> validator.validate(orderRequest));
    }

}