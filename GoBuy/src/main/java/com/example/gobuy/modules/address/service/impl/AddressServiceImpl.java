package com.example.gobuy.modules.address.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.gobuy.common.exception.BusinessException;
import com.example.gobuy.modules.address.dto.AddressDTO;
import com.example.gobuy.modules.address.entity.Address;
import com.example.gobuy.modules.address.mapper.AddressMapper;
import com.example.gobuy.modules.address.service.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收货地址表 服务实现层
 */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

    @Override
    @Transactional(readOnly = true)
    public List<Address> listMyAddresses(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId);
        wrapper.orderByDesc(Address::getIsDefault);
        return list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultAddress(Long userId, Long addressId) {
        Address address = getById(addressId);
        if (address == null) {
            throw new BusinessException(404, "地址不存在");
        }
        if (!userId.equals(address.getUserId())) {
            throw new BusinessException(403, "无权操作该地址");
        }

        LambdaUpdateWrapper<Address> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Address::getUserId, userId);
        wrapper.set(Address::getIsDefault, 0);
        update(wrapper);

        address.setIsDefault(1);
        updateById(address);
    }

    @Override
    @Transactional(readOnly = true)
    public Address getDefaultAddress(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId);
        wrapper.eq(Address::getIsDefault, 1);
        return getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Address createAddress(Long userId, AddressDTO dto) {
        Address entity = new Address();
        entity.setUserId(userId);
        entity.setReceiverName(dto.getReceiverName());
        entity.setPhone(dto.getPhone());
        entity.setProvince(dto.getProvince());
        entity.setCity(dto.getCity());
        entity.setDistrict(dto.getDistrict());
        entity.setDetailAddress(dto.getDetailAddress());
        entity.setIsDefault(dto.getIsDefault() != null ? dto.getIsDefault() : 0);

        if (entity.getIsDefault() == 1) {
            LambdaUpdateWrapper<Address> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Address::getUserId, userId);
            wrapper.set(Address::getIsDefault, 0);
            update(wrapper);
        }

        save(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Address updateAddress(Long userId, Long addressId, AddressDTO dto) {
        Address entity = getById(addressId);
        if (entity == null) {
            throw new BusinessException(404, "地址不存在");
        }
        if (!userId.equals(entity.getUserId())) {
            throw new BusinessException(403, "无权操作该地址");
        }

        entity.setReceiverName(dto.getReceiverName());
        entity.setPhone(dto.getPhone());
        entity.setProvince(dto.getProvince());
        entity.setCity(dto.getCity());
        entity.setDistrict(dto.getDistrict());
        entity.setDetailAddress(dto.getDetailAddress());

        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            if (entity.getIsDefault() != 1) {
                LambdaUpdateWrapper<Address> wrapper = new LambdaUpdateWrapper<>();
                wrapper.eq(Address::getUserId, userId);
                wrapper.set(Address::getIsDefault, 0);
                update(wrapper);
                entity.setIsDefault(1);
            }
        }

        updateById(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAddress(Long userId, Long addressId) {
        Address entity = getById(addressId);
        if (entity == null) {
            throw new BusinessException(404, "地址不存在");
        }
        if (!userId.equals(entity.getUserId())) {
            throw new BusinessException(403, "无权操作该地址");
        }

        removeById(addressId);
    }
}