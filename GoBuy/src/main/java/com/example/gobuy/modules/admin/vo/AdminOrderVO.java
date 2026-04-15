package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdminOrderVO {
    private Long id;
    private String orderNo;
    private Long userId;
    private String username;
    private BigDecimal totalAmount;
    private String status;
    private String expressCompany;
    private String expressNo;
    private LocalDateTime createdAt;
}
