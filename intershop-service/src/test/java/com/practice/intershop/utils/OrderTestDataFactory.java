package com.practice.intershop.utils;

import com.practice.intershop.enums.OrderStatus;
import com.practice.intershop.model.SalesOrder;

import java.util.List;

public class OrderTestDataFactory {

    public static SalesOrder createPlannedOrder1() {
        SalesOrder order = new SalesOrder();
        order.setOrderStatus(OrderStatus.PLANNED);
        order.setOrderItems(List.of(
                OrderItemTestDataFactory.createOrderItem1()
        ));
        return order;
    }

    public static SalesOrder createPlannedOrder2() {
        SalesOrder order = new SalesOrder();
        order.setOrderStatus(OrderStatus.PLANNED);
        order.setOrderItems(List.of(
                OrderItemTestDataFactory.createOrderItem1(),
                OrderItemTestDataFactory.createOrderItem2()
        ));
        return order;
    }

    public static SalesOrder createCompletedOrder1() {
        SalesOrder order = new SalesOrder();
        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setOrderItems(List.of(
                OrderItemTestDataFactory.createOrderItem1()
        ));
        return order;
    }

    public static SalesOrder createCompletedOrder2() {
        SalesOrder order = new SalesOrder();
        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setOrderItems(List.of(
                OrderItemTestDataFactory.createCompletedOrderItem1(),
                OrderItemTestDataFactory.createCompletedOrderItem2()
        ));
        return order;
    }
}
