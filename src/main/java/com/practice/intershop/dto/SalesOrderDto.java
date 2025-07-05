package com.practice.intershop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SalesOrderDto {

    private Long id;

    private List<OrderItemDto> orderItems;

    public BigDecimal getTotalSum() {
        return orderItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
