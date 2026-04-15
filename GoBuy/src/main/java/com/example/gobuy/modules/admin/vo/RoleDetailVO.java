package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoleDetailVO {

    private Long id;

    private String name;

    private String code;

    private String description;

    private Boolean isSystem;

    private String status;

    private List<PermissionSimpleVO> permissions;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
