package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdminProductDetailVO {
    private Long id;
    private String name;
    private String description;
    private String images;
    private String attributes;
    private BigDecimal originalPrice;
    private BigDecimal price;
    private Integer stock;
    private Integer salesCount;
    private Long categoryId;
    private String categoryName;
    private Long brandId;
    private String brandName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
