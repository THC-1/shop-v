package com.example.gobuy.modules.payment.assembler;

import com.example.gobuy.modules.payment.entity.Payment;
import com.example.gobuy.modules.payment.vo.PaymentVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentAssembler {

    PaymentVO toVO(Payment entity);

    List<PaymentVO> toVOList(List<Payment> entityList);
}
