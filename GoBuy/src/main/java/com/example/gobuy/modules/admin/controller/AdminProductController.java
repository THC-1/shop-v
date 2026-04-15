package com.example.gobuy.modules.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.gobuy.common.annotation.OperationLog;
import com.example.gobuy.common.annotation.RequirePermission;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.dto.AdminProductCreateDTO;
import com.example.gobuy.modules.admin.dto.AdminProductQueryDTO;
import com.example.gobuy.modules.admin.dto.AdminProductUpdateDTO;
import com.example.gobuy.modules.admin.dto.BatchStatusDTO;
import com.example.gobuy.modules.admin.service.IAdminProductService;
import com.example.gobuy.modules.admin.vo.AdminProductDetailVO;
import com.example.gobuy.modules.admin.vo.AdminProductVO;
import com.example.gobuy.modules.admin.vo.BatchStatusResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Admin商品管理")
@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final IAdminProductService adminProductService;

    @GetMapping
    @RequirePermission("PRODUCT:VIEW")
    @Operation(summary = "商品列表")
    public Result<IPage<AdminProductVO>> listProducts(AdminProductQueryDTO queryDTO) {
        return adminProductService.listProducts(queryDTO);
    }

    @GetMapping("/{id}")
    @RequirePermission("PRODUCT:VIEW")
    @Operation(summary = "商品详情")
    public Result<AdminProductDetailVO> getProductDetail(@PathVariable Long id) {
        return adminProductService.getProductDetail(id);
    }

    @PostMapping
    @RequirePermission("PRODUCT:EDIT")
    @OperationLog(module = "商品", action = "创建商品", targetType = "Product")
    @Operation(summary = "创建商品")
    public Result<Long> createProduct(@Valid @RequestBody AdminProductCreateDTO dto) {
        return adminProductService.createProduct(dto);
    }

    @PutMapping("/{id}")
    @RequirePermission("PRODUCT:EDIT")
    @OperationLog(module = "商品", action = "更新商品", targetType = "Product")
    @Operation(summary = "更新商品")
    public Result<Void> updateProduct(@PathVariable Long id, @Valid @RequestBody AdminProductUpdateDTO dto) {
        return adminProductService.updateProduct(id, dto);
    }

    @DeleteMapping("/{id}")
    @RequirePermission("PRODUCT:EDIT")
    @OperationLog(module = "商品", action = "删除商品", targetType = "Product")
    @Operation(summary = "删除商品")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        return adminProductService.deleteProduct(id);
    }

    @PatchMapping("/{id}/status")
    @RequirePermission("PRODUCT:STATUS")
    @OperationLog(module = "商品", action = "更新商品状态", targetType = "Product")
    @Operation(summary = "更新商品状态")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        return adminProductService.updateStatus(id, status);
    }

    @PostMapping("/batch/status")
    @RequirePermission("PRODUCT:STATUS")
    @OperationLog(module = "商品", action = "批量更新商品状态", targetType = "Product")
    @Operation(summary = "批量更新商品状态")
    public Result<BatchStatusResult> batchUpdateStatus(@Valid @RequestBody BatchStatusDTO dto) {
        return adminProductService.batchUpdateStatus(dto);
    }
}
