package com.practice.intershop.controller;

import com.practice.intershop.dto.OrderItemDto;
import com.practice.intershop.enums.UpdateCountAction;
import com.practice.intershop.mapper.SalesOrderMapper;
import com.practice.intershop.model.SalesOrder;
import com.practice.intershop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final OrderService orderService;
    private final SalesOrderMapper salesOrderMapper;

    @GetMapping("/cart/items")
    public String cartItems(Model model) {
        Optional<SalesOrder> plannedSalesOrder = orderService.findPlannedSalesOrder();
        plannedSalesOrder.map(salesOrderMapper::toDto)
                .ifPresentOrElse(order -> {
                            List<OrderItemDto> orderItemDtos = order.getOrderItems();
                            model.addAttribute("empty", orderItemDtos.isEmpty());
                            model.addAttribute("items", orderItemDtos);
                            model.addAttribute("total", order.getTotalSum());
                        },
                        () -> {
                            model.addAttribute("empty", true);
                            model.addAttribute("total", BigDecimal.ZERO);
                        });
        return "cart";
    }

    @PostMapping("/cart/buy")
    public String buy(Model model) {
        Optional<SalesOrder> plannedSalesOrder = orderService.findPlannedSalesOrder();
        if (plannedSalesOrder.isPresent()) {
            orderService.performBuyOrder(plannedSalesOrder.get());
        } else {
            throw new RuntimeException("Order not found");
        }
        model.addAttribute("newOrder", true);
        return "redirect:/orders/" + plannedSalesOrder.get().getId();
    }

    @PostMapping("/cart/items/{showcaseItemId}")
    public String updateCount(@PathVariable Long showcaseItemId,
                              @RequestParam(value = "action") UpdateCountAction action) {
        orderService.updateCountForPlannedOrder(showcaseItemId, action);
        return "redirect:/cart/items";
    }

}
