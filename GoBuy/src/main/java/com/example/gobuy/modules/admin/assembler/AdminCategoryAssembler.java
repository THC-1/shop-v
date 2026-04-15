package com.example.gobuy.modules.admin.assembler;

import com.example.gobuy.modules.admin.dto.CategoryCreateDTO;
import com.example.gobuy.modules.admin.dto.CategoryUpdateDTO;
import com.example.gobuy.modules.admin.vo.CategoryDetailVO;
import com.example.gobuy.modules.admin.vo.CategoryTreeVO;
import com.example.gobuy.modules.product.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdminCategoryAssembler {
    Category toEntity(CategoryCreateDTO dto);
    void updateEntity(@MappingTarget Category category, CategoryUpdateDTO dto);
    CategoryTreeVO toTreeVO(Category category);
    List<CategoryTreeVO> toTreeVOList(List<Category> categories);
    CategoryDetailVO toDetailVO(Category category);
}
