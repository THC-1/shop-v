package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UvPvTrendVO {
    private LocalDate date;
    private Integer uv;
    private Integer pv;
}
