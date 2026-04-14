package com.example.gobuy.modules.cart.service;

import com.example.gobuy.modules.cart.dto.CartAddDTO;
import com.example.gobuy.modules.cart.dto.CartUpdateDTO;
import com.example.gobuy.modules.cart.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 购物车表 服务接口
 */
public interface ICartService extends IService<Cart> {
    
    List<Cart> listByUserId(Long userId);
    
    void addToCart(Long userId, CartAddDTO dto);
    
    void updateQuantity(Long userId, Long cartItemId, Integer quantity);
    
    void deleteCartItem(Long userId, Long cartItemId);
    
    void toggleSelect(Long userId, Long cartItemId);
    
    void clearCart(Long userId);

    void removeItems(Long userId, List<Long> cartItemIds);
}