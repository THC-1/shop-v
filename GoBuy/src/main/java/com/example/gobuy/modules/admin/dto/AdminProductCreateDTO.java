package com.example.gobuy.modules.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdminProductCreateDTO {
    @NotBlank(message = "商品名称不能为空")
    private String name;
    private String description;
    private String images;
    private String attributes;
    private BigDecimal originalPrice;
    @NotNull(message = "售价不能为空")
    private BigDecimal price;
    private Integer stock = 0;
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
    private Long brandId;
}
