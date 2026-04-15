package com.example.gobuy.modules.admin.vo;

import lombok.Data;

import java.util.List;

@Data
public class BatchStatusResult {
    private Integer successCount;
    private Integer failCount;
    private List<Long> failIds;
}
