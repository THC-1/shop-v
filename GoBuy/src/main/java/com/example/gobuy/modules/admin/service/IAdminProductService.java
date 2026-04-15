package com.example.gobuy.modules.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.admin.dto.AdminProductCreateDTO;
import com.example.gobuy.modules.admin.dto.AdminProductQueryDTO;
import com.example.gobuy.modules.admin.dto.AdminProductUpdateDTO;
import com.example.gobuy.modules.admin.dto.BatchStatusDTO;
import com.example.gobuy.modules.admin.vo.AdminProductDetailVO;
import com.example.gobuy.modules.admin.vo.AdminProductVO;
import com.example.gobuy.modules.admin.vo.BatchStatusResult;

public interface IAdminProductService {
    Result<IPage<AdminProductVO>> listProducts(AdminProductQueryDTO queryDTO);
    Result<AdminProductDetailVO> getProductDetail(Long id);
    Result<Long> createProduct(AdminProductCreateDTO dto);
    Result<Void> updateProduct(Long id, AdminProductUpdateDTO dto);
    Result<Void> deleteProduct(Long id);
    Result<Void> updateStatus(Long id, String status);
    Result<BatchStatusResult> batchUpdateStatus(BatchStatusDTO dto);
}
