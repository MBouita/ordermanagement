package com.exercise.ordermanagement.dto;

import com.exercise.ordermanagement.config.ArrayOfStringDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderRequest extends Request {
    @JsonDeserialize(using = ArrayOfStringDeserializer.class)
    private String[] origin;
    @JsonDeserialize(using = ArrayOfStringDeserializer.class)
    private String[] destination;

    public OrderRequest(String[] origin, String[] destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public OrderRequest() {

    }
}
