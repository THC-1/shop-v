package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UvPvVO {
    private Integer totalUv;
    private Integer totalPv;
    private BigDecimal avgPvPerUv;
    private List<UvPvTrendVO> dailyList;
}
