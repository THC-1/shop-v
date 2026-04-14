package com.example.gobuy.modules.product.assembler;

import com.example.gobuy.modules.product.entity.Product;
import com.example.gobuy.modules.product.dto.ProductDTO;
import com.example.gobuy.modules.product.vo.ProductVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import java.util.List;

/**
 * 产品表 实体转换装配器
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductAssembler {

    /**
     * DTO 转 Entity
     * @param dto DTO 对象
     * @return Entity 对象
     */
    Product toEntity(ProductDTO dto);

    /**
     * Entity 转 VO
     * @param entity Entity 对象
     * @return VO 对象
     */
    ProductVO toVO(Product entity);

    /**
     * Entity 列表 转 VO 列表
     * @param entityList Entity 列表
     * @return VO 列表
     */
    List<ProductVO> toVOList(List<Product> entityList);
}