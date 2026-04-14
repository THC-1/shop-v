package com.example.gobuy.modules.address.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

/**
 * 收货地址表 DTO (数据传输对象)
 */
@Data
public class AddressDTO {

    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    private String receiverName;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;

    /**
     * 省份
     */
    @NotBlank(message = "省份不能为空")
    private String province;

    /**
     * 城市
     */
    @NotBlank(message = "城市不能为空")
    private String city;

    /**
     * 区县
     */
    @NotBlank(message = "区县不能为空")
    private String district;

    /**
     * 详细地址
     */
    @NotBlank(message = "详细地址不能为空")
    private String detailAddress;

    /**
     * 是否为默认地址 (0: 非默认，1: 默认)
     */
    private Integer isDefault;
}