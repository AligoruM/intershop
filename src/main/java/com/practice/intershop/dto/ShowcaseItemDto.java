package com.practice.intershop.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShowcaseItemDto {
    private Long id;

    private String name;

    private String description;

    private String imagePath;

    private BigDecimal price;

    private int count;

    public String getImageUrl() {
        if (imagePath != null && !imagePath.isBlank()) {
            return imagePath;
        } else {
            return "/showcaseImages/placeholder.svg";
        }
    }
}
