package com.example.gobuy.modules.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.gobuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("skus")
public class Sku extends BaseEntity {

    private Long productId;

    private String name;

    private String specValues;

    private BigDecimal price;

    private Integer stock;

    private String image;
}
