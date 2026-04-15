package com.example.gobuy.modules.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.dto.BrandCreateDTO;
import com.example.gobuy.modules.admin.dto.BrandQueryDTO;
import com.example.gobuy.modules.admin.dto.BrandUpdateDTO;
import com.example.gobuy.modules.admin.vo.BrandDetailVO;
import com.example.gobuy.modules.admin.vo.BrandVO;

public interface IAdminBrandService {
    Result<IPage<BrandVO>> listBrands(BrandQueryDTO queryDTO);
    Result<BrandDetailVO> getBrandDetail(Long id);
    Result<Long> createBrand(BrandCreateDTO dto);
    Result<Void> updateBrand(Long id, BrandUpdateDTO dto);
    Result<Void> deleteBrand(Long id);
}
