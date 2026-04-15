package com.example.gobuy.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.gobuy.common.exception.BusinessException;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.common.utils.JwtUtil;
import com.example.gobuy.modules.admin.dto.AdminLoginDTO;
import com.example.gobuy.modules.admin.entity.*;
import com.example.gobuy.modules.admin.mapper.*;
import com.example.gobuy.modules.admin.service.IAdminAuthService;
import com.example.gobuy.modules.admin.vo.AdminLoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements IAdminAuthService {

    private static final String LOGIN_FAIL_KEY_PREFIX = "admin:login:fail:";
    private static final int MAX_LOGIN_FAIL_COUNT = 5;
    private static final long LOGIN_FAIL_TTL_MINUTES = 5;

    private final AdminMapper adminMapper;
    private final AdminRoleMapper adminRoleMapper;
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;
    private final AdminLoginLogMapper adminLoginLogMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public Result<AdminLoginVO> login(AdminLoginDTO dto, String ip, String userAgent) {
        checkLoginFailRate(dto.getUsername());

        Admin admin = findAdminByUsername(dto.getUsername());
        if (admin == null) {
            handleLoginFail(null, dto.getUsername(), ip, userAgent, "用户名或密码错误");
            return Result.fail(401, "用户名或密码错误");
        }

        if (!"ACTIVE".equals(admin.getStatus())) {
            recordLoginLog(admin.getId(), dto.getUsername(), ip, userAgent, "FAILED", "账号已被禁用");
            throw new BusinessException(403, "账号已被禁用");
        }

        if (!passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
            handleLoginFail(admin.getId(), dto.getUsername(), ip, userAgent, "用户名或密码错误");
            return Result.fail(401, "用户名或密码错误");
        }

        return handleLoginSuccess(admin, ip, userAgent);
    }

    @Override
    public Result<Void> logout() {
        return Result.success();
    }

    private void checkLoginFailRate(String username) {
        String key = LOGIN_FAIL_KEY_PREFIX + username;
        String failCountStr = stringRedisTemplate.opsForValue().get(key);
        if (failCountStr != null && Integer.parseInt(failCountStr) >= MAX_LOGIN_FAIL_COUNT) {
            throw new BusinessException(429, "登录失败次数过多，请5分钟后再试");
        }
    }

    private Admin findAdminByUsername(String username) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, username);
        return adminMapper.selectOne(wrapper);
    }

    private void handleLoginFail(Long adminId, String username, String ip, String userAgent, String reason) {
        String key = LOGIN_FAIL_KEY_PREFIX + username;
        stringRedisTemplate.opsForValue().increment(key);
        stringRedisTemplate.expire(key, LOGIN_FAIL_TTL_MINUTES, TimeUnit.MINUTES);
        recordLoginLog(adminId, username, ip, userAgent, "FAILED", reason);
    }

    private Result<AdminLoginVO> handleLoginSuccess(Admin admin, String ip, String userAgent) {
        List<String> roleCodes = getRoleCodes(admin.getId());
        String token = jwtUtil.generateAdminToken(admin.getId(), admin.getUsername(), roleCodes);

        updateLoginInfo(admin.getId(), ip);
        clearLoginFailCount(admin.getUsername());
        recordLoginLog(admin.getId(), admin.getUsername(), ip, userAgent, "SUCCESS", null);

        AdminLoginVO vo = new AdminLoginVO();
        vo.setToken(token);
        vo.setAdminId(admin.getId());
        vo.setUsername(admin.getUsername());
        vo.setNickname(admin.getNickname());
        vo.setRoles(roleCodes);
        return Result.success(vo);
    }

    private List<String> getRoleCodes(Long adminId) {
        List<Long> roleIds = getAdminRoleIds(adminId);
        if (roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Role::getId, roleIds).select(Role::getCode);
        return roleMapper.selectList(wrapper).stream()
                .map(Role::getCode)
                .collect(Collectors.toList());
    }

    private List<Long> getAdminRoleIds(Long adminId) {
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getAdminId, adminId).select(AdminRole::getRoleId);
        return adminRoleMapper.selectList(wrapper).stream()
                .map(AdminRole::getRoleId)
                .collect(Collectors.toList());
    }

    private void updateLoginInfo(Long adminId, String ip) {
        Admin update = new Admin();
        update.setId(adminId);
        update.setLastLoginAt(LocalDateTime.now());
        update.setLastLoginIp(ip);
        adminMapper.updateById(update);
    }

    private void clearLoginFailCount(String username) {
        stringRedisTemplate.delete(LOGIN_FAIL_KEY_PREFIX + username);
    }

    private void recordLoginLog(Long adminId, String username, String ip, String userAgent, String status, String failReason) {
        AdminLoginLog log = new AdminLoginLog();
        log.setAdminId(adminId);
        log.setUsername(username);
        log.setIp(ip);
        log.setUserAgent(userAgent);
        log.setStatus(status);
        log.setFailReason(failReason);
        log.setLoginAt(LocalDateTime.now());
        adminLoginLogMapper.insert(log);
    }
}
