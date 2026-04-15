package com.example.gobuy.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.gobuy.common.exception.BusinessException;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.common.utils.AdminContextHolder;
import com.example.gobuy.modules.admin.assembler.AdminAssembler;
import com.example.gobuy.modules.admin.dto.AdminCreateDTO;
import com.example.gobuy.modules.admin.dto.AdminQueryDTO;
import com.example.gobuy.modules.admin.dto.AdminUpdateDTO;
import com.example.gobuy.modules.admin.entity.Admin;
import com.example.gobuy.modules.admin.entity.AdminRole;
import com.example.gobuy.modules.admin.entity.Role;
import com.example.gobuy.modules.admin.mapper.AdminMapper;
import com.example.gobuy.modules.admin.mapper.AdminRoleMapper;
import com.example.gobuy.modules.admin.mapper.RoleMapper;
import com.example.gobuy.modules.admin.service.IAdminService;
import com.example.gobuy.modules.admin.vo.AdminDetailVO;
import com.example.gobuy.modules.admin.vo.AdminVO;
import com.example.gobuy.modules.admin.vo.RoleSimpleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements IAdminService {

    private final AdminMapper adminMapper;
    private final AdminRoleMapper adminRoleMapper;
    private final RoleMapper roleMapper;
    private final AdminAssembler adminAssembler;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<Map<String, Object>> listAdmins(AdminQueryDTO dto) {
        Page<Admin> page = new Page<>(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(dto.getUsername()), Admin::getUsername, dto.getUsername());
        wrapper.eq(StringUtils.hasText(dto.getStatus()), Admin::getStatus, dto.getStatus());
        wrapper.orderByDesc(Admin::getCreatedAt);

        Page<Admin> result = adminMapper.selectPage(page, wrapper);
        List<AdminVO> voList = adminAssembler.toVOList(result.getRecords());

        List<Long> adminIds = voList.stream().map(AdminVO::getId).collect(Collectors.toList());
        Map<Long, List<String>> roleNamesMap = batchGetRoleNamesMap(adminIds);

        voList.forEach(vo -> vo.setRoles(roleNamesMap.getOrDefault(vo.getId(), Collections.emptyList())));

        Map<String, Object> data = new HashMap<>();
        data.put("total", result.getTotal());
        data.put("list", voList);
        return Result.success(data);
    }

    @Override
    public Result<AdminDetailVO> getAdminDetail(Long id) {
        Admin admin = adminMapper.selectById(id);
        if (admin == null) {
            throw new BusinessException(404, "管理员不存在");
        }

        AdminDetailVO vo = adminAssembler.toDetailVO(admin);
        vo.setRoles(getRoleSimpleVOListByAdminId(id));
        return Result.success(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Map<String, Object>> createAdmin(AdminCreateDTO dto) {
        checkUsernameUnique(dto.getUsername());

        Admin admin = new Admin();
        admin.setUsername(dto.getUsername());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setNickname(dto.getNickname());
        admin.setEmail(dto.getEmail());
        admin.setPhone(dto.getPhone());
        admin.setStatus("ACTIVE");
        admin.setCreatedBy(AdminContextHolder.getRequiredAdminId());
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        adminMapper.insert(admin);

        saveAdminRoles(admin.getId(), dto.getRoleIds());

        Map<String, Object> data = new HashMap<>();
        data.put("id", admin.getId());
        return Result.success(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> updateAdmin(Long id, AdminUpdateDTO dto) {
        Admin admin = adminMapper.selectById(id);
        if (admin == null) {
            throw new BusinessException(404, "管理员不存在");
        }

        if (isSuperAdmin(id) && StringUtils.hasText(dto.getStatus())) {
            throw new BusinessException(403, "超级管理员状态不可修改");
        }

        Admin update = new Admin();
        update.setId(id);
        update.setNickname(dto.getNickname());
        update.setEmail(dto.getEmail());
        update.setPhone(dto.getPhone());
        update.setStatus(dto.getStatus());
        update.setUpdatedAt(LocalDateTime.now());
        adminMapper.updateById(update);

        if (dto.getRoleIds() != null) {
            if (isSuperAdmin(id)) {
                throw new BusinessException(403, "超级管理员角色不可修改");
            }
            deleteAdminRolesByAdminId(id);
            saveAdminRoles(id, dto.getRoleIds());
        }

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> deleteAdmin(Long id) {
        if (isSuperAdmin(id)) {
            throw new BusinessException(403, "超级管理员不可删除");
        }

        Admin admin = adminMapper.selectById(id);
        if (admin == null) {
            throw new BusinessException(404, "管理员不存在");
        }

        Admin update = new Admin();
        update.setId(id);
        update.setStatus("DISABLED");
        update.setUpdatedAt(LocalDateTime.now());
        adminMapper.updateById(update);

        deleteAdminRolesByAdminId(id);

        return Result.success();
    }

    private void checkUsernameUnique(String username) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, username);
        if (adminMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(400, "用户名已存在");
        }
    }

    private boolean isSuperAdmin(Long adminId) {
        return adminId != null && adminId == 1L;
    }

    private List<String> getRoleNamesByAdminId(Long adminId) {
        List<Long> roleIds = getAdminRoleIds(adminId);
        if (roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Role::getId, roleIds).select(Role::getName);
        return roleMapper.selectList(wrapper).stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

    private List<RoleSimpleVO> getRoleSimpleVOListByAdminId(Long adminId) {
        List<Long> roleIds = getAdminRoleIds(adminId);
        if (roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Role::getId, roleIds);
        return adminAssembler.toRoleSimpleVOList(roleMapper.selectList(wrapper));
    }

    private List<Long> getAdminRoleIds(Long adminId) {
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getAdminId, adminId).select(AdminRole::getRoleId);
        return adminRoleMapper.selectList(wrapper).stream()
                .map(AdminRole::getRoleId)
                .collect(Collectors.toList());
    }

    private Map<Long, List<String>> batchGetRoleNamesMap(List<Long> adminIds) {
        if (adminIds.isEmpty()) {
            return Map.of();
        }
        LambdaQueryWrapper<AdminRole> adminRoleWrapper = new LambdaQueryWrapper<>();
        adminRoleWrapper.in(AdminRole::getAdminId, adminIds).select(AdminRole::getAdminId, AdminRole::getRoleId);
        List<AdminRole> adminRoles = adminRoleMapper.selectList(adminRoleWrapper);

        List<Long> allRoleIds = adminRoles.stream().map(AdminRole::getRoleId).distinct().collect(Collectors.toList());
        if (allRoleIds.isEmpty()) {
            return Map.of();
        }

        LambdaQueryWrapper<Role> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.in(Role::getId, allRoleIds).select(Role::getId, Role::getName);
        List<Role> roles = roleMapper.selectList(roleWrapper);
        Map<Long, String> roleIdNameMap = roles.stream().collect(Collectors.toMap(Role::getId, Role::getName));

        Map<Long, List<String>> result = new HashMap<>();
        for (Long adminId : adminIds) {
            result.put(adminId, new ArrayList<>());
        }
        for (AdminRole ar : adminRoles) {
            String roleName = roleIdNameMap.get(ar.getRoleId());
            if (roleName != null) {
                result.get(ar.getAdminId()).add(roleName);
            }
        }
        return result;
    }

    private void saveAdminRoles(Long adminId, List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return;
        }
        for (Long roleId : roleIds) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            adminRole.setCreatedAt(LocalDateTime.now());
            adminRoleMapper.insert(adminRole);
        }
    }

    private void deleteAdminRolesByAdminId(Long adminId) {
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getAdminId, adminId);
        adminRoleMapper.delete(wrapper);
    }
}
