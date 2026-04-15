package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesSummaryVO {
    private Integer totalOrders;
    private BigDecimal totalAmount;
    private Integer totalUsers;
    private BigDecimal avgOrderAmount;
}
