package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TopProductVO {
    private Integer rank;
    private Long productId;
    private String productName;
    private String brandName;
    private Integer salesCount;
    private BigDecimal salesAmount;
}
