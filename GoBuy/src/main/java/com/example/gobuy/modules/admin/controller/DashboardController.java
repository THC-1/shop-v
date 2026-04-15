package com.example.gobuy.modules.admin.controller;

import com.example.gobuy.common.annotation.RequirePermission;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.service.IDashboardService;
import com.example.gobuy.modules.admin.vo.SalesSummaryVO;
import com.example.gobuy.modules.admin.vo.SalesTrendVO;
import com.example.gobuy.modules.admin.vo.TopProductVO;
import com.example.gobuy.modules.admin.vo.UvPvVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin数据看板")
@RestController
@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IDashboardService dashboardService;

    @GetMapping("/sales-summary")
    @RequirePermission("DASHBOARD:VIEW")
    @Operation(summary = "销售汇总")
    public Result<SalesSummaryVO> getSalesSummary(
            @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式必须为yyyy-MM-dd") String startDate,
            @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式必须为yyyy-MM-dd") String endDate) {
        return Result.success(dashboardService.getSalesSummary(startDate, endDate));
    }

    @GetMapping("/sales-trend")
    @RequirePermission("DASHBOARD:VIEW")
    @Operation(summary = "销售趋势")
    public Result<List<SalesTrendVO>> getSalesTrend(
            @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式必须为yyyy-MM-dd") String startDate,
            @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式必须为yyyy-MM-dd") String endDate,
            @RequestParam(defaultValue = "day") String type) {
        return Result.success(dashboardService.getSalesTrend(startDate, endDate, type));
    }

    @GetMapping("/top-products")
    @RequirePermission("DASHBOARD:VIEW")
    @Operation(summary = "热销商品")
    public Result<List<TopProductVO>> getTopProducts(
            @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式必须为yyyy-MM-dd") String startDate,
            @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式必须为yyyy-MM-dd") String endDate,
            @RequestParam(defaultValue = "10") @Positive @Max(100) Integer limit) {
        return Result.success(dashboardService.getTopProducts(startDate, endDate, limit));
    }

    @GetMapping("/uv-pv")
    @RequirePermission("DASHBOARD:VIEW")
    @Operation(summary = "UV/PV统计")
    public Result<UvPvVO> getUvPv(
            @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式必须为yyyy-MM-dd") String startDate,
            @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式必须为yyyy-MM-dd") String endDate) {
        return Result.success(dashboardService.getUvPv(startDate, endDate));
    }
}
