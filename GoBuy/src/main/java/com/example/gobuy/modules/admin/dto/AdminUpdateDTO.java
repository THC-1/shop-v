package com.example.gobuy.modules.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminUpdateDTO {

    private String nickname;

    private String email;

    private String phone;

    private String status;

    private List<Long> roleIds;
}
