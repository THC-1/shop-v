package com.example.gobuy.modules.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailVO {

    private Long id;
    private String orderNo;
    private Long userId;
    private Long addressId;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private BigDecimal totalAmount;
    private String status;
    private String note;
    private List<OrderItemDetailVO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    public static class OrderItemDetailVO {
        private Long id;
        private Long productId;
        private Long skuId;
        private String productName;
        private Integer quantity;
        private BigDecimal price;
    }
}
