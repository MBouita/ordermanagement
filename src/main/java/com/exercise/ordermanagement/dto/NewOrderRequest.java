package com.exercise.ordermanagement.dto;

import com.exercise.ordermanagement.config.ArrayOfStringDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewOrderRequest {

    @NotEmpty(message = "Origin must have exactly 2 values")
    @Size(min = 2, max = 2, message = "Origin must have exactly 2 values")
    @JsonDeserialize(using = ArrayOfStringDeserializer.class)
    private String[] origin;

    @NotEmpty(message = "Destination must have exactly 2 values")
    @Size(min = 2, max = 2, message = "Destination must have exactly 2 values")
    @JsonDeserialize(using = ArrayOfStringDeserializer.class)
    private String[] destination;

}
