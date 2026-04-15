package com.example.gobuy.modules.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class RoleCreateDTO {

    @NotBlank(message = "角色名称不能为空")
    private String name;

    @NotBlank(message = "角色代码不能为空")
    private String code;

    private String description;

    private Integer sort;

    private List<Long> permissionIds;
}
