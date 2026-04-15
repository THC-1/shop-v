package com.example.gobuy.modules.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.gobuy.common.annotation.RequirePermission;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.dto.BrandCreateDTO;
import com.example.gobuy.modules.admin.dto.BrandQueryDTO;
import com.example.gobuy.modules.admin.dto.BrandUpdateDTO;
import com.example.gobuy.modules.admin.service.IAdminBrandService;
import com.example.gobuy.modules.admin.vo.BrandDetailVO;
import com.example.gobuy.modules.admin.vo.BrandVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin品牌管理")
@RestController
@RequestMapping("/api/v1/admin/brands")
@RequiredArgsConstructor
public class AdminBrandController {

    private final IAdminBrandService adminBrandService;

    @GetMapping
    @RequirePermission("BRAND:VIEW")
    @Operation(summary = "品牌列表")
    public Result<IPage<BrandVO>> listBrands(BrandQueryDTO queryDTO) {
        return adminBrandService.listBrands(queryDTO);
    }

    @GetMapping("/{id}")
    @RequirePermission("BRAND:VIEW")
    @Operation(summary = "品牌详情")
    public Result<BrandDetailVO> getBrandDetail(@PathVariable Long id) {
        return adminBrandService.getBrandDetail(id);
    }

    @PostMapping
    @RequirePermission("BRAND:EDIT")
    @Operation(summary = "创建品牌")
    public Result<Long> createBrand(@Valid @RequestBody BrandCreateDTO dto) {
        return adminBrandService.createBrand(dto);
    }

    @PutMapping("/{id}")
    @RequirePermission("BRAND:EDIT")
    @Operation(summary = "更新品牌")
    public Result<Void> updateBrand(@PathVariable Long id, @Valid @RequestBody BrandUpdateDTO dto) {
        return adminBrandService.updateBrand(id, dto);
    }

    @DeleteMapping("/{id}")
    @RequirePermission("BRAND:EDIT")
    @Operation(summary = "删除品牌")
    public Result<Void> deleteBrand(@PathVariable Long id) {
        return adminBrandService.deleteBrand(id);
    }
}
