package com.practice.intershop.controller;

import com.practice.intershop.enums.UpdateCountAction;
import com.practice.intershop.mapper.SalesOrderMapper;
import com.practice.intershop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

}
