package com.example.gobuy.modules.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("admin_login_logs")
public class AdminLoginLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long adminId;

    private String username;

    private String ip;

    private String userAgent;

    private String status;

    private String failReason;

    private LocalDateTime loginAt;
}
