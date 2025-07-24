package com.practice.intershop.repository;

import com.practice.intershop.model.OrderItem;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface OrderItemR2dbcRepository extends R2dbcRepository<OrderItem, Long> {
    Flux<OrderItem> findAllBySalesOrderId(Long salesOrderId);
}
