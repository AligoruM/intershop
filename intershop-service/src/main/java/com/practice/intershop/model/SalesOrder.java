package com.practice.intershop.model;

import com.practice.intershop.enums.OrderStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.List;

@Data
@Table("sales_order")
public class SalesOrder {

    @Id
    private Long id;

    @Column("order_status")
    private OrderStatus orderStatus;

    @Column("created_at")
    private Instant createdAt;

    @Transient
    private List<OrderItem> orderItems;
}
