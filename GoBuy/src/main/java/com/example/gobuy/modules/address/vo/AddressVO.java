package com.example.gobuy.modules.address.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 收货地址表 VO (视图对象)
 */
@Data
public class AddressVO {

    /**
     * 地址ID
     */
    private Long id;

    /**
     * 关联的用户ID
     */
    private Long userId;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 是否为默认地址
     */
    private Integer isDefault;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;


}