package com.example.gobuy.modules.admin.service;

import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.dto.PermissionAssignDTO;
import com.example.gobuy.modules.admin.dto.RoleCreateDTO;
import com.example.gobuy.modules.admin.dto.RoleQueryDTO;
import com.example.gobuy.modules.admin.dto.RoleUpdateDTO;
import com.example.gobuy.modules.admin.vo.PermissionTreeVO;
import com.example.gobuy.modules.admin.vo.RoleDetailVO;

import java.util.List;
import java.util.Map;

public interface IRoleService {

    Result<Map<String, Object>> listRoles(RoleQueryDTO dto);

    Result<RoleDetailVO> getRoleDetail(Long id);

    Result<Map<String, Object>> createRole(RoleCreateDTO dto);

    Result<Void> updateRole(Long id, RoleUpdateDTO dto);

    Result<Void> deleteRole(Long id);

    Result<Void> assignPermissions(Long roleId, PermissionAssignDTO dto);

    Result<List<PermissionTreeVO>> getPermissionTree();
}
