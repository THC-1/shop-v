package com.example.gobuy.modules.payment.controller;

import com.example.gobuy.common.result.Result;
import com.example.gobuy.common.utils.UserContextHolder;
import com.example.gobuy.modules.payment.assembler.PaymentAssembler;
import com.example.gobuy.modules.payment.entity.Payment;
import com.example.gobuy.modules.payment.service.IPaymentService;
import com.example.gobuy.modules.payment.vo.PaymentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "支付管理")
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentService paymentService;
    private final PaymentAssembler paymentAssembler;

    @GetMapping("/order/{orderId}")
    @Operation(summary = "根据订单 ID 查询支付信息")
    public Result<PaymentVO> getByOrderId(@PathVariable("orderId") Long orderId) {
        Payment entity = paymentService.getByOrderId(orderId);
        if (entity == null) {
            return Result.fail(404, "未找到相关数据");
        }
        return Result.success(paymentAssembler.toVO(entity));
    }

    @GetMapping("/my")
    @Operation(summary = "查询当前用户的支付记录")
    public Result<List<PaymentVO>> listMyPayments() {
        Long userId = UserContextHolder.getRequiredUserId();
        List<Payment> list = paymentService.listMyPayments(userId);
        return Result.success(paymentAssembler.toVOList(list));
    }

    @PostMapping("/{id}/callback")
    @Operation(summary = "支付回调处理（模拟）")
    public Result<Void> handleCallback(@PathVariable("id") Long paymentId, @RequestParam String status) {
        paymentService.handleCallback(paymentId, status);
        return Result.success();
    }

    @PutMapping("/{id}/refund")
    @Operation(summary = "退款处理")
    public Result<Void> refundPayment(@PathVariable("id") Long paymentId) {
        Long userId = UserContextHolder.getRequiredUserId();
        paymentService.refundPayment(userId, paymentId);
        return Result.success();
    }

    @PostMapping("/mock-pay/{orderId}")
    @Operation(summary = "模拟支付（点击即支付）")
    public Result<Void> mockPay(@PathVariable("orderId") Long orderId) {
        Long userId = UserContextHolder.getRequiredUserId();
        paymentService.createMockPayment(userId, orderId);
        return Result.success();
    }
}
