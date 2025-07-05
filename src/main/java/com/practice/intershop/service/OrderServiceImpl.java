package com.practice.intershop.service;

import com.practice.intershop.enums.OrderItemStatus;
import com.practice.intershop.enums.OrderStatus;
import com.practice.intershop.enums.UpdateCountAction;
import com.practice.intershop.model.OrderItem;
import com.practice.intershop.model.SalesOrder;
import com.practice.intershop.model.ShowcaseItem;
import com.practice.intershop.repository.OrderRepository;
import com.practice.intershop.repository.ShowcaseItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ShowcaseItemRepository showcaseItemRepository;

    @Override
    @Transactional
    public void updateCountForPlannedOrder(Long showcaseItemId, UpdateCountAction action) {
        SalesOrder plannedSalesOrder = orderRepository.findFirstByOrderStatus(OrderStatus.PLANNED)
                .orElseGet(() -> {
                    SalesOrder salesOrder = new SalesOrder();
                    salesOrder.setOrderStatus(OrderStatus.PLANNED);
                    salesOrder.setOrderItems(new ArrayList<>());
                    return salesOrder;
                });

        OrderItem orderItem = plannedSalesOrder.getOrderItems().stream()
                .filter(item -> item.getShowcaseItem().getId().equals(showcaseItemId))
                .findFirst()
                .orElseGet(() -> {
                    OrderItem innerOi = new OrderItem();
                    ShowcaseItem showcaseItem = showcaseItemRepository.getReferenceById(showcaseItemId);
                    innerOi.setShowcaseItem(showcaseItem);
                    innerOi.setSalesOrder(plannedSalesOrder);
                    innerOi.setUnitPrice(showcaseItem.getPrice());
                    innerOi.setStatus(OrderItemStatus.PLANNED);
                    plannedSalesOrder.getOrderItems().add(innerOi);
                    return innerOi;
                });

        switch (action) {
            case minus -> {
                int quantity = orderItem.getQuantity();
                if (quantity <= 0) {
                    throw new RuntimeException("Quantity must be greater than zero");
                }
                if (quantity == 1) {
                    plannedSalesOrder.getOrderItems().remove(orderItem);
                } else {
                    orderItem.setQuantity(quantity - 1);
                }
            }
            case delete -> plannedSalesOrder.getOrderItems().remove(orderItem);
            case plus -> orderItem.setQuantity(orderItem.getQuantity() + 1);
        }
        orderRepository.save(plannedSalesOrder);
    }

    @Override
    public Optional<SalesOrder> findPlannedSalesOrder() {
        return orderRepository.findFirstByOrderStatus(OrderStatus.PLANNED);
    }

    @Override
    @Transactional
    public void performBuyOrder(SalesOrder salesOrder) {
        salesOrder.setOrderStatus(OrderStatus.COMPLETED);
        salesOrder.getOrderItems().forEach(orderItem -> orderItem.setStatus(OrderItemStatus.COMPLETED));
        orderRepository.save(salesOrder);
    }

    @Override
    public Optional<SalesOrder> findSalesOrder(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public List<SalesOrder> findAllCompletedOrders() {
        return orderRepository.findAllByOrderStatus(OrderStatus.COMPLETED);
    }

}
