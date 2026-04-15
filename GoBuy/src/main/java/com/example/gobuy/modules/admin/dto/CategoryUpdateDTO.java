package com.example.gobuy.modules.admin.dto;

import lombok.Data;

@Data
public class CategoryUpdateDTO {
    private String name;
    private Long parentId;
    private Integer sort;
    private String icon;
    private String status;
}
