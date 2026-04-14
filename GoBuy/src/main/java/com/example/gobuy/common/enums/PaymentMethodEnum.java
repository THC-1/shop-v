package com.example.gobuy.common.enums;

import lombok.Getter;

@Getter
public enum PaymentMethodEnum {

    ALIPAY(1, "支付宝"),
    WECHAT(2, "微信支付"),
    BANK_CARD(3, "银行卡");

    private final int code;
    private final String description;

    PaymentMethodEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
