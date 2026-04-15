package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminDetailVO {

    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String phone;

    private String status;

    private List<RoleSimpleVO> roles;

    private LocalDateTime lastLoginAt;

    private String lastLoginIp;

    private LocalDateTime createdAt;
}
