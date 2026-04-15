package com.example.gobuy.modules.payment.service;

import com.example.gobuy.modules.payment.entity.Payment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 支付记录表 服务接口
 */
public interface IPaymentService extends IService<Payment> {
    
    Payment getByOrderId(Long orderId);
    
    void handleCallback(Long paymentId, String status);
    
    void refundPayment(Long userId, Long paymentId);
    
    List<Payment> listMyPayments(Long userId);

    Payment createMockPayment(Long userId, Long orderId);
}