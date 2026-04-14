package com.example.gobuy.modules.product.controller;

import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.product.entity.Sku;
import com.example.gobuy.modules.product.service.ISkuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "SKU管理", description = "SKU规格查询等相关接口")
@RestController
@RequestMapping("/api/v1/skus")
@RequiredArgsConstructor
public class SkuController {

    private final ISkuService skuService;

    @GetMapping("/product/{productId}")
    @Operation(summary = "获取商品的SKU列表", description = "根据商品ID获取所有SKU规格")
    public Result<List<Sku>> getSkusByProductId(
            @Parameter(description = "商品 ID", example = "1") @PathVariable("productId") Long productId) {
        List<Sku> skus = skuService.getSkusByProductId(productId);
        return Result.success(skus);
    }
}