package com.example.gobuy.modules.admin.dto;

import lombok.Data;

@Data
public class CategoryCreateDTO {
    @jakarta.validation.constraints.NotBlank(message = "分类名称不能为空")
    private String name;
    private Long parentId;
    private Integer sort = 0;
    private String icon;
}
