package com.practice.intershop.service;

import com.practice.intershop.enums.UpdateCountAction;
import com.practice.intershop.model.SalesOrder;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    void updateCountForPlannedOrder(Long showcaseItemId, UpdateCountAction action);

    Optional<SalesOrder> findPlannedSalesOrder();

    void performBuyOrder(SalesOrder salesOrder);

    Optional<SalesOrder> findSalesOrder(Long orderId);

    List<SalesOrder> findAllCompletedOrders();
}
