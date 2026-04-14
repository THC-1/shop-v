package com.example.gobuy.modules.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.product.assembler.ProductAssembler;
import com.example.gobuy.modules.product.dto.ProductDTO;
import com.example.gobuy.modules.product.dto.ProductQueryDTO;
import com.example.gobuy.modules.product.entity.Product;
import com.example.gobuy.modules.product.service.IProductService;
import com.example.gobuy.modules.product.vo.ProductVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品管理", description = "商品查询、管理等相关接口")
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;
    private final ProductAssembler productAssembler;

    @GetMapping
    @Operation(summary = "获取商品列表", description = "获取所有商品信息（公开接口，无需认证）")
    public Result<List<ProductVO>> list() {
        List<Product> list = productService.list();
        return Result.success(productAssembler.toVOList(list));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取商品详情", description = "根据商品 ID 获取详细信息（公开接口）")
    public Result<ProductVO> getById(
            @Parameter(description = "商品 ID", example = "1") @PathVariable("id") Long id) {
        Product entity = productService.getById(id);
        if (entity == null) {
            return Result.fail(404, "未找到相关数据");
        }
        return Result.success(productAssembler.toVO(entity));
    }

    @PostMapping
    @Operation(summary = "创建商品", description = "创建新商品")
    public Result<Void> create(@Valid @RequestBody ProductDTO dto) {
        Product entity = productAssembler.toEntity(dto);
        productService.save(entity);
        return Result.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新商品信息", description = "根据商品 ID 更新商品信息")
    public Result<Void> update(
            @Parameter(description = "商品 ID", example = "1") @PathVariable("id") Long id,
            @Valid @RequestBody ProductDTO dto) {
        Product entity = productAssembler.toEntity(dto);
        entity.setId(id);
        productService.updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品", description = "根据商品 ID 删除商品")
    public Result<Void> delete(
            @Parameter(description = "商品 ID", example = "1") @PathVariable("id") Long id) {
        productService.removeById(id);
        return Result.success();
    }

    @GetMapping("/search")
    @Operation(summary = "商品搜索", description = "多条件商品搜索：关键词、分类、价格区间、排序（公开接口，无需认证）")
    public Result<IPage<ProductVO>> search(
            ProductQueryDTO dto,
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量", example = "10") @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<ProductVO> page = productService.searchProducts(dto, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "按分类查询商品", description = "根据分类 ID 查询商品列表")
    public Result<IPage<ProductVO>> getCategoryProducts(
            @Parameter(description = "分类 ID", example = "1") @PathVariable("categoryId") Long categoryId,
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量", example = "10") @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<ProductVO> page = productService.getCategoryProducts(categoryId, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/{id}/stock")
    @Operation(summary = "查询商品库存", description = "根据商品 ID 查询当前库存数量")
    public Result<Integer> getStock(
            @Parameter(description = "商品 ID", example = "1") @PathVariable("id") Long id) {
        Integer stock = productService.getStock(id);
        return Result.success(stock);
    }

    @PutMapping("/{id}/stock")
    @Operation(summary = "更新商品库存", description = "根据商品 ID 更新库存数量")
    public Result<Void> updateStock(
            @Parameter(description = "商品 ID", example = "1") @PathVariable("id") Long id,
            @Parameter(description = "库存数量", example = "100") @RequestParam Integer stock) {
        productService.updateStock(id, stock);
        return Result.success();
    }
}
