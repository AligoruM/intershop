package com.practice.intershop.controller;

import com.practice.intershop.model.OrderItem;
import com.practice.intershop.model.SalesOrder;
import com.practice.intershop.utils.OrderTestDataFactory;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class OrderControllerTest extends AbstractControllerTest {

    @Test
    void testGetOrderById_shouldReturnOrderPage() {
        SalesOrder order = OrderTestDataFactory.createCompletedOrder1();
        persistShowcaseItems(order);

        SalesOrder savedOrder = salesOrderRepository.save(order).block();
        persistOrderItems(savedOrder);

        webTestClient.get()
                .uri("/orders/" + savedOrder.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> {
                    assertThat(body).contains("Заказ № " + savedOrder.getId());
                    savedOrder.getOrderItems().forEach(item -> {
                        assertThat(body).contains(item.getShowcaseItem().getName());
                        assertThat(body).contains(item.getQuantity() + " шт.");

                        BigDecimal total = item.getUnitPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                                .setScale(2, RoundingMode.HALF_UP);

                        assertThat(body).contains(total.toPlainString() + " руб.");
                    });
                });
    }

    @Test
    void testGetAllCompletedOrders_shouldReturnOrdersPage() {
        SalesOrder order1 = OrderTestDataFactory.createCompletedOrder1();
        SalesOrder order2 = OrderTestDataFactory.createCompletedOrder2();
        persistShowcaseItems(order1, order2);

        List<SalesOrder> savedOrders = Flux.just(order1, order2)
                .flatMap(salesOrderRepository::save)
                .collectList()
                .block();
        persistOrderItems(savedOrders.toArray(new SalesOrder[0]));

        webTestClient.get()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> {
                    savedOrders.forEach(order -> {
                        assertThat(body).contains("Заказ № " + order.getId());

                        order.getOrderItems().forEach(item -> {
                            BigDecimal total = item.getUnitPrice()
                                    .multiply(BigDecimal.valueOf(item.getQuantity()))
                                    .setScale(2, RoundingMode.HALF_UP);

                            assertThat(body).contains(item.getShowcaseItem().getName());
                            assertThat(body).contains(item.getQuantity() + " шт.");
                            assertThat(body).contains(total.toPlainString() + " руб.");
                        });
                    });
                });
    }

    //here should be more tests, but I have no time to write them :(

    private void persistShowcaseItems(SalesOrder... salesOrders) {
        Arrays.stream(salesOrders)
                .flatMap(so -> so.getOrderItems().stream())
                .map(OrderItem::getShowcaseItem)
                .distinct()
                .forEach(showcaseItem -> showcaseItemRepository.save(showcaseItem).block());
    }

    private void persistOrderItems(SalesOrder... salesOrders) {
        for (SalesOrder salesOrder : salesOrders) {
            salesOrder.getOrderItems().forEach(orderItem -> {
                orderItem.setSalesOrderId(salesOrder.getId());
                orderItem.setSalesOrder(salesOrder);
                orderItem.setShowcaseItemId(orderItem.getShowcaseItem().getId());
                orderItemRepository.save(orderItem).block();
            });
        }
    }
}
