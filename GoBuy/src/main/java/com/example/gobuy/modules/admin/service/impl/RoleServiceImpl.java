package com.example.gobuy.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.gobuy.common.exception.BusinessException;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.assembler.RoleAssembler;
import com.example.gobuy.modules.admin.dto.PermissionAssignDTO;
import com.example.gobuy.modules.admin.dto.RoleCreateDTO;
import com.example.gobuy.modules.admin.dto.RoleQueryDTO;
import com.example.gobuy.modules.admin.dto.RoleUpdateDTO;
import com.example.gobuy.modules.admin.entity.AdminRole;
import com.example.gobuy.modules.admin.entity.Permission;
import com.example.gobuy.modules.admin.entity.Role;
import com.example.gobuy.modules.admin.entity.RolePermission;
import com.example.gobuy.modules.admin.mapper.AdminRoleMapper;
import com.example.gobuy.modules.admin.mapper.PermissionMapper;
import com.example.gobuy.modules.admin.mapper.RoleMapper;
import com.example.gobuy.modules.admin.mapper.RolePermissionMapper;
import com.example.gobuy.modules.admin.service.IRoleService;
import com.example.gobuy.modules.admin.vo.PermissionSimpleVO;
import com.example.gobuy.modules.admin.vo.PermissionTreeVO;
import com.example.gobuy.modules.admin.vo.RoleDetailVO;
import com.example.gobuy.modules.admin.vo.RoleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final AdminRoleMapper adminRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final RoleAssembler roleAssembler;

    @Override
    public Result<Map<String, Object>> listRoles(RoleQueryDTO dto) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (dto.getName() != null && !dto.getName().isEmpty()) {
            wrapper.like(Role::getName, dto.getName());
        }
        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            wrapper.eq(Role::getStatus, dto.getStatus());
        }
        wrapper.orderByAsc(Role::getSort).orderByDesc(Role::getCreatedAt);

        Page<Role> page = roleMapper.selectPage(new Page<>(dto.getPage(), dto.getSize()), wrapper);
        List<RoleVO> voList = roleAssembler.toVOList(page.getRecords());

        List<Long> roleIds = voList.stream().map(RoleVO::getId).collect(Collectors.toList());
        Map<Long, Long> userCountMap = batchCountAdminUsers(roleIds);

        voList.forEach(vo -> {
            vo.setUserCount(userCountMap.getOrDefault(vo.getId(), 0L).intValue());
        });

        Map<String, Object> result = new HashMap<>();
        result.put("total", page.getTotal());
        result.put("list", voList);
        return Result.success(result);
    }

    @Override
    public Result<RoleDetailVO> getRoleDetail(Long id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(404, "角色不存在");
        }

        RoleDetailVO detailVO = new RoleDetailVO();
        detailVO.setId(role.getId());
        detailVO.setName(role.getName());
        detailVO.setCode(role.getCode());
        detailVO.setDescription(role.getDescription());
        detailVO.setIsSystem(role.getIsSystem() != null && role.getIsSystem() == 1);
        detailVO.setStatus(role.getStatus());
        detailVO.setCreatedAt(role.getCreatedAt());
        detailVO.setUpdatedAt(role.getUpdatedAt());

        List<Long> permissionIds = rolePermissionMapper.selectList(
                new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, id)
        ).stream().map(RolePermission::getPermissionId).collect(Collectors.toList());

        if (!permissionIds.isEmpty()) {
            List<Permission> permissions = permissionMapper.selectList(
                    new LambdaQueryWrapper<Permission>().in(Permission::getId, permissionIds)
            );
            detailVO.setPermissions(roleAssembler.toSimpleVOList(permissions));
        } else {
            detailVO.setPermissions(new ArrayList<>());
        }

        return Result.success(detailVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Map<String, Object>> createRole(RoleCreateDTO dto) {
        checkCodeUniqueness(dto.getCode(), null);

        Role role = roleAssembler.toEntity(dto);
        role.setStatus("ACTIVE");
        role.setIsSystem(0);
        roleMapper.insert(role);

        if (dto.getPermissionIds() != null && !dto.getPermissionIds().isEmpty()) {
            batchInsertRolePermissions(role.getId(), dto.getPermissionIds());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", role.getId());
        return Result.success(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> updateRole(Long id, RoleUpdateDTO dto) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(404, "角色不存在");
        }
        if (role.getIsSystem() != null && role.getIsSystem() == 1) {
            throw new BusinessException(422, "系统角色不可修改");
        }

        if (dto.getName() != null) {
            role.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            role.setDescription(dto.getDescription());
        }
        if (dto.getSort() != null) {
            role.setSort(dto.getSort());
        }
        if (dto.getStatus() != null) {
            role.setStatus(dto.getStatus());
        }
        roleMapper.updateById(role);

        if (dto.getPermissionIds() != null) {
            rolePermissionMapper.delete(
                    new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, id)
            );
            if (!dto.getPermissionIds().isEmpty()) {
                batchInsertRolePermissions(id, dto.getPermissionIds());
            }
        }

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> deleteRole(Long id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(404, "角色不存在");
        }
        if (role.getIsSystem() != null && role.getIsSystem() == 1) {
            throw new BusinessException(422, "系统角色不可删除");
        }

        Long userCount = adminRoleMapper.selectCount(
                new LambdaQueryWrapper<AdminRole>().eq(AdminRole::getRoleId, id)
        );
        if (userCount > 0) {
            throw new BusinessException(422, "该角色下存在用户，无法删除");
        }

        roleMapper.deleteById(id);
        rolePermissionMapper.delete(
                new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, id)
        );

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> assignPermissions(Long roleId, PermissionAssignDTO dto) {
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException(404, "角色不存在");
        }

        rolePermissionMapper.delete(
                new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, roleId)
        );

        if (dto.getPermissionIds() != null && !dto.getPermissionIds().isEmpty()) {
            batchInsertRolePermissions(roleId, dto.getPermissionIds());
        }

        return Result.success();
    }

    @Override
    public Result<List<PermissionTreeVO>> getPermissionTree() {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getStatus, "ACTIVE").orderByAsc(Permission::getSort);
        List<Permission> permissions = permissionMapper.selectList(wrapper);
        return Result.success(buildTree(permissions));
    }

    private void checkCodeUniqueness(String code, Long excludeId) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getCode, code);
        if (excludeId != null) {
            wrapper.ne(Role::getId, excludeId);
        }
        if (roleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(422, "角色代码已存在");
        }
    }

    private Map<Long, Long> batchCountAdminUsers(List<Long> roleIds) {
        if (roleIds.isEmpty()) {
            return Map.of();
        }
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<AdminRole>()
                .in(AdminRole::getRoleId, roleIds)
                .select(AdminRole::getRoleId);
        List<AdminRole> adminRoles = adminRoleMapper.selectList(wrapper);
        return adminRoles.stream()
                .collect(Collectors.groupingBy(AdminRole::getRoleId, Collectors.counting()));
    }

    private void batchInsertRolePermissions(Long roleId, List<Long> permissionIds) {
        for (Long permissionId : permissionIds) {
            RolePermission rp = new RolePermission();
            rp.setRoleId(roleId);
            rp.setPermissionId(permissionId);
            rp.setCreatedAt(LocalDateTime.now());
            rolePermissionMapper.insert(rp);
        }
    }

    private List<PermissionTreeVO> buildTree(List<Permission> permissions) {
        List<PermissionTreeVO> allNodes = roleAssembler.toTreeVOList(permissions);
        Map<Long, List<PermissionTreeVO>> childrenMap = allNodes.stream()
                .filter(n -> n.getParentId() != null)
                .collect(Collectors.groupingBy(PermissionTreeVO::getParentId));
        allNodes.forEach(n -> n.setChildren(childrenMap.getOrDefault(n.getId(), new ArrayList<>())));
        return allNodes.stream()
                .filter(n -> n.getParentId() == null)
                .collect(Collectors.toList());
    }
}
