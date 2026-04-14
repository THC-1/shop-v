package com.example.gobuy.modules.payment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.gobuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("payments")
public class Payment extends BaseEntity {

    private Long orderId;

    private String paymentMethod;

    private BigDecimal amount;

    private String status;
}
