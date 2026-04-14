package com.example.gobuy.modules.product.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SkuVO {
    private Long id;
    private Long productId;
    private String name;
    private String specValues;
    private BigDecimal price;
    private Integer stock;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}