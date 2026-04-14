package com.example.gobuy.modules.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.gobuy.common.exception.BusinessException;
import com.example.gobuy.modules.order.entity.Order;
import com.example.gobuy.modules.order.mapper.OrderMapper;
import com.example.gobuy.modules.payment.entity.Payment;
import com.example.gobuy.modules.payment.mapper.PaymentMapper;
import com.example.gobuy.modules.payment.service.IPaymentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付记录表 服务实现层
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements IPaymentService {

    private final OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public Payment getByOrderId(Long orderId) {
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Payment::getOrderId, orderId);
        return getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleCallback(Long paymentId, String status) {
        Payment payment = getById(paymentId);
        if (payment == null) {
            throw new BusinessException(404, "支付记录不存在");
        }
        
        if ("paid".equalsIgnoreCase(status) || "success".equalsIgnoreCase(status)) {
            if ("paid".equals(payment.getStatus())) {
                throw new BusinessException(400, "该支付记录已处理");
            }
            
            payment.setStatus("paid");
            payment.setUpdatedAt(LocalDateTime.now());
            updateById(payment);
            
            Order order = orderMapper.selectById(payment.getOrderId());
            if (order == null) {
                throw new BusinessException(404, "关联订单不存在");
            }
            
            order.setStatus("PAID");
            order.setUpdatedAt(LocalDateTime.now());
            orderMapper.updateById(order);
        } else {
            throw new BusinessException(400, "支付状态无效：" + status);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundPayment(Long userId, Long paymentId) {
        Payment payment = getById(paymentId);
        if (payment == null) {
            throw new BusinessException(404, "支付记录不存在");
        }
        
        Order order = orderMapper.selectById(payment.getOrderId());
        if (order == null) {
            throw new BusinessException(404, "关联订单不存在");
        }
        
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作此支付记录");
        }
        
        if (!"paid".equals(payment.getStatus())) {
            throw new BusinessException(400, "只有已支付的订单才能退款");
        }
        
        payment.setStatus("refunded");
        payment.setUpdatedAt(LocalDateTime.now());
        updateById(payment);
        
        order.setStatus("REFUNDED");
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> listMyPayments(Long userId) {
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getUserId, userId);
        orderWrapper.select(Order::getId);
        List<Long> orderIds = orderMapper.selectList(orderWrapper)
                .stream().map(Order::getId).toList();

        if (orderIds.isEmpty()) {
            return List.of();
        }

        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Payment::getOrderId, orderIds);
        wrapper.orderByDesc(Payment::getCreatedAt);
        return list(wrapper);
    }
}