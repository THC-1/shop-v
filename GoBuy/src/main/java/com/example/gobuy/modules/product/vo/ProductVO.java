package com.example.gobuy.modules.product.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 产品表 VO (视图对象)
 */
@Data
public class ProductVO {

    /**
     * 产品ID
     */
    private Long id;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 详细描述/富文本
     */
    private String description;

    /**
     * 轮播图列表 (URL数组)
     */
    private String images;

    /**
     * 商品规格属性（如颜色、内存等）
     */
    private String attributes;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 售价
     */
    private BigDecimal price;

    /**
     * 当前库存
     */
    private Integer stock;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;


}