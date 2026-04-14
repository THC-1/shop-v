package com.example.gobuy.modules.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemCreateDTO {

    @NotNull(message = "商品 ID 不能为空")
    private Long productId;

    @NotNull(message = "SKU ID 不能为空")
    private Long skuId;

    private String productName;

    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量必须大于 0")
    private Integer quantity;

    @NotNull(message = "单价不能为空")
    private BigDecimal price;
}
