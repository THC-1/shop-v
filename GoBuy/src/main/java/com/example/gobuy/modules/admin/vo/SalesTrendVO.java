package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SalesTrendVO {
    private LocalDate date;
    private Integer orderCount;
    private BigDecimal orderAmount;
    private Integer userCount;
}
