package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoleVO {

    private Long id;

    private String name;

    private String code;

    private String description;

    private Integer isSystem;

    private String status;

    private Integer userCount;

    private LocalDateTime createdAt;
}
