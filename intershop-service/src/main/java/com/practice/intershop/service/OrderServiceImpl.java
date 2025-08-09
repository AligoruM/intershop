package com.practice.intershop.service;

import com.practice.intershop.enums.OrderItemStatus;
import com.practice.intershop.enums.OrderStatus;
import com.practice.intershop.enums.UpdateCountAction;
import com.practice.intershop.exception.IntershopCustomException;
import com.practice.intershop.exception.NotFoundException;
import com.practice.intershop.exception.PaymentFailedException;
import com.practice.intershop.model.OrderItem;
import com.practice.intershop.model.SalesOrder;
import com.practice.intershop.repository.OrderItemR2dbcRepository;
import com.practice.intershop.repository.OrderR2dbcRepository;
import com.practice.intershop.repository.ShowcaseItemR2dbcRepository;
import com.practice.intershop.service.validation.OrderValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderR2dbcRepository orderRepository;
    private final OrderItemR2dbcRepository orderItemRepository;
    private final ShowcaseItemR2dbcRepository showcaseItemRepository;
    private final OrderValidationService validationService;
    private final PaymentService paymentService;

    @Override
    @CacheEvict(value = "showCaseItem", key = "#p0")
    public Mono<Void> updateCountForPlannedOrder(Long showcaseItemId, UpdateCountAction action) {
        return findPlannedSalesOrder()
                .switchIfEmpty(Mono.defer(() -> {
                    SalesOrder newOrder = new SalesOrder();
                    newOrder.setOrderStatus(OrderStatus.PLANNED);
                    newOrder.setOrderItems(new ArrayList<>(1));
                    return orderRepository.save(newOrder);
                }))
                .flatMap(plannedOrder -> {
                    List<OrderItem> items = plannedOrder.getOrderItems();
                    OrderItem existingItem = items.stream()
                            .filter(i -> showcaseItemId.equals(i.getShowcaseItem().getId()))
                            .findFirst()
                            .orElse(null);

                    Mono<OrderItem> orderItemMono;

                    if (existingItem != null) {
                        orderItemMono = Mono.just(existingItem);
                    } else {
                        orderItemMono = showcaseItemRepository.findById(showcaseItemId)
                                .switchIfEmpty(Mono.error(new NotFoundException("ShowcaseItem not found")))
                                .map(showcaseItem -> {
                                    OrderItem newItem = new OrderItem();
                                    newItem.setShowcaseItem(showcaseItem);
                                    newItem.setShowcaseItemId(showcaseItem.getId());
                                    newItem.setSalesOrder(plannedOrder);
                                    newItem.setSalesOrderId(plannedOrder.getId());
                                    newItem.setUnitPrice(showcaseItem.getPrice());
                                    newItem.setStatus(OrderItemStatus.PLANNED);
                                    newItem.setQuantity(0);
                                    items.add(newItem);
                                    return newItem;
                                });
                    }

                    return orderItemMono.flatMap(orderItem -> {
                        boolean removed = false;
                        switch (action) {
                            case PLUS -> orderItem.setQuantity(orderItem.getQuantity() + 1);
                            case MINUS -> {
                                int quantity = orderItem.getQuantity();
                                if (quantity <= 0) {
                                    return Mono.error(new IntershopCustomException("Quantity must be greater than zero"));
                                } else if (quantity == 1) {
                                    removed = items.remove(orderItem);
                                } else {
                                    orderItem.setQuantity(quantity - 1);
                                }
                            }
                            case DELETE -> removed = items.remove(orderItem);
                        }
                        Publisher<?> oiActionPublisher = removed ? orderItemRepository.delete(orderItem) : orderItemRepository.save(orderItem);
                        return Mono.when(orderRepository.save(plannedOrder), oiActionPublisher)
                                .then();
                    });
                });
    }

    @Override
    public Mono<Void> performBuyOrder(Long orderId) {
        return findSalesOrder(orderId)
                .flatMap(salesOrder ->
                        validationService.isValid(salesOrder)
                                .then(paymentService.performPayment(salesOrder))
                                .flatMap(paymentResponse -> {
                                    if (Boolean.FALSE.equals(paymentResponse.getSuccess())) {
                                        return Mono.error(new PaymentFailedException("Payment rejected"));
                                    }
                                    log.debug("For order {} payment successful {}", orderId, paymentResponse);
                                    return processSuccessfulPayment(salesOrder);
                                })
                )
                .onErrorResume(PaymentFailedException.class, e -> {
                    log.error("Order {} payment failed: {}", orderId, e.getMessage());
                    return Mono.error(e);
                });
    }

    private Mono<Void> processSuccessfulPayment(SalesOrder salesOrder) {
        return Mono.defer(() -> {
                    salesOrder.setOrderStatus(OrderStatus.COMPLETED);
                    return orderRepository.save(salesOrder);
                })
                .thenMany(orderItemRepository.findAllBySalesOrderId(salesOrder.getId()))
                .flatMap(orderItem -> {
                    orderItem.setStatus(OrderItemStatus.COMPLETED);
                    return orderItemRepository.save(orderItem);
                })
                .then();
    }

    @Override
    public Mono<SalesOrder> findSalesOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .switchIfEmpty(Mono.error(() -> new NotFoundException("Order not found")))
                .flatMap(this::enrichOrderWithItems);
    }

    @Override
    public Flux<SalesOrder> findAllCompletedOrders() {
        return orderRepository.findAllByOrderStatus(OrderStatus.COMPLETED)
                .flatMap(this::enrichOrderWithItems);
    }

    @Override
    public Mono<SalesOrder> findPlannedSalesOrder() {
        return orderRepository.findFirstByOrderStatus(OrderStatus.PLANNED)
                .flatMap(this::enrichOrderWithItems);
    }

    private Mono<SalesOrder> enrichOrderWithItems(SalesOrder order) {
        return orderItemRepository.findAllBySalesOrderId(order.getId())
                .flatMap(orderItem ->
                        showcaseItemRepository.findById(orderItem.getShowcaseItemId())
                                .map(showcaseItem -> {
                                    orderItem.setShowcaseItem(showcaseItem);
                                    return orderItem;
                                })
                )
                .collectList()
                .map(orderItems -> {
                    order.setOrderItems(orderItems);
                    return order;
                });
    }
}
