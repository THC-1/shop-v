package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.util.List;

@Data
public class PermissionTreeVO {

    private Long id;

    private String name;

    private String code;

    private String type;

    private Long parentId;

    private String path;

    private String icon;

    private Integer sort;

    private List<PermissionTreeVO> children;
}
