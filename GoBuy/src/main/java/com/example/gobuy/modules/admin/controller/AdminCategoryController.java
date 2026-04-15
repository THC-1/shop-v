package com.example.gobuy.modules.admin.controller;

import com.example.gobuy.common.annotation.RequirePermission;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.dto.CategoryCreateDTO;
import com.example.gobuy.modules.admin.dto.CategoryUpdateDTO;
import com.example.gobuy.modules.admin.service.IAdminCategoryService;
import com.example.gobuy.modules.admin.vo.CategoryDetailVO;
import com.example.gobuy.modules.admin.vo.CategoryTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin分类管理")
@RestController
@RequestMapping("/api/v1/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final IAdminCategoryService adminCategoryService;

    @GetMapping
    @RequirePermission("CATEGORY:VIEW")
    @Operation(summary = "分类树")
    public Result<List<CategoryTreeVO>> getCategoryTree() {
        return Result.success(adminCategoryService.getCategoryTree());
    }

    @GetMapping("/{id}")
    @RequirePermission("CATEGORY:VIEW")
    @Operation(summary = "分类详情")
    public Result<CategoryDetailVO> getCategoryDetail(@PathVariable Long id) {
        return Result.success(adminCategoryService.getCategoryDetail(id));
    }

    @PostMapping
    @RequirePermission("CATEGORY:EDIT")
    @Operation(summary = "创建分类")
    public Result<Long> createCategory(@Valid @RequestBody CategoryCreateDTO dto) {
        return Result.success(adminCategoryService.createCategory(dto));
    }

    @PutMapping("/{id}")
    @RequirePermission("CATEGORY:EDIT")
    @Operation(summary = "更新分类")
    public Result<Void> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryUpdateDTO dto) {
        adminCategoryService.updateCategory(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @RequirePermission("CATEGORY:EDIT")
    @Operation(summary = "删除分类")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        adminCategoryService.deleteCategory(id);
        return Result.success();
    }
}
