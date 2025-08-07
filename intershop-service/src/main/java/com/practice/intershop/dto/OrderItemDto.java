package com.practice.intershop.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {

    private Long id;

    private ShowcaseItemDto showcaseItem;

    private int quantity;

    private BigDecimal unitPrice;

}
