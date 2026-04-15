package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdminUserVO {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String status;
    private Integer orderCount;
    private BigDecimal totalSpent;
    private LocalDateTime createdAt;
}
