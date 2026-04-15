package com.example.gobuy.modules.admin.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdminProductUpdateDTO {
    private String name;
    private String description;
    private String images;
    private String attributes;
    private BigDecimal originalPrice;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;
    private Long brandId;
}
