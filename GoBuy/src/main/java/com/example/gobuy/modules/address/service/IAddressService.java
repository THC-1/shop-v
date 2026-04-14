package com.example.gobuy.modules.address.service;

import com.example.gobuy.modules.address.dto.AddressDTO;
import com.example.gobuy.modules.address.entity.Address;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 收货地址表 服务接口
 */
public interface IAddressService extends IService<Address> {

    /**
     * 获取当前用户的地址列表
     * @param userId 用户 ID
     * @return 地址列表
     */
    List<Address> listMyAddresses(Long userId);

    /**
     * 设置默认地址
     * @param userId 用户 ID
     * @param addressId 地址 ID
     */
    void setDefaultAddress(Long userId, Long addressId);

    /**
     * 获取默认地址
     * @param userId 用户 ID
     * @return 默认地址，如果没有则返回 null
     */
    Address getDefaultAddress(Long userId);

    /**
     * 创建地址
     * @param userId 用户 ID
     * @param dto 地址 DTO
     * @return 创建的地址
     */
    Address createAddress(Long userId, AddressDTO dto);

    /**
     * 更新地址
     * @param userId 用户 ID
     * @param addressId 地址 ID
     * @param dto 地址 DTO
     * @return 更新后的地址
     */
    Address updateAddress(Long userId, Long addressId, AddressDTO dto);

    /**
     * 删除地址
     * @param userId 用户 ID
     * @param addressId 地址 ID
     */
    void deleteAddress(Long userId, Long addressId);
}