package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminOrderDetailVO {
    private Long id;
    private String orderNo;
    private Long userId;
    private String username;
    private String receiverName;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private BigDecimal totalAmount;
    private String status;
    private String expressCompany;
    private String expressNo;
    private LocalDateTime shippedAt;
    private String note;
    private List<OrderItemVO> items;
    private LocalDateTime createdAt;
}
