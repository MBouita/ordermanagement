package com.exercise.ordermanagement.dto;

import lombok.Data;

@Data
public class UpdateOrderResponse {
    String status;

    public UpdateOrderResponse(String status) {
        this.status = status;
    }
}
