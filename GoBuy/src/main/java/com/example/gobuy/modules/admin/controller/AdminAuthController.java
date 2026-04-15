package com.example.gobuy.modules.admin.controller;

import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.dto.AdminLoginDTO;
import com.example.gobuy.modules.admin.service.IAdminAuthService;
import com.example.gobuy.modules.admin.vo.AdminLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理员认证")
@RestController
@RequestMapping("/api/v1/admin/sessions")
@RequiredArgsConstructor
public class AdminAuthController {

    private final IAdminAuthService adminAuthService;

    @PostMapping
    @Operation(summary = "管理员登录")
    public Result<AdminLoginVO> login(@Valid @RequestBody AdminLoginDTO dto, HttpServletRequest request) {
        String ip = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        return adminAuthService.login(dto, ip, userAgent);
    }

    @DeleteMapping
    @Operation(summary = "管理员登出")
    public Result<Void> logout() {
        return adminAuthService.logout();
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (org.springframework.util.StringUtils.hasText(xForwardedFor)) {
            String firstIp = xForwardedFor.split(",")[0].trim();
            if (isValidIp(firstIp)) {
                return firstIp;
            }
        }
        return request.getRemoteAddr();
    }

    private boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        if (ip.startsWith("10.") || ip.startsWith("192.168.") || ip.startsWith("172.")) {
            return false;
        }
        return true;
    }
}
