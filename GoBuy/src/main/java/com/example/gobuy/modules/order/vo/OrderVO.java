package com.example.gobuy.modules.order.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 订单表 VO (视图对象)
 */
@Data
public class OrderVO {

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 买家ID
     */
    private Long userId;

    /**
     * 收货地址ID
     */
    private Long addressId;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 订单备注
     */
    private String note;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;


}