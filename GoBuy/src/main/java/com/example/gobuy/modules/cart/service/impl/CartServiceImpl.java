package com.example.gobuy.modules.cart.service.impl;

import com.example.gobuy.common.exception.BusinessException;
import com.example.gobuy.modules.cart.dto.CartAddDTO;
import com.example.gobuy.modules.cart.entity.Cart;
import com.example.gobuy.modules.cart.mapper.CartMapper;
import com.example.gobuy.modules.cart.service.ICartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 购物车表 服务实现层
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

    @Override
    @Transactional(readOnly = true)
    public List<Cart> listByUserId(Long userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        return list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addToCart(Long userId, CartAddDTO dto) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
               .eq(Cart::getProductId, dto.getProductId())
               .eq(Cart::getSkuId, dto.getSkuId());
        
        Cart existingCart = getOne(wrapper);
        
        if (existingCart != null) {
            existingCart.setQuantity(existingCart.getQuantity() + dto.getQuantity());
            updateById(existingCart);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(dto.getProductId());
            cart.setSkuId(dto.getSkuId());
            cart.setQuantity(dto.getQuantity());
            cart.setSelected(true);
            save(cart);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateQuantity(Long userId, Long cartItemId, Integer quantity) {
        Cart cart = getById(cartItemId);
        if (cart == null) {
            throw new BusinessException(404, "购物车项不存在");
        }
        if (!cart.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作该购物车项");
        }
        cart.setQuantity(quantity);
        updateById(cart);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCartItem(Long userId, Long cartItemId) {
        Cart cart = getById(cartItemId);
        if (cart == null) {
            throw new BusinessException(404, "购物车项不存在");
        }
        if (!cart.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作该购物车项");
        }
        removeById(cartItemId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleSelect(Long userId, Long cartItemId) {
        Cart cart = getById(cartItemId);
        if (cart == null) {
            throw new BusinessException(404, "购物车项不存在");
        }
        if (!cart.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作该购物车项");
        }
        Boolean currentSelected = cart.getSelected();
        cart.setSelected(currentSelected == null || !currentSelected);
        updateById(cart);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearCart(Long userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        remove(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeItems(Long userId, List<Long> cartItemIds) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
               .in(Cart::getId, cartItemIds);
        remove(wrapper);
    }
}