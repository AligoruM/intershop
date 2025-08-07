package com.practice.intershop.repository;

import com.practice.intershop.enums.OrderStatus;
import com.practice.intershop.model.SalesOrder;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderR2dbcRepository extends R2dbcRepository<SalesOrder, Long> {
    Mono<SalesOrder> findFirstByOrderStatus(OrderStatus orderStatus);

    Flux<SalesOrder> findAllByOrderStatus(OrderStatus orderStatus);
}
