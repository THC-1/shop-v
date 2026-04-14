package com.example.gobuy.modules.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.gobuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("brands")
public class Brand extends BaseEntity {

    private String name;

    private String logo;

    private String description;
}
