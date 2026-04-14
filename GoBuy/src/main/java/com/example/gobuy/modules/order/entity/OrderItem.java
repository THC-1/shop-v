package com.example.gobuy.modules.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.gobuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("order_items")
public class OrderItem extends BaseEntity {

    private Long orderId;

    private Long productId;

    private Long skuId;

    private String productName;

    private BigDecimal price;

    private Integer quantity;
}
