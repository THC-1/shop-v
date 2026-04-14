package com.example.gobuy.modules.user.service;

import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.user.dto.UserLoginDTO;
import com.example.gobuy.modules.user.dto.UserRegisterDTO;
import com.example.gobuy.modules.user.dto.UserUpdateDTO;
import com.example.gobuy.modules.user.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.gobuy.modules.user.vo.UserVO;

/**
 * 用户表 服务接口
 */
public interface IUserService extends IService<User> {
    
    /**
     * 用户登录
     * @param dto 登录信息
     * @return 登录结果（包含 token）
     */
    Result<String> login(UserLoginDTO dto);
    
    /**
     * 用户注册
     * @param dto 注册信息
     * @return 注册结果
     */
    Result<Void> register(UserRegisterDTO dto);
    
    /**
     * 获取用户信息
     * @param userId 用户 ID
     * @return 用户信息
     */
    Result<UserVO> getUserInfo(Long userId);
    
    /**
     * 更新用户信息
     * @param userId 用户 ID
     * @param dto 更新信息
     * @return 更新结果
     */
    Result<Void> updateUserInfo(Long userId, UserUpdateDTO dto);
    
    /**
     * 用户登出
     * @param userId 用户 ID
     * @return 登出结果
     */
    Result<Void> logout(Long userId);
}