package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryTreeVO {
    private Long id;
    private String name;
    private Long parentId;
    private Integer sort;
    private String icon;
    private String status;
    private java.util.List<CategoryTreeVO> children = new java.util.ArrayList<>();
}
