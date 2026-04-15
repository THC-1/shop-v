package com.example.gobuy.modules.admin.assembler;

import com.example.gobuy.modules.admin.vo.AdminOrderDetailVO;
import com.example.gobuy.modules.admin.vo.AdminOrderVO;
import com.example.gobuy.modules.admin.vo.OrderItemVO;
import com.example.gobuy.modules.address.entity.Address;
import com.example.gobuy.modules.order.entity.Order;
import com.example.gobuy.modules.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminOrderAssembler {
    @Mapping(target = "username", ignore = true)
    AdminOrderVO toVO(Order order);

    List<AdminOrderVO> toVOList(List<Order> orders);

    @Mapping(target = "id", source = "order.id")
    @Mapping(target = "orderNo", source = "order.orderNo")
    @Mapping(target = "userId", source = "order.userId")
    @Mapping(target = "totalAmount", source = "order.totalAmount")
    @Mapping(target = "status", source = "order.status")
    @Mapping(target = "expressCompany", source = "order.expressCompany")
    @Mapping(target = "expressNo", source = "order.expressNo")
    @Mapping(target = "shippedAt", source = "order.shippedAt")
    @Mapping(target = "note", source = "order.note")
    @Mapping(target = "createdAt", source = "order.createdAt")
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "receiverName", source = "address.receiverName")
    @Mapping(target = "phone", source = "address.phone")
    @Mapping(target = "province", source = "address.province")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "district", source = "address.district")
    @Mapping(target = "detailAddress", source = "address.detailAddress")
    AdminOrderDetailVO toDetailVO(Order order, Address address);

    OrderItemVO toItemVO(OrderItem item);

    List<OrderItemVO> toItemVOList(List<OrderItem> items);
}
