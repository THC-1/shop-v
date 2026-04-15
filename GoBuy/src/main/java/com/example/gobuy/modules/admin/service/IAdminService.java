package com.example.gobuy.modules.admin.service;

import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.dto.AdminCreateDTO;
import com.example.gobuy.modules.admin.dto.AdminQueryDTO;
import com.example.gobuy.modules.admin.dto.AdminUpdateDTO;
import com.example.gobuy.modules.admin.vo.AdminDetailVO;

import java.util.Map;

public interface IAdminService {

    Result<Map<String, Object>> listAdmins(AdminQueryDTO dto);

    Result<AdminDetailVO> getAdminDetail(Long id);

    Result<Map<String, Object>> createAdmin(AdminCreateDTO dto);

    Result<Void> updateAdmin(Long id, AdminUpdateDTO dto);

    Result<Void> deleteAdmin(Long id);
}
