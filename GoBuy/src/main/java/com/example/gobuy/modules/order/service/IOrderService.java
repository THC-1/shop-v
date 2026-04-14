package com.example.gobuy.modules.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.order.dto.OrderCreateDTO;
import com.example.gobuy.modules.order.entity.Order;
import com.example.gobuy.modules.order.vo.OrderDetailVO;
import com.example.gobuy.modules.order.vo.OrderVO;

public interface IOrderService extends IService<Order> {

    Result<OrderDetailVO> createOrder(Long userId, OrderCreateDTO dto);

    Result<IPage<OrderVO>> listOrders(Long userId, Integer status, Integer pageNum, Integer pageSize);

    Result<OrderDetailVO> getOrderDetail(Long userId, Long orderId);

    Result<Void> cancelOrder(Long userId, Long orderId);

    Result<Void> confirmOrder(Long userId, Long orderId);

    Result<Void> refundOrder(Long userId, Long orderId);

    Result<Void> deleteOrder(Long userId, Long orderId);
}
