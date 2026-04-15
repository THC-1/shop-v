package com.example.gobuy.modules.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("admin_roles")
public class AdminRole {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long adminId;

    private Long roleId;

    private LocalDateTime createdAt;
}
