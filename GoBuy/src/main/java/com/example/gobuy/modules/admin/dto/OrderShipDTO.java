package com.example.gobuy.modules.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderShipDTO {
    @NotBlank(message = "物流公司不能为空")
    private String expressCompany;
    @NotBlank(message = "运单号不能为空")
    private String expressNo;
}
