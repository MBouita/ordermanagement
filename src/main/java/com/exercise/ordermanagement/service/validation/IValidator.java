package com.exercise.ordermanagement.service.validation;

import com.exercise.ordermanagement.exception.ValidationException;

public interface IValidator<Request> {
    void validate(Request request) throws ValidationException;
}