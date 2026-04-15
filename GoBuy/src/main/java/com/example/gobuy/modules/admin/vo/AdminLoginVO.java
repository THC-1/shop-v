package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.util.List;

@Data
public class AdminLoginVO {

    private String token;

    private Long adminId;

    private String username;

    private String nickname;

    private List<String> roles;
}
