package com.example.gobuy.modules.admin.controller;

import com.example.gobuy.common.annotation.RequirePermission;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.dto.PermissionAssignDTO;
import com.example.gobuy.modules.admin.dto.RoleCreateDTO;
import com.example.gobuy.modules.admin.dto.RoleQueryDTO;
import com.example.gobuy.modules.admin.dto.RoleUpdateDTO;
import com.example.gobuy.modules.admin.service.IRoleService;
import com.example.gobuy.modules.admin.vo.RoleDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/v1/admin/roles")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleService roleService;

    @GetMapping
    @RequirePermission("ROLE:VIEW")
    @Operation(summary = "获取角色列表")
    public Result<Map<String, Object>> listRoles(RoleQueryDTO dto) {
        return roleService.listRoles(dto);
    }

    @GetMapping("/{id}")
    @RequirePermission("ROLE:VIEW")
    @Operation(summary = "获取角色详情")
    public Result<RoleDetailVO> getRoleDetail(@PathVariable Long id) {
        return roleService.getRoleDetail(id);
    }

    @PostMapping
    @RequirePermission("ROLE:EDIT")
    @Operation(summary = "创建角色")
    public Result<Map<String, Object>> createRole(@Valid @RequestBody RoleCreateDTO dto) {
        return roleService.createRole(dto);
    }

    @PutMapping("/{id}")
    @RequirePermission("ROLE:EDIT")
    @Operation(summary = "更新角色")
    public Result<Void> updateRole(@PathVariable Long id, @Valid @RequestBody RoleUpdateDTO dto) {
        return roleService.updateRole(id, dto);
    }

    @DeleteMapping("/{id}")
    @RequirePermission("ROLE:EDIT")
    @Operation(summary = "删除角色")
    public Result<Void> deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }

    @PostMapping("/{roleId}/permissions")
    @RequirePermission("ROLE:EDIT")
    @Operation(summary = "分配权限")
    public Result<Void> assignPermissions(@PathVariable Long roleId, @Valid @RequestBody PermissionAssignDTO dto) {
        return roleService.assignPermissions(roleId, dto);
    }
}
