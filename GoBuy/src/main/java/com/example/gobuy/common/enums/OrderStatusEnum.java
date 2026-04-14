package com.example.gobuy.common.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {

    PENDING_PAYMENT(0, "待付款"),
    PAID(1, "已付款"),
    SHIPPED(2, "已发货"),
    DELIVERED(3, "已送达"),
    COMPLETED(4, "已完成"),
    CANCELLED(5, "已取消");

    private final int code;
    private final String description;

    OrderStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
