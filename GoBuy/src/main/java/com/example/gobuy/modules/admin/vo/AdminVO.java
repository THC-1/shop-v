package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminVO {

    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String phone;

    private String status;

    private List<String> roles;

    private LocalDateTime lastLoginAt;

    private LocalDateTime createdAt;
}
