package com.exercise.ordermanagement.entity;

import com.exercise.ordermanagement.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "`ORDER`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer distance;
    private OrderStatus status;

}