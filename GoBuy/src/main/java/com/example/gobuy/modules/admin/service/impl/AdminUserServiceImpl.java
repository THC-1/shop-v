package com.example.gobuy.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.gobuy.common.exception.BusinessException;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.assembler.AdminUserAssembler;
import com.example.gobuy.modules.admin.dto.AdminUserQueryDTO;
import com.example.gobuy.modules.admin.dto.UserRoleDTO;
import com.example.gobuy.modules.admin.dto.UserStatusDTO;
import com.example.gobuy.modules.admin.service.IAdminUserService;
import com.example.gobuy.modules.admin.vo.AdminUserDetailVO;
import com.example.gobuy.modules.admin.vo.AdminUserVO;
import com.example.gobuy.modules.order.entity.Order;
import com.example.gobuy.modules.order.mapper.OrderMapper;
import com.example.gobuy.modules.user.entity.User;
import com.example.gobuy.modules.user.entity.UserRole;
import com.example.gobuy.modules.user.mapper.UserMapper;
import com.example.gobuy.modules.user.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl extends ServiceImpl<UserMapper, User> implements IAdminUserService {

    private final AdminUserAssembler assembler;
    private final OrderMapper orderMapper;
    private final UserRoleMapper userRoleMapper;

    @Override
    public Result<IPage<AdminUserVO>> listUsers(AdminUserQueryDTO queryDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDTO.getUsername())) {
            wrapper.like(User::getUsername, queryDTO.getUsername());
        }
        if (StringUtils.hasText(queryDTO.getStatus())) {
            wrapper.eq(User::getStatus, queryDTO.getStatus());
        }
        wrapper.orderByDesc(User::getCreatedAt);

        IPage<User> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        IPage<User> result = page(page, wrapper);

        List<User> users = result.getRecords();
        Map<Long, UserStats> statsMap = getUserStatsMap(users);

        List<AdminUserVO> voList = users.stream().map(user -> {
            AdminUserVO vo = assembler.toVO(user);
            UserStats stats = statsMap.getOrDefault(user.getId(), UserStats.EMPTY);
            vo.setOrderCount(stats.orderCount);
            vo.setTotalSpent(stats.totalSpent);
            return vo;
        }).collect(Collectors.toList());

        IPage<AdminUserVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(voList);
        return Result.success(voPage);
    }

    @Override
    public Result<AdminUserDetailVO> getUserDetail(Long id) {
        User user = getById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        AdminUserDetailVO vo = assembler.toDetailVO(user);
        UserStats stats = getUserStatsMap(List.of(user)).getOrDefault(id, UserStats.EMPTY);
        vo.setOrderCount(stats.orderCount);
        vo.setTotalSpent(stats.totalSpent);
        return Result.success(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> updateUserStatus(Long id, UserStatusDTO dto) {
        User user = getById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        user.setStatus(dto.getStatus());
        updateById(user);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> assignUserRoles(Long id, UserRoleDTO dto) {
        User user = getById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, id);
        userRoleMapper.delete(wrapper);

        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            for (Long roleId : dto.getRoleIds()) {
                UserRole userRole = new UserRole();
                userRole.setUserId(id);
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }
        return Result.success();
    }

    private Map<Long, UserStats> getUserStatsMap(List<User> users) {
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        if (userIds.isEmpty()) {
            return Map.of();
        }
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .in(Order::getUserId, userIds)
                .eq(Order::getStatus, "COMPLETED");
        List<Order> orders = orderMapper.selectList(wrapper);
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getUserId,
                        Collectors.collectingAndThen(
                                Collectors.reducing(new UserStats(), o -> {
                                    UserStats s = new UserStats();
                                    s.orderCount = 1;
                                    s.totalSpent = o.getTotalAmount();
                                    return s;
                                }, (s1, s2) -> {
                                    s1.orderCount += s2.orderCount;
                                    s1.totalSpent = s1.totalSpent.add(s2.totalSpent);
                                    return s1;
                                }),
                                stats -> stats
                        )));
    }

    private static class UserStats {
        int orderCount = 0;
        BigDecimal totalSpent = BigDecimal.ZERO;
        static final UserStats EMPTY = new UserStats();
    }

}
