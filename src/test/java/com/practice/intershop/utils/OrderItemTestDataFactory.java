package com.practice.intershop.utils;

import com.practice.intershop.enums.OrderItemStatus;
import com.practice.intershop.model.OrderItem;
import com.practice.intershop.model.ShowcaseItem;

public class OrderItemTestDataFactory {

    public static OrderItem createOrderItem1() {
        OrderItem item = new OrderItem();
        item.setStatus(OrderItemStatus.PLANNED);
        ShowcaseItem showcaseItem = ShowcaseTestDataFactory.createShowcaseItem1();
        item.setShowcaseItem(showcaseItem);
        item.setUnitPrice(showcaseItem.getPrice());
        item.setQuantity(1);
        return item;
    }

    public static OrderItem createOrderItem2() {
        OrderItem item = new OrderItem();
        item.setStatus(OrderItemStatus.PLANNED);
        ShowcaseItem showcaseItem = ShowcaseTestDataFactory.createShowcaseItem2();
        item.setShowcaseItem(showcaseItem);
        item.setUnitPrice(showcaseItem.getPrice());
        item.setQuantity(2);
        return item;
    }

    public static OrderItem createCompletedOrderItem1() {
        OrderItem item = new OrderItem();
        item.setStatus(OrderItemStatus.COMPLETED);
        ShowcaseItem showcaseItem = ShowcaseTestDataFactory.createShowcaseItem1();
        item.setShowcaseItem(showcaseItem);
        item.setUnitPrice(showcaseItem.getPrice());
        item.setQuantity(1);
        return item;
    }

    public static OrderItem createCompletedOrderItem2() {
        OrderItem item = new OrderItem();
        item.setStatus(OrderItemStatus.COMPLETED);
        ShowcaseItem showcaseItem = ShowcaseTestDataFactory.createShowcaseItem2();
        item.setShowcaseItem(showcaseItem);
        item.setUnitPrice(showcaseItem.getPrice());
        item.setQuantity(2);
        return item;
    }
}
