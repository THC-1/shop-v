package com.example.gobuy.modules.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("daily_stats")
public class DailyStats {

    @TableId(type = IdType.AUTO)
    private Long id;

    private LocalDate statDate;
    private Integer totalOrders;
    private Integer totalUsers;
    private Integer totalProducts;
    private Integer uv;
    private Integer pv;
    private LocalDateTime createdAt;
}
