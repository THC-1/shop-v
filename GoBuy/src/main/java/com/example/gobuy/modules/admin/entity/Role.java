package com.example.gobuy.modules.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.gobuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("roles")
public class Role extends BaseEntity {

    private String name;

    private String code;

    private String description;

    private Integer sort;

    private String status;

    private Integer isSystem;
}
