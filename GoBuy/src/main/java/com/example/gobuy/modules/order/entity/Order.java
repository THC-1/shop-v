package com.example.gobuy.modules.order.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.gobuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("orders")
public class Order extends BaseEntity {

    private String orderNo;

    private Long userId;

    private Long addressId;

    private BigDecimal totalAmount;

    private String status;

    private String note;

    @TableLogic
    private Boolean deleted;
}
