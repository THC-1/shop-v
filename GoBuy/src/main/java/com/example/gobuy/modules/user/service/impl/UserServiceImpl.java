package com.example.gobuy.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.gobuy.common.exception.BusinessException;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.common.utils.JwtUtil;
import com.example.gobuy.modules.user.assembler.UserAssembler;
import com.example.gobuy.modules.user.dto.UserLoginDTO;
import com.example.gobuy.modules.user.dto.UserRegisterDTO;
import com.example.gobuy.modules.user.dto.UserUpdateDTO;
import com.example.gobuy.modules.user.entity.User;
import com.example.gobuy.modules.user.mapper.UserMapper;
import com.example.gobuy.modules.user.service.IUserService;
import com.example.gobuy.modules.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserAssembler userAssembler;

    @Override
    public Result<String> login(UserLoginDTO dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User user = getOne(wrapper);

        if (user == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        if (!"active".equals(user.getStatus())) {
            throw new BusinessException(403, "账号已被禁用");
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        return Result.success(token);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> register(UserRegisterDTO dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        if (getOne(wrapper) != null) {
            throw new BusinessException(400, "用户名已存在");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setNickname(dto.getUsername());
        user.setStatus("active");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        save(user);
        return Result.success();
    }

    @Override
    public Result<UserVO> getUserInfo(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        return Result.success(userAssembler.toVO(user));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> updateUserInfo(Long userId, UserUpdateDTO dto) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        if (dto.getNickname() != null) {
            user.setNickname(dto.getNickname());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getAvatar() != null) {
            user.setAvatar(dto.getAvatar());
        }
        user.setUpdatedAt(LocalDateTime.now());

        updateById(user);
        return Result.success();
    }

    @Override
    public Result<Void> logout(Long userId) {
        return Result.success();
    }
}
