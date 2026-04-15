package com.example.gobuy.modules.admin.service;

import com.example.gobuy.modules.admin.vo.*;

public interface IDashboardService {
    SalesSummaryVO getSalesSummary(String startDate, String endDate);
    java.util.List<SalesTrendVO> getSalesTrend(String startDate, String endDate, String type);
    java.util.List<TopProductVO> getTopProducts(String startDate, String endDate, Integer limit);
    UvPvVO getUvPv(String startDate, String endDate);
}
