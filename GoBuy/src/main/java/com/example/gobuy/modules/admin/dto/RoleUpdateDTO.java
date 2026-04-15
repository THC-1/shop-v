package com.example.gobuy.modules.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleUpdateDTO {

    private String name;

    private String description;

    private Integer sort;

    private String status;

    private List<Long> permissionIds;
}
