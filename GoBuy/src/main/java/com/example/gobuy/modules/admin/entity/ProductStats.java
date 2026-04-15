package com.example.gobuy.modules.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("product_stats")
public class ProductStats {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long productId;
    private LocalDate statDate;
    private Integer uv;
    private Integer pv;
    private Integer cartCount;
    private Integer orderCount;
    private Integer refundCount;
    private LocalDateTime createdAt;
}
