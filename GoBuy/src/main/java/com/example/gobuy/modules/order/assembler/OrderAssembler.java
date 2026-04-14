package com.example.gobuy.modules.order.assembler;

import com.example.gobuy.modules.order.entity.Order;
import com.example.gobuy.modules.order.vo.OrderVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderAssembler {

    OrderVO toVO(Order entity);

    List<OrderVO> toVOList(List<Order> entityList);
}
