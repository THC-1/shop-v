package com.example.gobuy.modules.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AdminOrderQueryDTO {

    @Min(value = 1, message = "页码最小为1")
    private Integer page = 1;

    @Max(value = 100, message = "每页最大100条")
    private Integer size = 10;

    private String orderNo;

    private String status;

    private Long userId;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式必须为yyyy-MM-dd")
    private String startDate;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式必须为yyyy-MM-dd")
    private String endDate;
}
