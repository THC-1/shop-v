package com.example.gobuy.modules.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.dto.AdminUserQueryDTO;
import com.example.gobuy.modules.admin.dto.UserRoleDTO;
import com.example.gobuy.modules.admin.dto.UserStatusDTO;
import com.example.gobuy.modules.admin.vo.AdminUserDetailVO;
import com.example.gobuy.modules.admin.vo.AdminUserVO;

public interface IAdminUserService {
    Result<IPage<AdminUserVO>> listUsers(AdminUserQueryDTO queryDTO);
    Result<AdminUserDetailVO> getUserDetail(Long id);
    Result<Void> updateUserStatus(Long id, UserStatusDTO dto);
    Result<Void> assignUserRoles(Long id, UserRoleDTO dto);
}
