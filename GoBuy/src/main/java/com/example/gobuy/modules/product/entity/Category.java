package com.example.gobuy.modules.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.gobuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("categories")
public class Category extends BaseEntity {

    private String name;

    private Long parentId;

    private Integer sort;

    private String icon;
}
