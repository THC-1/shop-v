package com.example.gobuy.modules.order.mapper;

import com.example.gobuy.modules.order.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单表 Mapper 接口
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}