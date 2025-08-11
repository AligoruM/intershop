package com.practice.intershop.controller;

import com.practice.intershop.dto.SalesOrderDto;
import com.practice.intershop.enums.UpdateCountAction;
import com.practice.intershop.mapper.SalesOrderMapper;
import com.practice.intershop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final SalesOrderMapper salesOrderMapper;

    @GetMapping("/orders")
    public Mono<Rendering> orders() {
        return orderService.findAllCompletedOrders()
                .map(salesOrderMapper::toDto)
                .collectList()
                .map(orders -> Rendering.view("orders")
                        .modelAttribute("orders", orders)
                        .build());
    }

    @GetMapping("/orders/{id}")
    public Mono<Rendering> order(@PathVariable("id") Long id) {
        return orderService.findSalesOrder(id)
                .map(salesOrderMapper::toDto)
                .map(order -> Rendering.view("order")
                        .modelAttribute("salesOrder", order)
                        .build());
    }

    @PostMapping({"/main/items/{showcaseItemId}", "/items/{showcaseItemId}"})
    public Mono<Rendering> updateItem(@PathVariable("showcaseItemId") Long showcaseItemId,
                                      ServerWebExchange webExchange) {
        return webExchange.getFormData().flatMap(formData -> {
            String action = formData.getFirst("action");
            UpdateCountAction enumAction = UpdateCountAction.valueOf(action);
            return orderService.updateCountForPlannedOrder(showcaseItemId, enumAction)
                    .thenReturn(Rendering.redirectTo("/main/items/" + showcaseItemId).build());
        });
    }

    @GetMapping("/cart/items")
    public Mono<Rendering> cartItems() {
        return orderService.findPlannedSalesOrder()
                .flatMap(salesOrder -> {
                    Mono<Tuple2<BigDecimal, Boolean>> balanceInfo = orderService.getBalance(1L)
                            .map(balance -> Tuples.of(balance, false))
                            .onErrorReturn(Tuples.of(BigDecimal.ZERO, true));

                    return balanceInfo.map(info -> Rendering.view("cart")
                            .modelAttribute("cart", salesOrderMapper.toDto(salesOrder))
                            .modelAttribute("balanceInfo", info.getT1())
                            .modelAttribute("errorMessage", info.getT2() ? "Оплата недоступна" : "")
                            .modelAttribute("disabled", info.getT2())
                            .build());
                })
                .defaultIfEmpty(Rendering.view("cart")
                        .modelAttribute("cart", new SalesOrderDto(-1L, List.of()))
                        .modelAttribute("balanceInfo", BigDecimal.ZERO)
                        .modelAttribute("disabled", true)
                        .build());
    }

    @PostMapping("/cart/buy/{orderId}")
    public Mono<Rendering> buy(@PathVariable("orderId") Long orderId) {
        return orderService.performBuyOrder(orderId)
                .thenReturn(Rendering.redirectTo("/orders/" + orderId)
                        .modelAttribute("newOrder", true)
                        .build());
    }

    @PostMapping("/cart/items/{showcaseItemId}")
    public Mono<Rendering> updateCountFromCart(@PathVariable("showcaseItemId") Long showcaseItemId,
                                               ServerWebExchange webExchange) {
        return webExchange.getFormData()
                .flatMap(formData -> {
                    String action = formData.getFirst("action");
                    UpdateCountAction enumAction = UpdateCountAction.valueOf(action);
                    return orderService.updateCountForPlannedOrder(showcaseItemId, enumAction)
                            .thenReturn(Rendering.redirectTo("/cart/items").build());
                });
    }
}
