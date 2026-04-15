package com.example.gobuy.modules.admin.controller;

import com.example.gobuy.common.annotation.RequirePermission;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.service.IRoleService;
import com.example.gobuy.modules.admin.vo.PermissionTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "权限管理")
@RestController
@RequestMapping("/api/v1/admin/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final IRoleService roleService;

    @GetMapping
    @RequirePermission("PERMISSION:VIEW")
    @Operation(summary = "获取权限树")
    public Result<List<PermissionTreeVO>> getPermissionTree() {
        return roleService.getPermissionTree();
    }
}
