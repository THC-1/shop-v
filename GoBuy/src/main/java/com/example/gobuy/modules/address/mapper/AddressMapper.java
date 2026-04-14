package com.example.gobuy.modules.address.mapper;

import com.example.gobuy.modules.address.entity.Address;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收货地址表 Mapper 接口
 */
@Mapper
public interface AddressMapper extends BaseMapper<Address> {
}