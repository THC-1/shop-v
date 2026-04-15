package com.example.gobuy.modules.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.gobuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("users")
public class User extends BaseEntity {

    private String username;

    private String password;

    private String email;

    private String phone;

    private String nickname;

    private String avatar;

    private String status = "ACTIVE";
}
