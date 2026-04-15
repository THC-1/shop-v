package com.example.gobuy.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.gobuy.modules.admin.entity.DailyStats;
import com.example.gobuy.modules.admin.entity.ProductStats;
import com.example.gobuy.modules.admin.mapper.DailyStatsMapper;
import com.example.gobuy.modules.admin.mapper.ProductStatsMapper;
import com.example.gobuy.modules.admin.service.IDashboardService;
import com.example.gobuy.modules.admin.vo.*;
import com.example.gobuy.modules.order.entity.Order;
import com.example.gobuy.modules.order.mapper.OrderMapper;
import com.example.gobuy.modules.product.entity.Brand;
import com.example.gobuy.modules.product.entity.Product;
import com.example.gobuy.modules.product.mapper.BrandMapper;
import com.example.gobuy.modules.product.mapper.ProductMapper;
import com.example.gobuy.modules.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements IDashboardService {

    private final OrderMapper orderMapper;
    private final DailyStatsMapper dailyStatsMapper;
    private final ProductStatsMapper productStatsMapper;
    private final ProductMapper productMapper;
    private final BrandMapper brandMapper;
    private final UserMapper userMapper;

    @Override
    public SalesSummaryVO getSalesSummary(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        LocalDateTime startTime = start.atStartOfDay();
        LocalDateTime endTime = end.atTime(LocalTime.MAX);

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getStatus, "COMPLETED")
                .between(Order::getCreatedAt, startTime, endTime)
                .select(Order::getTotalAmount, Order::getUserId);
        List<Order> orders = orderMapper.selectList(wrapper);

        SalesSummaryVO vo = new SalesSummaryVO();
        int totalOrders = orders.size();
        BigDecimal totalAmount = orders.stream().map(Order::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalUsers = userMapper.selectCount(null).intValue();
        Set<Long> paidUserIds = orders.stream().map(Order::getUserId).collect(Collectors.toSet());
        BigDecimal avgCustomerSpend = paidUserIds.size() > 0 ? totalAmount.divide(BigDecimal.valueOf(paidUserIds.size()), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        vo.setTotalOrders(totalOrders);
        vo.setTotalAmount(totalAmount);
        vo.setTotalUsers(totalUsers);
        vo.setAvgOrderAmount(avgCustomerSpend);
        return vo;
    }

    @Override
    public List<SalesTrendVO> getSalesTrend(String startDate, String endDate, String type) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        LocalDateTime startTime = start.atStartOfDay();
        LocalDateTime endTime = end.atTime(LocalTime.MAX);

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getStatus, "COMPLETED")
                .between(Order::getCreatedAt, startTime, endTime)
                .select(Order::getCreatedAt, Order::getTotalAmount, Order::getUserId);
        List<Order> orders = orderMapper.selectList(wrapper);

        List<SalesTrendVO> trendList = new ArrayList<>();

        if ("week".equals(type)) {
            LocalDate weekStart = start.with(java.time.DayOfWeek.MONDAY);
            LocalDate weekEnd = end.with(java.time.DayOfWeek.SUNDAY);
            while (!weekStart.isAfter(weekEnd)) {
                LocalDate weekEndDay = weekStart.plusDays(6);
                LocalDate finalWeekStart = weekStart;
                LocalDate finalWeekEndDay = weekEndDay;
                List<Order> weekOrders = orders.stream()
                        .filter(o -> {
                            LocalDate d = o.getCreatedAt().toLocalDate();
                            return !d.isBefore(finalWeekStart) && !d.isAfter(finalWeekEndDay);
                        })
                        .collect(Collectors.toList());
                SalesTrendVO vo = new SalesTrendVO();
                vo.setDate(weekStart);
                vo.setOrderCount(weekOrders.size());
                vo.setOrderAmount(weekOrders.stream().map(Order::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                vo.setUserCount((int) weekOrders.stream().map(Order::getUserId).distinct().count());
                trendList.add(vo);
                weekStart = weekStart.plusWeeks(1);
            }
        } else if ("month".equals(type)) {
            LocalDate monthStart = start.withDayOfMonth(1);
            while (!monthStart.isAfter(end)) {
                LocalDate monthEnd = monthStart.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());
                LocalDate finalMonthStart = monthStart;
                LocalDate finalMonthEnd = monthEnd;
                List<Order> monthOrders = orders.stream()
                        .filter(o -> {
                            LocalDate d = o.getCreatedAt().toLocalDate();
                            return !d.isBefore(finalMonthStart) && !d.isAfter(finalMonthEnd);
                        })
                        .collect(Collectors.toList());
                SalesTrendVO vo = new SalesTrendVO();
                vo.setDate(monthStart);
                vo.setOrderCount(monthOrders.size());
                vo.setOrderAmount(monthOrders.stream().map(Order::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                vo.setUserCount((int) monthOrders.stream().map(Order::getUserId).distinct().count());
                trendList.add(vo);
                monthStart = monthStart.plusMonths(1);
            }
        } else {
            LocalDate day = start;
            while (!day.isAfter(end)) {
                LocalDate finalDay = day;
                List<Order> dayOrders = orders.stream()
                        .filter(o -> o.getCreatedAt().toLocalDate().equals(finalDay))
                        .collect(Collectors.toList());
                SalesTrendVO vo = new SalesTrendVO();
                vo.setDate(day);
                vo.setOrderCount(dayOrders.size());
                vo.setOrderAmount(dayOrders.stream().map(Order::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                vo.setUserCount((int) dayOrders.stream().map(Order::getUserId).distinct().count());
                trendList.add(vo);
                day = day.plusDays(1);
            }
        }
        return trendList;
    }

    @Override
    public List<TopProductVO> getTopProducts(String startDate, String endDate, Integer limit) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        if (limit == null) limit = 10;

        LambdaQueryWrapper<ProductStats> psWrapper = new LambdaQueryWrapper<ProductStats>()
                .between(ProductStats::getStatDate, start, end);
        List<ProductStats> productStats = productStatsMapper.selectList(psWrapper);

        Map<Long, Integer> productSalesCount = productStats.stream()
                .collect(Collectors.groupingBy(ProductStats::getProductId,
                        Collectors.summingInt(ProductStats::getOrderCount)));

        List<Map.Entry<Long, Integer>> sorted = productSalesCount.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toList());

        List<Long> productIds = sorted.stream().map(Map.Entry::getKey).collect(Collectors.toList());
        Map<Long, String> brandNameMap = batchGetBrandNames(productIds);

        List<TopProductVO> result = new ArrayList<>();
        int rank = 1;
        for (Map.Entry<Long, Integer> entry : sorted) {
            TopProductVO vo = new TopProductVO();
            vo.setRank(rank++);
            vo.setProductId(entry.getKey());
            Product product = productMapper.selectById(entry.getKey());
            if (product != null) {
                vo.setProductName(product.getName());
                if (product.getBrandId() != null) {
                    vo.setBrandName(brandNameMap.getOrDefault(product.getBrandId(), ""));
                }
            }
            vo.setSalesCount(entry.getValue());
            result.add(vo);
        }
        return result;
    }

    private Map<Long, String> batchGetBrandNames(List<Long> brandIds) {
        if (brandIds.isEmpty()) {
            return Map.of();
        }
        List<Brand> brands = brandMapper.selectList(
                new LambdaQueryWrapper<Brand>().in(Brand::getId, brandIds).select(Brand::getId, Brand::getName)
        );
        return brands.stream().collect(Collectors.toMap(Brand::getId, Brand::getName));
    }

    @Override
    public UvPvVO getUvPv(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        LambdaQueryWrapper<DailyStats> wrapper = new LambdaQueryWrapper<DailyStats>()
                .between(DailyStats::getStatDate, start, end)
                .orderByAsc(DailyStats::getStatDate);
        List<DailyStats> stats = dailyStatsMapper.selectList(wrapper);

        int totalUv = stats.stream().mapToInt(DailyStats::getUv).sum();
        int totalPv = stats.stream().mapToInt(DailyStats::getPv).sum();
        BigDecimal avgPvPerUv = totalUv > 0 ? BigDecimal.valueOf(totalPv).divide(BigDecimal.valueOf(totalUv), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        List<UvPvTrendVO> dailyList = stats.stream().map(s -> {
            UvPvTrendVO vo = new UvPvTrendVO();
            vo.setDate(s.getStatDate());
            vo.setUv(s.getUv());
            vo.setPv(s.getPv());
            return vo;
        }).collect(Collectors.toList());

        UvPvVO result = new UvPvVO();
        result.setTotalUv(totalUv);
        result.setTotalPv(totalPv);
        result.setAvgPvPerUv(avgPvPerUv);
        result.setDailyList(dailyList);
        return result;
    }
}
