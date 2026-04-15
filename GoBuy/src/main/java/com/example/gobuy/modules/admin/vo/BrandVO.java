package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BrandVO {
    private Long id;
    private String name;
    private String logo;
    private String description;
    private Integer productCount;
    private LocalDateTime createdAt;
}
