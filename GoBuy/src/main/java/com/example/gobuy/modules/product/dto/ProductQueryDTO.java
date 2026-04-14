package com.example.gobuy.modules.product.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 商品查询 DTO
 */
@Data
public class ProductQueryDTO {

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 分类 ID
     */
    private Long categoryId;

    /**
     * 品牌 ID
     */
    private Long brandId;

    /**
     * 最低价格
     */
    private BigDecimal minPrice;

    /**
     * 最高价格
     */
    private BigDecimal maxPrice;

    /**
     * 排序字段：price/sales/createdAt
     */
    private String sortField;

    /**
     * 排序方式：asc/desc
     */
    private String sortOrder;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    private Integer pageSize = 10;
}
