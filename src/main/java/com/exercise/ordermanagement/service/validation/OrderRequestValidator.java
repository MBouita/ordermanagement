package com.exercise.ordermanagement.service.validation;

import com.exercise.ordermanagement.dto.OrderRequest;
import com.exercise.ordermanagement.exception.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class OrderRequestValidator {
    public void validate(OrderRequest request) throws ValidationException {
         if (isInvalidCoordinates(request.getDestination())){
            throw new ValidationException("destination must have exactly 2 values");
        }
        if (isInvalidCoordinates(request.getOrigin())){
            throw new ValidationException("origin must have exactly 2 values");
        }
    }

    private boolean isInvalidCoordinates(String[] coordinates) {
        return coordinates == null || coordinates.length != 2;
    }

}
