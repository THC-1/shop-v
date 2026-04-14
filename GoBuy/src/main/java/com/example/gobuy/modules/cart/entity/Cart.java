package com.example.gobuy.modules.cart.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.gobuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("carts")
public class Cart extends BaseEntity {

    private Long userId;

    private Long productId;

    private Long skuId;

    private Integer quantity;

    private Boolean selected;
}
