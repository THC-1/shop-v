package com.example.gobuy.modules.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RoleQueryDTO {

    @Min(value = 1, message = "页码最小为1")
    private Integer page = 1;

    @Max(value = 100, message = "每页最大100条")
    private Integer size = 10;

    private String name;

    private String status;
}
