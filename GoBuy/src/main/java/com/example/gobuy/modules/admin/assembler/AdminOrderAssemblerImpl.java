package com.example.gobuy.modules.admin.assembler;

import com.example.gobuy.modules.admin.vo.AdminOrderDetailVO;
import com.example.gobuy.modules.admin.vo.AdminOrderVO;
import com.example.gobuy.modules.admin.vo.OrderItemVO;
import com.example.gobuy.modules.address.entity.Address;
import com.example.gobuy.modules.order.entity.Order;
import com.example.gobuy.modules.order.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminOrderAssemblerImpl {

    public OrderItemVO toItemVO(OrderItem item) {
        if (item == null) return null;
        OrderItemVO vo = new OrderItemVO();
        vo.setId(item.getId());
        vo.setProductId(item.getProductId());
        vo.setProductName(item.getProductName());
        vo.setSkuId(item.getSkuId());
        vo.setPrice(item.getPrice());
        vo.setQuantity(item.getQuantity());
        return vo;
    }

    public List<OrderItemVO> toItemVOList(List<OrderItem> items) {
        if (items == null) return List.of();
        return items.stream().map(this::toItemVO).collect(Collectors.toList());
    }

    public AdminOrderVO toVO(Order order) {
        if (order == null) return null;
        AdminOrderVO vo = new AdminOrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setUserId(order.getUserId());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setStatus(order.getStatus());
        vo.setExpressCompany(order.getExpressCompany());
        vo.setExpressNo(order.getExpressNo());
        vo.setCreatedAt(order.getCreatedAt());
        return vo;
    }

    public List<AdminOrderVO> toVOList(List<Order> orders) {
        if (orders == null) return List.of();
        return orders.stream().map(this::toVO).collect(Collectors.toList());
    }

    public AdminOrderDetailVO toDetailVO(Order order, Address address) {
        if (order == null) return null;
        AdminOrderDetailVO vo = new AdminOrderDetailVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setUserId(order.getUserId());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setStatus(order.getStatus());
        vo.setExpressCompany(order.getExpressCompany());
        vo.setExpressNo(order.getExpressNo());
        vo.setShippedAt(order.getShippedAt());
        vo.setNote(order.getNote());
        vo.setCreatedAt(order.getCreatedAt());
        if (address != null) {
            vo.setReceiverName(address.getReceiverName());
            vo.setPhone(address.getPhone());
            vo.setProvince(address.getProvince());
            vo.setCity(address.getCity());
            vo.setDistrict(address.getDistrict());
            vo.setDetailAddress(address.getDetailAddress());
        }
        return vo;
    }
}
