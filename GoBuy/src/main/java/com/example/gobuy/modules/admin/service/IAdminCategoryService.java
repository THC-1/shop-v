package com.example.gobuy.modules.admin.service;

import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.dto.CategoryCreateDTO;
import com.example.gobuy.modules.admin.dto.CategoryUpdateDTO;
import com.example.gobuy.modules.admin.vo.CategoryDetailVO;
import com.example.gobuy.modules.admin.vo.CategoryTreeVO;

import java.util.List;

public interface IAdminCategoryService {
    Result<List<CategoryTreeVO>> getCategoryTree();
    Result<CategoryDetailVO> getCategoryDetail(Long id);
    Result<Long> createCategory(CategoryCreateDTO dto);
    Result<Void> updateCategory(Long id, CategoryUpdateDTO dto);
    Result<Void> deleteCategory(Long id);
}
