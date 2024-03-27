package com.exercise.ordermanagement.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String[] origin;
    private String[] destination;

    public OrderRequest(String[] origin, String[] destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public OrderRequest() {

    }
}
