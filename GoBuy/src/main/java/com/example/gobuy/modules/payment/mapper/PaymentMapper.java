package com.example.gobuy.modules.payment.mapper;

import com.example.gobuy.modules.payment.entity.Payment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付记录表 Mapper 接口
 */
@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {
}