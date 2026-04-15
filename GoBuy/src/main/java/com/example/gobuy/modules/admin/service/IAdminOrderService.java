package com.example.gobuy.modules.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.gobuy.modules.admin.dto.AdminOrderQueryDTO;
import com.example.gobuy.modules.admin.dto.BatchShipDTO;
import com.example.gobuy.modules.admin.dto.OrderShipDTO;
import com.example.gobuy.modules.admin.vo.AdminOrderDetailVO;
import com.example.gobuy.modules.admin.vo.AdminOrderVO;
import com.example.gobuy.modules.order.entity.Order;

public interface IAdminOrderService extends IService<Order> {
    IPage<AdminOrderVO> listOrders(AdminOrderQueryDTO queryDTO);
    AdminOrderDetailVO getOrderDetail(Long id);
    void shipOrder(Long id, OrderShipDTO dto);
    void batchShip(BatchShipDTO dto);
}
