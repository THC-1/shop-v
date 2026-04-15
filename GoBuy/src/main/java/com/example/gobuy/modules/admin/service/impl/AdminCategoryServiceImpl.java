package com.example.gobuy.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.gobuy.common.exception.BusinessException;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.assembler.AdminCategoryAssembler;
import com.example.gobuy.modules.admin.dto.CategoryCreateDTO;
import com.example.gobuy.modules.admin.dto.CategoryUpdateDTO;
import com.example.gobuy.modules.admin.service.IAdminCategoryService;
import com.example.gobuy.modules.admin.vo.CategoryDetailVO;
import com.example.gobuy.modules.admin.vo.CategoryTreeVO;
import com.example.gobuy.modules.product.entity.Category;
import com.example.gobuy.modules.product.entity.Product;
import com.example.gobuy.modules.product.mapper.CategoryMapper;
import com.example.gobuy.modules.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements IAdminCategoryService {

    private final AdminCategoryAssembler assembler;
    private final ProductMapper productMapper;

    @Override
    public Result<List<CategoryTreeVO>> getCategoryTree() {
        List<Category> allCategories = list();
        Map<Long, List<Category>> childrenMap = allCategories.stream()
                .filter(c -> c.getParentId() != null)
                .collect(Collectors.groupingBy(Category::getParentId));

        List<CategoryTreeVO> voList = assembler.toTreeVOList(allCategories);
        Map<Long, CategoryTreeVO> voMap = voList.stream()
                .collect(Collectors.toMap(CategoryTreeVO::getId, v -> v, (a, b) -> a));

        List<CategoryTreeVO> rootList = new ArrayList<>();
        for (CategoryTreeVO vo : voList) {
            if (vo.getParentId() == null) {
                rootList.add(vo);
            } else {
                CategoryTreeVO parent = voMap.get(vo.getParentId());
                if (parent != null) {
                    parent.getChildren().add(vo);
                }
            }
        }
        rootList.sort((a, b) -> a.getSort().compareTo(b.getSort()));
        return Result.success(rootList);
    }

    @Override
    public Result<CategoryDetailVO> getCategoryDetail(Long id) {
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException(404, "分类不存在");
        }
        CategoryDetailVO vo = assembler.toDetailVO(category);
        vo.setLevel(calculateLevel(category));
        vo.setChildrenCount(countChildren(id));
        vo.setProductCount(countProducts(id));
        return Result.success(vo);
    }

    @Override
    public Result<Long> createCategory(CategoryCreateDTO dto) {
        if (dto.getParentId() != null) {
            Category parent = getById(dto.getParentId());
            if (parent == null) {
                throw new BusinessException(400, "父分类不存在");
            }
            int level = calculateLevel(parent);
            if (level >= 3) {
                throw new BusinessException(400, "分类最多支持 3 级嵌套");
            }
        }
        Category category = assembler.toEntity(dto);
        category.setStatus("ACTIVE");
        save(category);
        return Result.success(category.getId());
    }

    @Override
    public Result<Void> updateCategory(Long id, CategoryUpdateDTO dto) {
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException(404, "分类不存在");
        }
        if (dto.getParentId() != null) {
            if (dto.getParentId().equals(id)) {
                throw new BusinessException(400, "不能将自己设为父分类");
            }
            Category parent = getById(dto.getParentId());
            if (parent == null) {
                throw new BusinessException(400, "父分类不存在");
            }
            int level = calculateLevel(parent);
            if (level >= 3) {
                throw new BusinessException(400, "分类最多支持 3 级嵌套");
            }
        }
        assembler.updateEntity(category, dto);
        updateById(category);
        return Result.success();
    }

    @Override
    @Transactional
    public Result<Void> deleteCategory(Long id) {
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException(404, "分类不存在");
        }
        if (countChildren(id) > 0) {
            throw new BusinessException(422, "该分类下存在子分类，无法删除");
        }
        if (countProducts(id) > 0) {
            throw new BusinessException(422, "该分类下存在商品，无法删除");
        }
        removeById(id);
        return Result.success();
    }

    private int calculateLevel(Category category) {
        if (category.getParentId() == null) {
            return 1;
        }
        Category parent = getById(category.getParentId());
        if (parent == null || parent.getParentId() == null) {
            return 2;
        }
        return 3;
    }

    private int countChildren(Long parentId) {
        return Math.toIntExact(count(new LambdaQueryWrapper<Category>().eq(Category::getParentId, parentId)));
    }

    private int countProducts(Long categoryId) {
        return Math.toIntExact(productMapper.selectCount(new LambdaQueryWrapper<Product>().eq(Product::getCategoryId, categoryId)));
    }
}
