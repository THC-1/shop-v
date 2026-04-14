package com.example.gobuy.modules.address.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.gobuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("addresses")
public class Address extends BaseEntity {

    private Long userId;

    private String receiverName;

    private String phone;

    private String province;

    private String city;

    private String district;

    private String detailAddress;

    private Integer isDefault;
}
