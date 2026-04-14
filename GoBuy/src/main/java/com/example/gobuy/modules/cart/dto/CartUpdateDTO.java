package com.example.gobuy.modules.cart.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 购物车更新商品 DTO
 */
@Data
public class CartUpdateDTO {

    /**
     * 购买数量
     */
    @Min(value = 1, message = "购买数量必须大于 0")
    private Integer quantity;
}
