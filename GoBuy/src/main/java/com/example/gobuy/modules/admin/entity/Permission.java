package com.example.gobuy.modules.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.gobuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("permissions")
public class Permission extends BaseEntity {

    private String name;

    private String code;

    private String type;

    private Long parentId;

    private String path;

    private String icon;

    private Integer sort;

    private String status;
}
