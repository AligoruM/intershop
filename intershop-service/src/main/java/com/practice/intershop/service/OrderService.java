package com.practice.intershop.service;

import com.practice.intershop.enums.UpdateCountAction;
import com.practice.intershop.model.SalesOrder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface OrderService {
    Mono<Void> updateCountForPlannedOrder(Long showcaseItemId, UpdateCountAction action);

    Mono<SalesOrder> findPlannedSalesOrder();

    Mono<Void> performBuyOrder(Long orderId);

    Mono<SalesOrder> findSalesOrder(Long orderId);

    Flux<SalesOrder> findAllCompletedOrders();

    Mono<BigDecimal> getBalance(Long userId);
}
