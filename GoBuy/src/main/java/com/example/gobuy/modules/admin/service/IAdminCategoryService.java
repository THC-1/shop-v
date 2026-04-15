package com.example.gobuy.modules.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.gobuy.modules.admin.dto.CategoryCreateDTO;
import com.example.gobuy.modules.admin.dto.CategoryUpdateDTO;
import com.example.gobuy.modules.admin.vo.CategoryDetailVO;
import com.example.gobuy.modules.admin.vo.CategoryTreeVO;
import com.example.gobuy.modules.product.entity.Category;

import java.util.List;

public interface IAdminCategoryService extends IService<Category> {
    List<CategoryTreeVO> getCategoryTree();
    CategoryDetailVO getCategoryDetail(Long id);
    Long createCategory(CategoryCreateDTO dto);
    void updateCategory(Long id, CategoryUpdateDTO dto);
    void deleteCategory(Long id);
}
