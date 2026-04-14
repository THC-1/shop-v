package com.example.gobuy.modules.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateDTO {

    @NotNull(message = "收货地址不能为空")
    private Long addressId;

    @NotEmpty(message = "订单项不能为空")
    private List<OrderItemCreateDTO> items;

    private String note;
}
