package com.example.gobuy.modules.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.gobuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("products")
public class Product extends BaseEntity {

    private String name;

    private String description;

    private String images;

    private String attributes;

    private BigDecimal originalPrice;

    private BigDecimal price;

    private Integer stock;

    private Long categoryId;

    private Long brandId;
}
