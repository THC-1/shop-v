package com.example.gobuy.modules.user.controller;

import com.example.gobuy.common.result.Result;
import com.example.gobuy.common.utils.UserContextHolder;
import com.example.gobuy.modules.user.dto.UserLoginDTO;
import com.example.gobuy.modules.user.dto.UserRegisterDTO;
import com.example.gobuy.modules.user.dto.UserUpdateDTO;
import com.example.gobuy.modules.user.service.IUserService;
import com.example.gobuy.modules.user.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理", description = "用户注册、登录、信息管理等相关接口")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户名密码登录，返回 JWT Token")
    @SecurityRequirement(name = "")
    public Result<String> login(@Valid @RequestBody UserLoginDTO dto) {
        return userService.login(dto);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "注册新用户账号")
    @SecurityRequirement(name = "")
    public Result<Void> register(@Valid @RequestBody UserRegisterDTO dto) {
        return userService.register(dto);
    }

    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @SecurityRequirement(name = "Bearer Authentication")
    public Result<UserVO> getCurrentUserInfo() {
        Long userId = UserContextHolder.getRequiredUserId();
        return userService.getUserInfo(userId);
    }

    @PutMapping("/me")
    @Operation(summary = "更新当前用户信息", description = "更新当前登录用户的信息")
    @SecurityRequirement(name = "Bearer Authentication")
    public Result<Void> updateCurrentUserInfo(@Valid @RequestBody UserUpdateDTO dto) {
        Long userId = UserContextHolder.getRequiredUserId();
        return userService.updateUserInfo(userId, dto);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "退出登录")
    @SecurityRequirement(name = "Bearer Authentication")
    public Result<Void> logout() {
        Long userId = UserContextHolder.getRequiredUserId();
        return userService.logout(userId);
    }
}
