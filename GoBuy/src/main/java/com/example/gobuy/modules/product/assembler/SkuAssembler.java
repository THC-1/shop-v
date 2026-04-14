package com.example.gobuy.modules.product.assembler;

import com.example.gobuy.modules.product.entity.Sku;
import com.example.gobuy.modules.product.vo.SkuVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SkuAssembler {
    SkuVO toVO(Sku entity);
    List<SkuVO> toVOList(List<Sku> entityList);
}