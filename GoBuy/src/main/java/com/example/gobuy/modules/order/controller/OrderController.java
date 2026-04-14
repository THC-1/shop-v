package com.example.gobuy.modules.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.common.utils.UserContextHolder;
import com.example.gobuy.modules.order.assembler.OrderAssembler;
import com.example.gobuy.modules.order.dto.OrderCreateDTO;
import com.example.gobuy.modules.order.entity.Order;
import com.example.gobuy.modules.order.service.IOrderService;
import com.example.gobuy.modules.order.vo.OrderDetailVO;
import com.example.gobuy.modules.order.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "订单管理")
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final OrderAssembler orderAssembler;

    @Operation(summary = "创建订单")
    @PostMapping
    public Result<OrderDetailVO> createOrder(@Valid @RequestBody OrderCreateDTO dto) {
        Long userId = UserContextHolder.getRequiredUserId();
        return orderService.createOrder(userId, dto);
    }

    @Operation(summary = "查询我的订单列表")
    @GetMapping("/my")
    public Result<IPage<OrderVO>> listMyOrders(
            @Parameter(description = "订单状态：0-待付款，1-已付款，2-已发货，3-已送达，4-已完成，5-已取消")
            @RequestParam(required = false) Integer status,
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = UserContextHolder.getRequiredUserId();
        return orderService.listOrders(userId, status, pageNum, pageSize);
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/{id}/detail")
    public Result<OrderDetailVO> getOrderDetail(@PathVariable("id") Long id) {
        Long userId = UserContextHolder.getRequiredUserId();
        return orderService.getOrderDetail(userId, id);
    }

    @Operation(summary = "确认收货")
    @PutMapping("/{id}/confirm")
    public Result<Void> confirmOrder(@PathVariable("id") Long id) {
        Long userId = UserContextHolder.getRequiredUserId();
        return orderService.confirmOrder(userId, id);
    }

    @Operation(summary = "申请退款")
    @PutMapping("/{id}/refund")
    public Result<Void> refundOrder(@PathVariable("id") Long id) {
        Long userId = UserContextHolder.getRequiredUserId();
        return orderService.refundOrder(userId, id);
    }

    @Operation(summary = "取消订单")
    @PutMapping("/{id}/cancel")
    public Result<Void> cancelOrder(@PathVariable("id") Long id) {
        Long userId = UserContextHolder.getRequiredUserId();
        return orderService.cancelOrder(userId, id);
    }

    @Operation(summary = "删除订单")
    @DeleteMapping("/{id}")
    public Result<Void> deleteOrder(@PathVariable("id") Long id) {
        Long userId = UserContextHolder.getRequiredUserId();
        return orderService.deleteOrder(userId, id);
    }
}
