package com.example.gobuy.modules.user.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户表 VO (视图对象)
 */
@Data
public class UserVO {

    /**
     * 真实用户ID
     */
    private Long id;

    /**
     * 账户登录名
     */
    private String username;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 联系手机
     */
    private String phone;

    /**
     * 页面展示昵称
     */
    private String nickname;

    /**
     * 头像缩略图链接
     */
    private String avatar;

    /**
     * 账户状态：active/disabled
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;


}