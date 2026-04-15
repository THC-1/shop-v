package com.example.gobuy.modules.admin.controller;

import com.example.gobuy.common.annotation.RequirePermission;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.dto.AdminCreateDTO;
import com.example.gobuy.modules.admin.dto.AdminQueryDTO;
import com.example.gobuy.modules.admin.dto.AdminUpdateDTO;
import com.example.gobuy.modules.admin.service.IAdminService;
import com.example.gobuy.modules.admin.vo.AdminDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "管理员管理")
@RestController
@RequestMapping("/api/v1/admin/admins")
@RequiredArgsConstructor
public class AdminController {

    private final IAdminService adminService;

    @GetMapping
    @RequirePermission("ADMIN:VIEW")
    @Operation(summary = "获取管理员列表")
    public Result<Map<String, Object>> listAdmins(AdminQueryDTO dto) {
        return adminService.listAdmins(dto);
    }

    @GetMapping("/{id}")
    @RequirePermission("ADMIN:VIEW")
    @Operation(summary = "获取管理员详情")
    public Result<AdminDetailVO> getAdminDetail(@PathVariable Long id) {
        return adminService.getAdminDetail(id);
    }

    @PostMapping
    @RequirePermission("ADMIN:EDIT")
    @Operation(summary = "创建管理员")
    public Result<Map<String, Object>> createAdmin(@Valid @RequestBody AdminCreateDTO dto) {
        return adminService.createAdmin(dto);
    }

    @PutMapping("/{id}")
    @RequirePermission("ADMIN:EDIT")
    @Operation(summary = "更新管理员")
    public Result<Void> updateAdmin(@PathVariable Long id, @Valid @RequestBody AdminUpdateDTO dto) {
        return adminService.updateAdmin(id, dto);
    }

    @DeleteMapping("/{id}")
    @RequirePermission("ADMIN:EDIT")
    @Operation(summary = "删除管理员")
    public Result<Void> deleteAdmin(@PathVariable Long id) {
        return adminService.deleteAdmin(id);
    }
}
