package com.example.gobuy.modules.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class BatchStatusDTO {
    @NotEmpty(message = "商品ID列表不能为空")
    private List<Long> productIds;
    @NotBlank(message = "状态不能为空")
    private String status;
}
