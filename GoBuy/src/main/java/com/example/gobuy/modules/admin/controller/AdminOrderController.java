package com.example.gobuy.modules.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.gobuy.common.annotation.OperationLog;
import com.example.gobuy.common.annotation.RequirePermission;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.dto.AdminOrderQueryDTO;
import com.example.gobuy.modules.admin.dto.BatchShipDTO;
import com.example.gobuy.modules.admin.dto.OrderShipDTO;
import com.example.gobuy.modules.admin.service.IAdminOrderService;
import com.example.gobuy.modules.admin.vo.AdminOrderDetailVO;
import com.example.gobuy.modules.admin.vo.AdminOrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin订单管理")
@RestController
@RequestMapping("/api/v1/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final IAdminOrderService adminOrderService;

    @GetMapping
    @RequirePermission("ORDER:VIEW")
    @Operation(summary = "订单列表")
    public Result<IPage<AdminOrderVO>> listOrders(AdminOrderQueryDTO queryDTO) {
        return Result.success(adminOrderService.listOrders(queryDTO));
    }

    @GetMapping("/{id}")
    @RequirePermission("ORDER:VIEW")
    @Operation(summary = "订单详情")
    public Result<AdminOrderDetailVO> getOrderDetail(@PathVariable Long id) {
        return Result.success(adminOrderService.getOrderDetail(id));
    }

    @PatchMapping("/{id}/ship")
    @RequirePermission("ORDER:SHIP")
    @OperationLog(module = "订单", action = "订单发货", targetType = "Order")
    @Operation(summary = "订单发货")
    public Result<Void> shipOrder(@PathVariable Long id, @Valid @RequestBody OrderShipDTO dto) {
        adminOrderService.shipOrder(id, dto);
        return Result.success();
    }

    @PostMapping("/batch/ship")
    @RequirePermission("ORDER:SHIP")
    @OperationLog(module = "订单", action = "批量发货", targetType = "Order")
    @Operation(summary = "批量发货")
    public Result<Void> batchShip(@Valid @RequestBody BatchShipDTO dto) {
        adminOrderService.batchShip(dto);
        return Result.success();
    }
}
