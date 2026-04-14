package com.example.gobuy.modules.user.dto;

import lombok.Data;

/**
 * 用户更新 DTO
 */
@Data
public class UserUpdateDTO {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像 URL
     */
    private String avatar;
}
