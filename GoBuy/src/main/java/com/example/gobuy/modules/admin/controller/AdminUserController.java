package com.example.gobuy.modules.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.gobuy.common.annotation.OperationLog;
import com.example.gobuy.common.annotation.RequirePermission;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.dto.AdminUserQueryDTO;
import com.example.gobuy.modules.admin.dto.UserRoleDTO;
import com.example.gobuy.modules.admin.dto.UserStatusDTO;
import com.example.gobuy.modules.admin.service.IAdminUserService;
import com.example.gobuy.modules.admin.vo.AdminUserDetailVO;
import com.example.gobuy.modules.admin.vo.AdminUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin用户管理")
@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final IAdminUserService adminUserService;

    @GetMapping
    @RequirePermission("USER:VIEW")
    @Operation(summary = "用户列表")
    public Result<IPage<AdminUserVO>> listUsers(AdminUserQueryDTO queryDTO) {
        return adminUserService.listUsers(queryDTO);
    }

    @GetMapping("/{id}")
    @RequirePermission("USER:VIEW")
    @Operation(summary = "用户详情")
    public Result<AdminUserDetailVO> getUserDetail(@PathVariable Long id) {
        return adminUserService.getUserDetail(id);
    }

    @PatchMapping("/{id}/status")
    @RequirePermission("USER:EDIT")
    @OperationLog(module = "用户", action = "更新用户状态", targetType = "User")
    @Operation(summary = "更新用户状态")
    public Result<Void> updateUserStatus(@PathVariable Long id, @Valid @RequestBody UserStatusDTO dto) {
        return adminUserService.updateUserStatus(id, dto);
    }

    @PostMapping("/{id}/roles")
    @RequirePermission("USER:EDIT")
    @OperationLog(module = "用户", action = "分配用户角色", targetType = "User")
    @Operation(summary = "分配用户角色")
    public Result<Void> assignUserRoles(@PathVariable Long id, @Valid @RequestBody UserRoleDTO dto) {
        return adminUserService.assignUserRoles(id, dto);
    }
}
