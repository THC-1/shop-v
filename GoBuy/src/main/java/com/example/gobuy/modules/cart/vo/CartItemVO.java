package com.example.gobuy.modules.cart.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车项 VO
 */
@Data
public class CartItemVO {

    /**
     * 购物车项 ID
     */
    private Long id;

    /**
     * 商品 ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品图片
     */
    private String productImage;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * SKU 规格信息
     */
    private String skuInfo;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 是否选中
     */
    private Boolean selected;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
