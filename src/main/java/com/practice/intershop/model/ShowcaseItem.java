package com.practice.intershop.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "showcase_item")
public class ShowcaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "price")
    private BigDecimal price;

    @Formula("(SELECT COALESCE(oi.quantity, 0) FROM order_item oi WHERE oi.showcase_item_id = id and oi.status = 0)")
    private Integer count;

}
