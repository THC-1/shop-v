package com.example.gobuy.modules.address.assembler;

import com.example.gobuy.modules.address.entity.Address;
import com.example.gobuy.modules.address.dto.AddressDTO;
import com.example.gobuy.modules.address.vo.AddressVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import java.util.List;

/**
 * 收货地址表 实体转换装配器
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressAssembler {

    /**
     * DTO 转 Entity
     * @param dto DTO 对象
     * @return Entity 对象
     */
    Address toEntity(AddressDTO dto);

    /**
     * Entity 转 VO
     * @param entity Entity 对象
     * @return VO 对象
     */
    AddressVO toVO(Address entity);

    /**
     * Entity 列表 转 VO 列表
     * @param entityList Entity 列表
     * @return VO 列表
     */
    List<AddressVO> toVOList(List<Address> entityList);
}