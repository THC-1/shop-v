package com.example.gobuy.modules.cart.controller;

import com.example.gobuy.common.result.Result;
import com.example.gobuy.common.utils.UserContextHolder;
import com.example.gobuy.modules.cart.assembler.CartAssembler;
import com.example.gobuy.modules.cart.dto.CartAddDTO;
import com.example.gobuy.modules.cart.dto.CartUpdateDTO;
import com.example.gobuy.modules.cart.entity.Cart;
import com.example.gobuy.modules.cart.service.ICartService;
import com.example.gobuy.modules.cart.vo.CartItemVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.example.gobuy.modules.product.entity.Product;
import com.example.gobuy.modules.product.entity.Sku;
import com.example.gobuy.modules.product.service.IProductService;
import com.example.gobuy.modules.product.service.ISkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "购物车管理")
@RestController
@RequestMapping("/api/v1/carts/items")
@RequiredArgsConstructor
public class CartController {

    private final ICartService cartService;
    private final CartAssembler cartAssembler;
    private final IProductService productService;
    private final ISkuService skuService;

    @Operation(summary = "添加商品到购物车")
    @PostMapping
    public Result<Void> addItem(@Valid @RequestBody CartAddDTO dto) {
        Long userId = UserContextHolder.getRequiredUserId();
        cartService.addToCart(userId, dto);
        return Result.success();
    }

    @Operation(summary = "获取当前用户购物车列表")
    @GetMapping
    public Result<List<CartItemVO>> list() {
        Long userId = UserContextHolder.getRequiredUserId();
        List<Cart> carts = cartService.listByUserId(userId);
        List<CartItemVO> voList = cartAssembler.toCartItemVOList(carts);

        Set<Long> productIds = carts.stream().map(Cart::getProductId).collect(Collectors.toSet());
        Set<Long> skuIds = carts.stream().map(Cart::getSkuId).collect(Collectors.toSet());

        Map<Long, Product> productMap = productIds.isEmpty() ? Map.of() :
                productService.listByIds(productIds).stream()
                        .collect(Collectors.toMap(Product::getId, p -> p));
        Map<Long, Sku> skuMap = skuIds.isEmpty() ? Map.of() :
                skuService.listByIds(skuIds).stream()
                        .collect(Collectors.toMap(Sku::getId, s -> s));

        for (CartItemVO vo : voList) {
            Product product = productMap.get(vo.getProductId());
            Sku sku = skuMap.get(vo.getSkuId());

            BigDecimal price = sku != null ? sku.getPrice() : (product != null ? product.getPrice() : BigDecimal.ZERO);
            vo.setPrice(price);
            vo.setTotalPrice(price.multiply(BigDecimal.valueOf(vo.getQuantity())));

            if (product != null) {
                vo.setProductName(product.getName());
                vo.setProductImage(product.getImages());
            }
            if (sku != null) {
                vo.setSkuInfo(sku.getSpecValues());
            }
        }

        return Result.success(voList);
    }

    @Operation(summary = "更新购物车项数量")
    @PutMapping("/{id}")
    public Result<Void> updateQuantity(@PathVariable("id") Long id,
                                       @Valid @RequestBody CartUpdateDTO dto) {
        Long userId = UserContextHolder.getRequiredUserId();
        cartService.updateQuantity(userId, id, dto.getQuantity());
        return Result.success();
    }

    @Operation(summary = "删除购物车项")
    @DeleteMapping("/{id}")
    public Result<Void> deleteItem(@PathVariable("id") Long id) {
        Long userId = UserContextHolder.getRequiredUserId();
        cartService.deleteCartItem(userId, id);
        return Result.success();
    }

    @Operation(summary = "选中/取消选中商品")
    @PutMapping("/{id}/select")
    public Result<Void> toggleSelect(@PathVariable("id") Long id) {
        Long userId = UserContextHolder.getRequiredUserId();
        cartService.toggleSelect(userId, id);
        return Result.success();
    }

    @Operation(summary = "清空购物车")
    @DeleteMapping
    public Result<Void> clear() {
        Long userId = UserContextHolder.getRequiredUserId();
        cartService.clearCart(userId);
        return Result.success();
    }

    @Operation(summary = "批量删除购物车项")
    @DeleteMapping("/batch")
    public Result<Void> removeItems(@RequestBody List<Long> cartItemIds) {
        Long userId = UserContextHolder.getRequiredUserId();
        cartService.removeItems(userId, cartItemIds);
        return Result.success();
    }
}
