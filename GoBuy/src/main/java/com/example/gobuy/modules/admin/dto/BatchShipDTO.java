package com.example.gobuy.modules.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class BatchShipDTO {
    @NotEmpty(message = "订单ID列表不能为空")
    private List<Long> orderIds;
    @NotBlank(message = "物流公司不能为空")
    private String expressCompany;
    @NotBlank(message = "运单号不能为空")
    private String expressNo;
}
