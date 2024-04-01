package com.exercise.ordermanagement.unit.controller.validation;

import com.exercise.ordermanagement.dto.NewOrderRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class NewOrderRequestValidatorTest {

    @InjectMocks
    private static Validator validator;

    @BeforeAll
    public static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validationSuccessful() {
        NewOrderRequest request = new NewOrderRequest();
        request.setOrigin(new String[]{"firstValue", "secondValue"});
        request.setDestination(new String[]{"firstValue", "secondValue"});

        assertEquals(0, validator.validate(request).size());
    }

    @Test
    public void validationFailingWhenOriginIsInvalid() {
        NewOrderRequest request = new NewOrderRequest();
        request.setOrigin(new String[]{"firstValue"});
        request.setDestination(new String[]{"firstValue", "secondValue"});

        ConstraintViolation<NewOrderRequest> violation = validator.validate(request).iterator().next();

        assertEquals("Origin must have exactly 2 values", violation.getMessage());
        assertEquals("origin", violation.getPropertyPath().toString());

    }

    @Test
    public void validationFailingWhenDestinationIsInvalid() {
        NewOrderRequest request = new NewOrderRequest();
        request.setOrigin(new String[]{"firstValue", "secondValue"});
        request.setDestination(new String[]{"firstValue"});

        ConstraintViolation<NewOrderRequest> violation = validator.validate(request).iterator().next();

        assertEquals("Destination must have exactly 2 values", violation.getMessage());
        assertEquals("destination", violation.getPropertyPath().toString());

    }
}