package com.example.gobuy.modules.payment.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 支付记录表 VO (视图对象)
 */
@Data
public class PaymentVO {

    /**
     * 支付ID
     */
    private Long id;

    /**
     * 关联的订单ID
     */
    private Long orderId;

    /**
     * 支付方式（如alipay, wechat等）
     */
    private String paymentMethod;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 支付状态
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;


}