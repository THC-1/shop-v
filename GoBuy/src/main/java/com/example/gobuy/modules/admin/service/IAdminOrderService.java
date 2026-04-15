package com.example.gobuy.modules.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.dto.AdminOrderQueryDTO;
import com.example.gobuy.modules.admin.dto.BatchShipDTO;
import com.example.gobuy.modules.admin.dto.OrderShipDTO;
import com.example.gobuy.modules.admin.vo.AdminOrderDetailVO;
import com.example.gobuy.modules.admin.vo.AdminOrderVO;

public interface IAdminOrderService {
    Result<IPage<AdminOrderVO>> listOrders(AdminOrderQueryDTO queryDTO);
    Result<AdminOrderDetailVO> getOrderDetail(Long id);
    Result<Void> shipOrder(Long id, OrderShipDTO dto);
    Result<Void> batchShip(BatchShipDTO dto);
}
