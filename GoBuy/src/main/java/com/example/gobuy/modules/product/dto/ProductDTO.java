package com.example.gobuy.modules.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {

    @NotBlank(message = "商品名称不能为空")
    private String name;

    private String description;

    private String images;

    private String attributes;

    private BigDecimal originalPrice;

    @NotNull(message = "商品价格不能为空")
    private BigDecimal price;

    @NotNull(message = "商品库存不能为空")
    private Integer stock;

    private Long categoryId;

    private Long brandId;
}
