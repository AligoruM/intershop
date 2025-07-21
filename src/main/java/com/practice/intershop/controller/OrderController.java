package com.practice.intershop.controller;

/*import com.practice.intershop.dto.SalesOrderDto;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final SalesOrderMapper salesOrderMapper;

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", salesOrderMapper.toDto(orderService.findAllCompletedOrders()));
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String order(@PathVariable Long id, Model model) {
        model.addAttribute("salesOrder", salesOrderMapper.toDto(orderService.findSalesOrder(id)));
        return "order";
    }

    @PostMapping({"/main/items/{showcaseItemId}", "/items/{showcaseItemId}"})
    public String changeItem(@PathVariable Long showcaseItemId,
                             @RequestParam(value = "action") UpdateCountAction action) {
        orderService.updateCountForPlannedOrder(showcaseItemId, action);
        return "redirect:/main/items/" + showcaseItemId;
    }

    @GetMapping("/cart/items")
    public String cartItems(Model model) {
        Optional<SalesOrder> plannedSalesOrder = orderService.findPlannedSalesOrder();
        plannedSalesOrder.map(salesOrderMapper::toDto)
                .ifPresentOrElse(
                        order -> model.addAttribute("cart", order),
                        () -> {
                            SalesOrderDto salesOrderDto = new SalesOrderDto();
                            salesOrderDto.setOrderItems(List.of());
                            model.addAttribute("cart", salesOrderDto);
                        });
        return "cart";
    }

    @PostMapping("/cart/buy/{orderId}")
    public String buy(@PathVariable Long orderId, RedirectAttributes redirectAttributes) {
        orderService.performBuyOrder(orderId);
        redirectAttributes.addFlashAttribute("newOrder", true);
        return "redirect:/orders/" + orderId;
    }

    @PostMapping("/cart/items/{showcaseItemId}")
    public String updateCount(@PathVariable Long showcaseItemId,
                              @RequestParam(value = "action") UpdateCountAction action) {
        orderService.updateCountForPlannedOrder(showcaseItemId, action);
        return "redirect:/cart/items";
    }
}*/
