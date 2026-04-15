package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryTreeVO {
    private Long id;
    private String name;
    private Long parentId;
    private Integer sort;
    private String icon;
    private String status;
    private List<CategoryTreeVO> children = new ArrayList<>();
}
