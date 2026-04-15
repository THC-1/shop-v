package com.example.gobuy.modules.admin.assembler;

import com.example.gobuy.modules.admin.dto.AdminProductCreateDTO;
import com.example.gobuy.modules.admin.dto.AdminProductUpdateDTO;
import com.example.gobuy.modules.admin.vo.AdminProductDetailVO;
import com.example.gobuy.modules.admin.vo.AdminProductVO;
import com.example.gobuy.modules.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdminProductAssembler {
    Product toEntity(AdminProductCreateDTO dto);
    void updateEntity(@MappingTarget Product product, AdminProductUpdateDTO dto);
    AdminProductVO toVO(Product product);
    List<AdminProductVO> toVOList(List<Product> products);
    AdminProductDetailVO toDetailVO(Product product);
}
