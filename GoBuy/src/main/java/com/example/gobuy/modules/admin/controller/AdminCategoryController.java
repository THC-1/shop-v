package com.example.gobuy.modules.admin.controller;

import com.example.gobuy.common.annotation.OperationLog;
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
        return adminCategoryService.getCategoryTree();
    }

    @GetMapping("/{id}")
    @RequirePermission("CATEGORY:VIEW")
    @Operation(summary = "分类详情")
    public Result<CategoryDetailVO> getCategoryDetail(@PathVariable Long id) {
        return adminCategoryService.getCategoryDetail(id);
    }

    @PostMapping
    @RequirePermission("CATEGORY:EDIT")
    @OperationLog(module = "分类", action = "创建分类", targetType = "Category")
    @Operation(summary = "创建分类")
    public Result<Long> createCategory(@Valid @RequestBody CategoryCreateDTO dto) {
        return adminCategoryService.createCategory(dto);
    }

    @PutMapping("/{id}")
    @RequirePermission("CATEGORY:EDIT")
    @OperationLog(module = "分类", action = "更新分类", targetType = "Category")
    @Operation(summary = "更新分类")
    public Result<Void> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryUpdateDTO dto) {
        return adminCategoryService.updateCategory(id, dto);
    }

    @DeleteMapping("/{id}")
    @RequirePermission("CATEGORY:EDIT")
    @OperationLog(module = "分类", action = "删除分类", targetType = "Category")
    @Operation(summary = "删除分类")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        return adminCategoryService.deleteCategory(id);
    }
}
