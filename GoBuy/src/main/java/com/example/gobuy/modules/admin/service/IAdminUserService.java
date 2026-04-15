package com.example.gobuy.modules.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.gobuy.modules.admin.dto.AdminUserQueryDTO;
import com.example.gobuy.modules.admin.dto.UserRoleDTO;
import com.example.gobuy.modules.admin.dto.UserStatusDTO;
import com.example.gobuy.modules.admin.vo.AdminUserDetailVO;
import com.example.gobuy.modules.admin.vo.AdminUserVO;
import com.example.gobuy.modules.user.entity.User;

public interface IAdminUserService extends IService<User> {
    IPage<AdminUserVO> listUsers(AdminUserQueryDTO queryDTO);
    AdminUserDetailVO getUserDetail(Long id);
    void updateUserStatus(Long id, UserStatusDTO dto);
    void assignUserRoles(Long id, UserRoleDTO dto);
}
