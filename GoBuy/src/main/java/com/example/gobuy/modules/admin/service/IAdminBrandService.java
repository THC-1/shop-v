package com.example.gobuy.modules.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.gobuy.modules.admin.dto.BrandCreateDTO;
import com.example.gobuy.modules.admin.dto.BrandQueryDTO;
import com.example.gobuy.modules.admin.dto.BrandUpdateDTO;
import com.example.gobuy.modules.admin.vo.BrandDetailVO;
import com.example.gobuy.modules.admin.vo.BrandVO;
import com.example.gobuy.modules.product.entity.Brand;

public interface IAdminBrandService extends IService<Brand> {
    IPage<BrandVO> listBrands(BrandQueryDTO queryDTO);
    BrandDetailVO getBrandDetail(Long id);
    Long createBrand(BrandCreateDTO dto);
    void updateBrand(Long id, BrandUpdateDTO dto);
    void deleteBrand(Long id);
}
