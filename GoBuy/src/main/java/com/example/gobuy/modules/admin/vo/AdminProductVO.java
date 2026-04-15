package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdminProductVO {
    private Long id;
    private String name;
    private String categoryName;
    private String brandName;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private Integer salesCount;
    private String status;
    private LocalDateTime createdAt;
}
