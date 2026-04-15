package com.example.gobuy.modules.admin.assembler;

import com.example.gobuy.modules.admin.dto.BrandCreateDTO;
import com.example.gobuy.modules.admin.dto.BrandUpdateDTO;
import com.example.gobuy.modules.admin.vo.BrandDetailVO;
import com.example.gobuy.modules.admin.vo.BrandVO;
import com.example.gobuy.modules.product.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdminBrandAssembler {
    Brand toEntity(BrandCreateDTO dto);
    void updateEntity(@MappingTarget Brand brand, BrandUpdateDTO dto);
    BrandVO toVO(Brand brand);
    List<BrandVO> toVOList(List<Brand> brands);
    BrandDetailVO toDetailVO(Brand brand);
}
