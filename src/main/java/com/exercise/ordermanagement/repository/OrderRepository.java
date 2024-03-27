package com.exercise.ordermanagement.repository;

import com.exercise.ordermanagement.entity.Order;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @NotNull
    Page<Order> findAll(@NotNull Pageable pageable);
}

