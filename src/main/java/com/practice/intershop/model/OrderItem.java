package com.practice.intershop.model;

import com.practice.intershop.enums.OrderItemStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Table("order_item")
public class OrderItem {

    @Id
    private Long id;

    @Column("sales_order_id")
    private Long salesOrderId;

    @Column("showcase_item_id")
    private Long showcaseItemId;

    @Column("quantity")
    private int quantity;

    @Column("unit_price")
    private BigDecimal unitPrice;

    @Column("status")
    private OrderItemStatus status;

    @Transient
    private ShowcaseItem showcaseItem;

    @Transient
    private SalesOrder salesOrder;
}
