package com.example.gobuy.modules.cart.mapper;

import com.example.gobuy.modules.cart.entity.Cart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 购物车表 Mapper 接口
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {
}