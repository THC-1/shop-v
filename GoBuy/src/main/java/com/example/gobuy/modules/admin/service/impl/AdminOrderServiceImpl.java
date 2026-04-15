package com.example.gobuy.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.gobuy.common.exception.BusinessException;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.assembler.AdminOrderAssembler;
import com.example.gobuy.modules.admin.dto.AdminOrderQueryDTO;
import com.example.gobuy.modules.admin.dto.BatchShipDTO;
import com.example.gobuy.modules.admin.dto.OrderShipDTO;
import com.example.gobuy.modules.admin.service.IAdminOrderService;
import com.example.gobuy.modules.admin.vo.AdminOrderDetailVO;
import com.example.gobuy.modules.admin.vo.AdminOrderVO;
import com.example.gobuy.modules.address.entity.Address;
import com.example.gobuy.modules.address.mapper.AddressMapper;
import com.example.gobuy.modules.order.entity.Order;
import com.example.gobuy.modules.order.entity.OrderItem;
import com.example.gobuy.modules.order.mapper.OrderItemMapper;
import com.example.gobuy.modules.order.mapper.OrderMapper;
import com.example.gobuy.modules.user.entity.User;
import com.example.gobuy.modules.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IAdminOrderService {

    private final AdminOrderAssembler assembler;
    private final OrderItemMapper orderItemMapper;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    @Override
    public Result<IPage<AdminOrderVO>> listOrders(AdminOrderQueryDTO queryDTO) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDTO.getOrderNo())) {
            wrapper.like(Order::getOrderNo, queryDTO.getOrderNo());
        }
        if (StringUtils.hasText(queryDTO.getStatus())) {
            wrapper.eq(Order::getStatus, queryDTO.getStatus());
        }
        if (queryDTO.getUserId() != null) {
            wrapper.eq(Order::getUserId, queryDTO.getUserId());
        }
        if (StringUtils.hasText(queryDTO.getStartDate())) {
            LocalDate startDate = LocalDate.parse(queryDTO.getStartDate());
            wrapper.ge(Order::getCreatedAt, startDate.atStartOfDay());
        }
        if (StringUtils.hasText(queryDTO.getEndDate())) {
            LocalDate endDate = LocalDate.parse(queryDTO.getEndDate());
            wrapper.le(Order::getCreatedAt, endDate.atTime(LocalTime.MAX));
        }
        wrapper.orderByDesc(Order::getCreatedAt);

        IPage<Order> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        IPage<Order> result = page(page, wrapper);

        Map<Long, String> userNameMap = getUserNameMap(result.getRecords());

        List<AdminOrderVO> voList = result.getRecords().stream().map(order -> {
            AdminOrderVO vo = assembler.toVO(order);
            vo.setUsername(userNameMap.getOrDefault(order.getUserId(), ""));
            return vo;
        }).collect(Collectors.toList());

        IPage<AdminOrderVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(voList);
        return Result.success(voPage);
    }

    @Override
    public Result<AdminOrderDetailVO> getOrderDetail(Long id) {
        Order order = getById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        String userName = getUserName(order.getUserId());
        Address address = addressMapper.selectById(order.getAddressId());
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, id)
        );

        AdminOrderDetailVO detailVO = assembler.toDetailVO(order, address);
        if (detailVO != null) {
            detailVO.setUsername(userName);
            detailVO.setItems(assembler.toItemVOList(items));
        }
        return Result.success(detailVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> shipOrder(Long id, OrderShipDTO dto) {
        Order order = getById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        if (!"PAID".equals(order.getStatus())) {
            throw new BusinessException(422, "只有已支付的订单才能发货");
        }
        order.setExpressCompany(dto.getExpressCompany());
        order.setExpressNo(dto.getExpressNo());
        order.setShippedAt(LocalDateTime.now());
        order.setStatus("SHIPPED");
        updateById(order);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> batchShip(BatchShipDTO dto) {
        if (dto.getOrderIds().size() > 100) {
            throw new BusinessException(400, "单次批量发货最多 100 单");
        }

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .in(Order::getId, dto.getOrderIds())
                .eq(Order::getStatus, "PAID");
        List<Order> orders = list(wrapper);

        if (orders.size() != dto.getOrderIds().size()) {
            Set<Long> foundIds = orders.stream().map(Order::getId).collect(Collectors.toSet());
            List<Long> invalidIds = dto.getOrderIds().stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toList());
            throw new BusinessException(400, "以下订单不存在或状态不允许发货: " + invalidIds);
        }

        LocalDateTime now = LocalDateTime.now();
        for (Order order : orders) {
            order.setExpressCompany(dto.getExpressCompany());
            order.setExpressNo(dto.getExpressNo());
            order.setShippedAt(now);
            order.setStatus("SHIPPED");
        }
        updateBatchById(orders);
        return Result.success();
    }

    private Map<Long, String> getUserNameMap(List<Order> orders) {
        List<Long> userIds = orders.stream().map(Order::getUserId).distinct().collect(Collectors.toList());
        if (userIds.isEmpty()) {
            return Map.of();
        }
        List<User> users = userMapper.selectList(
                new LambdaQueryWrapper<User>().in(User::getId, userIds)
        );
        return users.stream().collect(Collectors.toMap(User::getId, User::getUsername, (a, b) -> a));
    }

    private String getUserName(Long userId) {
        if (userId == null) return "";
        User user = userMapper.selectById(userId);
        return user != null ? user.getUsername() : "";
    }
}
