package com.example.gobuy.modules.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 购物车添加商品 DTO
 */
@Data
public class CartAddDTO {

    /**
     * 商品 ID
     */
    @NotNull(message = "商品 ID 不能为空")
    private Long productId;

    /**
     * SKU ID
     */
    @NotNull(message = "SKU ID 不能为空")
    private Long skuId;

    /**
     * 购买数量
     */
    @NotNull(message = "购买数量不能为空")
    private Integer quantity;
}
