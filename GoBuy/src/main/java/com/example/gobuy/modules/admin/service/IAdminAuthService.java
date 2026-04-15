package com.example.gobuy.modules.admin.service;

import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.dto.AdminLoginDTO;
import com.example.gobuy.modules.admin.vo.AdminLoginVO;

public interface IAdminAuthService {

    Result<AdminLoginVO> login(AdminLoginDTO dto, String ip, String userAgent);

    Result<Void> logout();
}
