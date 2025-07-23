package com.practice.intershop.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Table(name = "showcase_item")
public class ShowcaseItem {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("image_path")
    private String imagePath;

    @Column("price")
    private BigDecimal price;

    @Transient
    private Integer count;

}
